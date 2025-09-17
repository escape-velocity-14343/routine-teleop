package org.firstinspires.ftc.teamcode.opmode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.RoutineManager.start
import dev.fishies.routine.compose.timeout
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.AnalogInput
import dev.fishies.routine.ftc.extensions.ButtonInput
import dev.fishies.routine.ftc.extensions.DashboardEx
import dev.fishies.routine.ftc.extensions.FtcDashboard
import dev.fishies.routine.ftc.extensions.VectorInput
import dev.fishies.routine.ftc.extensions.and
import dev.fishies.routine.ftc.extensions.get
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.Vector2
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.radians
import dev.fishies.routine.util.math.symmetricSqrt
import org.firstinspires.ftc.teamcode.utility.halfLinearHalfCubic
import kotlin.time.Duration.Companion.milliseconds

@Suppress("unused")
@TeleOp(name = "TeleOpp")
open class TeleOpp : Robot() {
    val dashVar by DashboardEx["config/myVar", "hi"]
    override fun run() {
        configureDriver()
        waitForStart()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.telemetry)

        pivot.manualControl = true
        slides.manualControl = true

        routine {
            drivetrain.lock()
            restart = true
            ready()
            forever {
                drivetrain.drive(
                    driver[VectorInput.STICK_LEFT].yx.rotated(-pinpoint.pose.h).halfLinearHalfCubic(),
                    -driver[AnalogInput.STICK_X_RIGHT].symmetricSqrt().radians
                )
            }
        }.start()

        routine {
            ready()
            forever {
                telemetry.addLine(dashVar)
            }
        }.start()

        driver[ButtonInput.A].onceOnTrue {
            routine {
                drivetrain.lock()
                ready()
                forever { drivetrain.drive(Vector2(0.inches, 0.inches), 0.radians) }
            }.timeout(1500.milliseconds).timeout(2000.milliseconds).start()
        }

        telemetry.msTransmissionInterval = 1
        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addData("pivot angle:", "${pivot.angle}")
            telemetry.addData("slide position:", "${slides.position}")
            telemetry.addLine("$RoutineManager")
            telemetry.update()
        }

        RoutineManager.reset()
    }

    fun configureDriver() {
        driver[ButtonInput.OPTIONS and ButtonInput.SHARE].onceOnTrue { pinpoint.resetYaw() }

        //driver[ButtonInput.A].onceOnTrue { pivot.target =  }
    }
}