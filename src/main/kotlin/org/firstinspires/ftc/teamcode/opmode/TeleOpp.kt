package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.RoutineManager.run
import dev.fishies.routine.compose.timeout
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.AnalogInput
import dev.fishies.routine.ftc.extensions.ButtonInput
import dev.fishies.routine.ftc.extensions.VectorInput
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
    override fun run() {
        waitForStart()
        routine {
            drivetrain.lock()
            restart = true
            ready()
            forever {
                drivetrain.drive(
                    driver[VectorInput.STICK_LEFT].yx.rotated(-pinpoint.pose.heading).halfLinearHalfCubic(),
                    -driver[AnalogInput.STICK_X_RIGHT].symmetricSqrt().radians
                )
            }
        }.run()

        driver[ButtonInput.A].onceOnTrue {
            routine {
                drivetrain.lock()
                ready()
                forever { drivetrain.drive(Vector2(0.inches, 0.inches), 0.radians) }
            }.timeout(1500.milliseconds).timeout(2000.milliseconds).run()
        }

        telemetry.msTransmissionInterval = 1
        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addLine("$RoutineManager")
            telemetry.update()
        }

        RoutineManager.reset()
    }
}