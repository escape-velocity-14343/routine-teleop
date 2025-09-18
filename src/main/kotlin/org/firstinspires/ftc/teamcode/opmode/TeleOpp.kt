package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.fishies.routine.RoutineManager
import dev.fishies.routine.RoutineManager.onceOnFalse
import dev.fishies.routine.RoutineManager.onceOnTrue
import dev.fishies.routine.RoutineManager.start
import dev.fishies.routine.compose.serial
import dev.fishies.routine.forever
import dev.fishies.routine.ftc.extensions.Analog
import dev.fishies.routine.ftc.extensions.Button
import dev.fishies.routine.ftc.extensions.DashboardEx
import dev.fishies.routine.ftc.extensions.Vector
import dev.fishies.routine.ftc.extensions.and
import dev.fishies.routine.ftc.extensions.current
import dev.fishies.routine.ftc.extensions.get
import dev.fishies.routine.ftc.extensions.or
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.degrees
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.radians
import dev.fishies.routine.util.math.symmetricSqrt
import org.firstinspires.ftc.teamcode.constants.IntakeConstants
import org.firstinspires.ftc.teamcode.utility.halfLinearHalfCubic

@Suppress("unused")
@TeleOp(name = "TeleOpp")
open class TeleOpp : Robot() {
    val dashVar by DashboardEx["myVar", "hi"]
    override fun run() {
        configureDriver()
        waitForStart()
        pivot.manualControl = true
        slides.manualControl = true

        routine {
            drivetrain.lock()
            restart = true
            ready()
            forever {
                drivetrain.drive(
                    driver[Vector.STICK_LEFT].yx.rotated(-pinpoint.pose.h).halfLinearHalfCubic(),
                    -driver[Analog.STICK_X_RIGHT].symmetricSqrt().radians
                )
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

    fun configureDriver() {
        driver[Button.OPTIONS and Button.SHARE].onceOnTrue { pinpoint.resetYaw() }

        driver[Button.A].onceOnTrue { retract().start() }
        driver[Button.X].onceOnTrue { bucketPos(BucketLevel.LOW).start() }

        driver[Button.Y or Button.B].onceOnTrue {
            subPosReady(turretAngle = if (driver.current(Button.Y)) 0.0.degrees else 90.0.degrees).start()
        }

        driver[Button.STICK_RIGHT].onceOnTrue { subPos().start() }.onceOnFalse { subPosReady().start() }

        var lastClawPos = 0.0
        driver[Button.BUMPER_LEFT].onceOnTrue {
            lastClawPos = intake.clawPos
            intake.control(
                pos = IntakeConstants.openPos, speed = if (currentState == State.OUTTAKING) -0.1 else 0.0
            ).start()
        }.onceOnFalse { intake.control(pos = lastClawPos, speed = 0.0).start() }
    }
}