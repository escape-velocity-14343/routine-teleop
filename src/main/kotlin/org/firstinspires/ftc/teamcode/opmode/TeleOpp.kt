package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.utility.halfLinearHalfCubic
import org.fishnpotatoes.routine.RoutineManager
import org.fishnpotatoes.routine.RoutineManager.onceOnTrue
import org.fishnpotatoes.routine.forever
import org.fishnpotatoes.routine.ftc.extensions.AnalogInput
import org.fishnpotatoes.routine.ftc.extensions.ButtonInput
import org.fishnpotatoes.routine.ftc.extensions.VectorInput
import org.fishnpotatoes.routine.ftc.extensions.get
import org.fishnpotatoes.routine.groups.deadline
import org.fishnpotatoes.routine.groups.timeout
import org.fishnpotatoes.routine.groups.wait
import org.fishnpotatoes.routine.routine
import org.fishnpotatoes.routine.run
import org.fishnpotatoes.routine.util.geometry.Vector2
import org.fishnpotatoes.routine.util.geometry.inches
import org.fishnpotatoes.routine.util.geometry.radians
import org.fishnpotatoes.routine.util.math.symmetricSqrt
import kotlin.math.sqrt
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