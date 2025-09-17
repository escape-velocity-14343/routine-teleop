package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import dev.fishies.routine.ftc.extensions.FtcDashboard
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.routine
import dev.fishies.routine.util.SquIDController
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.sin
import org.firstinspires.ftc.teamcode.constants.SlideConstants
import kotlin.math.roundToInt

class Slides(map: HardwareMapEx) : Subsystem() {
    private var positionTicks = 0
    private var resetOffset = 0
    private val motor0 by map.deferred<DcMotor>("slide0") {
        direction = REVERSE
        zeroPowerBehavior = FLOAT
    }
    private val motor1 by map.deferred<DcMotor>("slide1") {
        zeroPowerBehavior = FLOAT
    }

    private val controller = SquIDController(SlideConstants.kP)

    val position: Inches
        get() = (positionTicks / SlideConstants.ticksPerInch).inches
    var extensionPowerMul = 1.0

    var target: Inches = 0.0.inches
        set(value) {
            manualControl = false
            field = value
        }
    val targetTicks: Int get() = (target.inches * SlideConstants.ticksPerInch).roundToInt()

    var pivotAngle: () -> Radians = { Radians.ZERO }

    var manualControl = false

    /**
     * @param target in inches, uses the same one as the pid target
     */
    fun isClose(target: Inches) = target in position - SlideConstants.tolerance..position + SlideConstants.tolerance

    /**
     * Internal Factory Method
     *
     * @param ticks
     */
    private fun extendToPosition(ticks: Int) {
        // extensionPowerMul only applies to the squid output because the feedforward should stay constant
        val squid = controller.calculate(ticks.toDouble(), positionTicks.toDouble())
        val static = SlideConstants.kS + dynamicFeedforward(position) * sin(pivotAngle())
        val power: Double = CachingVoltageSensor.normalize(squid * extensionPowerMul + static)
        setPower(power)
    }

    private fun setPower(power: Double) {
        motor0.power = power * SlideConstants.direction
        motor1.power = -power * SlideConstants.direction

        FtcDashboard.telemetry.addData("slide position", position);
        FtcDashboard.telemetry.addData("slide motor power", power);
    }

    private fun dynamicFeedforward(position: Inches): Double {
        val y1: Double = SlideConstants.feedForwardBottom
        val x2: Double = SlideConstants.bucketPos
        val y2: Double = SlideConstants.feedForwardTop

        return y1 + position.inches * (y2 - y1) / x2
    }

    /**
     * Resets the position of the slides
     */
    fun reset() {
        motor0.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor0.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        resetOffset = 0
    }

    override fun tick() {
        positionTicks = -motor0.currentPosition - resetOffset

        if (!manualControl && position < SlideConstants.maxExtension.inches) {
            extendToPosition(targetTicks)
        }

        if (positionTicks < 0) {
            reset()
        }

        FtcDashboard.telemetry.addData("slide position", this.position)
        FtcDashboard.telemetry.addData("slide motor power", motor0.power)
    }

    fun extendTo(target: Inches, instant: Boolean = false) = routine(name = "SlidesTo$target") {
        this@Slides.lock()
        ready()
        this@Slides.target = target
        if (!instant) while (!isClose(target)) yield()
    }
}