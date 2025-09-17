package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import org.firstinspires.ftc.teamcode.subsystem.*
import dev.fishies.routine.ftc.extensions.HardwareMapEx

abstract class Robot : LinearOpMode() {
    val map = HardwareMapEx()

    val driver by map.deferred { gamepad1 ?: error("Driver gamepad not found") }
    val operator by map.deferred { gamepad2 ?: error("Operator gamepad not found") }

    val drivetrain by map.deferred { Drivetrain(map) }
    val pinpoint by map.deferred { Pinpoint(map) }
    val slides by map.deferred { Slides(map) }
    val pivot: Pivot by map.deferred { Pivot(map) }

    init {
        map.deferred {
            slides.pivotAngle = { pivot.angle }
            pivot.slidePosition = { slides.position }
        }
    }

    final override fun runOpMode() {
        map.init(hardwareMap)
        CachingVoltageSensor.initialize(map)
        run()
    }

    abstract fun run()
}