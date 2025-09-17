package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import dev.fishies.routine.ftc.drivers.SensOrangeAbsoluteEncoder
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.util.SquIDController
import dev.fishies.routine.util.Timer
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.cos
import dev.fishies.routine.util.geometry.degrees
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.plus
import dev.fishies.routine.util.geometry.radians
import org.firstinspires.ftc.teamcode.constants.PivotConstants
import org.firstinspires.ftc.teamcode.constants.SlideConstants
import kotlin.time.DurationUnit

class Pivot(map: HardwareMapEx) : Subsystem() {
    private var loopTimer = Timer()
    private var lastAngle = Radians.ZERO
    private var pivotVelocity = Radians.ZERO

    private val encoder by map.deferred<SensOrangeAbsoluteEncoder>("sensOrange") {
        offset = PivotConstants.encoderOffset.radians
        inverted = PivotConstants.encoderInvert
    }
    private val motor0 by map.deferred<DcMotor>("tilt0") { direction = REVERSE }
    private val motor1 by map.deferred<DcMotor>("tilt1") { direction = REVERSE }

    private val controller = SquIDController(PivotConstants.kPRetracted)

    private var _angle = Radians.ZERO
    val angle get() = _angle
    var target = Radians.ZERO
        set(value) {
            manualControl = false
            field = value
        }

    var manualControl = false

    var slidePosition: () -> Inches = { 0.0.inches }

    private fun setPower(power: Double) {
        motor0.power = power * PivotConstants.direction
        motor1.power = -power * PivotConstants.direction
    }

    private fun interpolateKp(slidePosition: Inches): Double {
        val x1 = 0.0
        val y1 = PivotConstants.kPRetracted
        val x2 = SlideConstants.bucketPos
        val y2 = PivotConstants.kPExtended

        return y1 + slidePosition.inches * (y2 - y1) / (x2 - x1)
    }

    private fun interpolateKg(slidePosition: Inches): Double {
        val x1 = 0.0
        val y1 = PivotConstants.kGRetracted
        val x2 = SlideConstants.bucketPos
        val y2 = PivotConstants.kGFullyExtended

        return y1 + slidePosition.inches * (y2 - y1) / (x2 - x1)
    }

    private fun interpolatedRawFeedforward(): Double {
        return interpolateKp(slidePosition())
    }

    private fun interpolatedRawFeedforwardkG(): Double {
        return interpolateKg(slidePosition())
    }

    private fun getKg(): Double {
        return interpolatedRawFeedforwardkG() * cos(angle)
    }

    /**
     * @param target in inches, use the same one as the pid target
     */
    fun isClose(target: Radians): Boolean {
        return isClose(target, PivotConstants.tolerance.radians)
    }

    /**
     * @param target in inches, use the same one as the pid target
     */
    fun isClose(target: Radians, tolerance: Radians): Boolean {
        return target in angle - tolerance..angle + tolerance
    }

    fun tiltToPos(target: Radians) {
        this.target = target
        var power = CachingVoltageSensor.normalize(controller.calculate(target.degrees, angle.degrees)) + getKg()

        if (power <= 0 && isClose(target) && target.radians == PivotConstants.bottomLimit) {
            power = -0.05
        }

        if (power > 0 && angle < 20.degrees) {
            power *= PivotConstants.bottomPMult
        }

        setPower(power)
    }

    override fun tick() {
        _angle = encoder.angle
        // lastAngle radians - angle radians    radians
        // --------------------------------- = --------- = radians per second
        //              seconds                 seconds
        pivotVelocity = (lastAngle - angle) / loopTimer.elapsed.toDouble(DurationUnit.SECONDS)
        lastAngle = angle

        controller.p = interpolatedRawFeedforward()

        if (!manualControl) {
            tiltToPos(target)
        }

        loopTimer.reset()
    }
}