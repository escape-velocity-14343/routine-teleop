package org.firstinspires.ftc.teamcode.opmode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.RoutineManager.start
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.*
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.degrees

@Suppress("unused")
@TeleOp(name = "PivotTest", group = "Tests")
class PivotTest : Robot() {
    val targetAngle by DashboardEx["Target Angle", 45.0]
    override fun run() {
        waitForStart()

        pivot.manualControl = true
        slides.manualControl = true

        routine {
            pivot.lock()
            restart = true
            ready()
            forever {
                pivot.target = targetAngle.degrees
            }
        }.start()

        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addData("pivot angle", "${pivot.angle}")
            telemetry.addData("slide position", "${slides.position}")
            telemetry.addLine("$RoutineManager")
            telemetry.update()
        }

        RoutineManager.reset()
    }
}