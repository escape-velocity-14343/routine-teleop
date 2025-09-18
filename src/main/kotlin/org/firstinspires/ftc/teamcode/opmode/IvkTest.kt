package org.firstinspires.ftc.teamcode.opmode

import android.R.attr.x
import android.R.attr.y
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.RoutineManager.start
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.*
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.inches
import org.firstinspires.ftc.teamcode.subroutine.getIvkTargetAngle
import org.firstinspires.ftc.teamcode.subroutine.getIvkTargetDistance
import org.firstinspires.ftc.teamcode.subroutine.ivk

@Suppress("unused")
@TeleOp(name = "IvkTest", group = "Tests")
class IvkTest : Robot() {
    val move by DashboardEx["Move", false]
    val targetX by DashboardEx["Target X", 0.0]
    val targetY by DashboardEx["Target Y", 0.0]
    override fun run() {
        waitForStart()
        pivot.manualControl = true
        slides.manualControl = true

        routine {
            pivot.lock()
            slides.lock()
            restart = true
            ready()
            forever {
                val distance = getIvkTargetDistance(targetX.inches, targetY.inches)
                val angle = getIvkTargetAngle(targetX.inches, targetY.inches)
                telemetry.addData("target slide position", "$distance")
                telemetry.addData("target pivot angle", "$angle")
                if (move) {
                    slides.target = distance
                    pivot.target = angle
                }
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