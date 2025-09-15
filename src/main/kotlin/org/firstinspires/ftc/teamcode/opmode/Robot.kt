package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Pinpoint
import org.fishies.routine.ftc.extensions.HardwareMapEx

abstract class Robot : LinearOpMode() {
    val map = HardwareMapEx()

    val driver by map.deferred { gamepad1 ?: error("Driver gamepad not found") }
    val operator by map.deferred { gamepad2 ?: error("Operator gamepad not found") }

    val drivetrain by map.deferred { Drivetrain(map) }
    val pinpoint by map.deferred { Pinpoint(map) }
}