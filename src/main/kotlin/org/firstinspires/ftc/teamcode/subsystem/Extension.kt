package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import dev.fishies.routine.ftc.extensions.DashboardEx
import dev.fishies.routine.ftc.extensions.FtcDashboard
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.util.SquIDController
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.inches
import kotlin.math.roundToInt

object ExtensionConstants {
    val kP by DashboardEx["Extension/kP", 0.004]
    val kS by DashboardEx["Extension/kS", 0.004]
    val ticksPerInch by DashboardEx["Extension/ticksPerInch", 54.9 / 1.5555555555555556]

    /**
     * Feedforward value that is multiplied by `Math.cos(slideAngle)`
     */
    val feedForwardBottom by DashboardEx["Extension/feedForwardBottom", 0.2]
    val feedForwardTop by DashboardEx["Extension/feedForwardTop", 0.25]
    val direction by DashboardEx["Extension/direction", 1.0]

    val bucketPos by DashboardEx["Extension/bucketPos", 31.0]
}

class Extension(map: HardwareMapEx) : Subsystem() {
    private var positionTicks = 0
    private var resetOffset = 0
    private val motor0 by map.deferred<DcMotor>("slide0") {
        direction = REVERSE
        zeroPowerBehavior = FLOAT
    }
    private val motor1 by map.deferred<DcMotor>("slide1") {
        zeroPowerBehavior = FLOAT
    }

    private val controller = SquIDController(ExtensionConstants.kP)

    val position: Inches
        get() = (positionTicks / ExtensionConstants.ticksPerInch).inches
    var extensionPowerMul = 1.0

    var targetPosition: Inches = 0.0.inches
    val targetPositionTicks: Int get() = (targetPosition.inches * ExtensionConstants.ticksPerInch).roundToInt()

    /**
     * Internal Factory Method
     *
     * @param ticks
     */
    private fun extendToPosition(ticks: Int) {
        // extensionPowerMul only applies to the squid output because the feedforward should stay constant
        var power: Double = +controller.calculate(
            ticks.toDouble(), positionTicks.toDouble()
        ) * extensionPowerMul + ExtensionConstants.kS /* TODO: once pivot subsystem exists /* + dynamicFeedForward(position) * sin(Math.toRadians(angleSupplier.get()))) */ */

        power = CachingVoltageSensor.normalize(power)

        setPower(power)
    }

    private fun extendTo(target: Inches) {
        targetPosition = target
        extendToPosition(targetPositionTicks)
    }

    private fun setPower(power: Double) {
        motor0.power = power * ExtensionConstants.direction
        motor1.power = -power * ExtensionConstants.direction

        FtcDashboard.telemetry.addData("slide position", position);
        FtcDashboard.telemetry.addData("slide motor power", power);
    }

    private fun dynamicFeedForward(x: Double): Double {
        val y1: Double = ExtensionConstants.feedForwardBottom
        val x2: Double = ExtensionConstants.bucketPos
        val y2: Double = ExtensionConstants.feedForwardTop

        return y1 + x * (y2 - y1) / x2
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

        extendTo(targetPosition)

        if (positionTicks < 0) {
            reset()
        }

        FtcDashboard.telemetry.addData("slide position", this.position)
        FtcDashboard.telemetry.addData("slide motor power", motor0.power)
    }
}