package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.ftc.extensions.Button
import dev.fishies.routine.ftc.extensions.DashboardEx
import dev.fishies.routine.ftc.extensions.get
import dev.fishies.routine.util.geometry.Pose2
import dev.fishies.routine.util.geometry.degrees
import dev.fishies.routine.util.geometry.inches

@Suppress("unused")
@TeleOp(name = "DrivetrainSquidTest", group = "Tests")
class DrivetrainSquidTest : Robot() {
    val targetX by DashboardEx["Target X", 0.0]
    val targetY by DashboardEx["Target Y", 0.0]
    val targetH by DashboardEx["Target H", 0.0]

    override fun run() {
        waitForStart()
        pivot.manualControl = true
        slides.manualControl = true

        driver[Button.A].onceOnTrue { driveTo(Pose2(targetX.inches, targetY.inches, targetH.degrees)) }

        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addData("pp x", pinpoint.pose.x)
            telemetry.addData("pp y", pinpoint.pose.y)
            telemetry.addData("pp h", pinpoint.pose.h)
            telemetry.update()
        }

        RoutineManager.reset()
    }
}