package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.subsystem.*
import dev.fishies.routine.ftc.extensions.HardwareMapEx

abstract class Robot : LinearOpMode() {
    val map = HardwareMapEx()

    val driver by map.deferred { gamepad1 ?: error("Driver gamepad not found") }
    val operator by map.deferred { gamepad2 ?: error("Operator gamepad not found") }

    val drivetrain by map.deferred { Drivetrain(map) }
    val pinpoint by map.deferred { Pinpoint(map) }
    val extension by map.deferred { Extension(map) }

    final override fun runOpMode() {
        map.init(hardwareMap)
        run()
    }

    abstract fun run()
}