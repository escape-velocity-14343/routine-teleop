package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.utility.halfLinearHalfCubic
import org.fishies.routine.RoutineManager
import org.fishies.routine.RoutineManager.onceOnTrue
import org.fishies.routine.forever
import org.fishies.routine.ftc.extensions.AnalogInput
import org.fishies.routine.ftc.extensions.ButtonInput
import org.fishies.routine.ftc.extensions.VectorInput
import org.fishies.routine.ftc.extensions.get
import org.fishies.routine.groups.timeout
import org.fishies.routine.routine
import org.fishies.routine.run
import org.fishies.routine.util.geometry.Vector2
import org.fishies.routine.util.geometry.inches
import org.fishies.routine.util.geometry.radians
import org.fishies.routine.util.math.symmetricSqrt
import kotlin.time.Duration.Companion.milliseconds

//import org.fishnpotatoes.routine.RoutineManager

@Suppress("unused")
@TeleOp(name = "TeleOpp")
open class TeleOpp : Robot() {
    override fun runOpMode() {
        map.init(hardwareMap)
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

        while (opModeIsActive()) {
            RoutineManager.tick()
            telemetry.addLine(RoutineManager.toString())
            telemetry.update()
        }
    }
}