package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE
import org.fishies.routine.Subsystem
import org.fishies.routine.ftc.extensions.HardwareMapEx
import org.fishies.routine.util.geometry.Pose2
import org.fishies.routine.util.geometry.Radians
import org.fishies.routine.util.geometry.Vector2

class Drivetrain(map: HardwareMapEx) : Subsystem() {
    val flMotor by map.deferred<DcMotor>("frontLeft") { direction = REVERSE }
    val frMotor by map.deferred<DcMotor>("frontRight")
    val blMotor by map.deferred<DcMotor>("backLeft") { direction = REVERSE }
    val brMotor by map.deferred<DcMotor>("backRight")

    fun drive(power: Pose2) {
        val (x, y, h) = power.xyh
        flMotor.power = x.inches - y.inches - h.radians
        frMotor.power = x.inches + y.inches + h.radians
        blMotor.power = x.inches + y.inches - h.radians
        brMotor.power = x.inches - y.inches + h.radians
    }

    fun drive(translationPower: Vector2, headingPower: Radians) = drive(Pose2(translationPower, headingPower))

    override fun tick() {}
}