package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.start
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.*
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.degrees
import org.firstinspires.ftc.teamcode.constants.IntakeConstants

@Suppress("unused")
@TeleOp(name = "IntakeTest", group = "Tests")
class IntakeTest : Robot() {
    val wristPosition by DashboardEx["Wrist position", IntakeConstants.foldedPos]
    val turretPosition by DashboardEx["Turret degrees", 0.0]
    val clawerPosition by DashboardEx["Claw position", 0.0]
    val intakeSpeed by DashboardEx["Intake speed", 0.0]
    override fun run() {
        waitForStart()
        pivot.manualControl = true
        slides.manualControl = true

        routine {
            wrist.lock()
            turret.lock()
            intake.lock()
            restart = true
            ready()
            forever {
                wrist.position = wristPosition
                turret.position = turretPosition.degrees
                intake.clawPos = clawerPosition
                intake.intakeSpeed = intakeSpeed
            }
        }.start()

        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addData("pivot angle", "${pivot.angle}")
            telemetry.addData("intake proximity", "${intake.proximity}")
            telemetry.addData("slide position", "${slides.position}")
            telemetry.addLine("$RoutineManager")
            telemetry.update()
        }

        RoutineManager.reset()
    }
}