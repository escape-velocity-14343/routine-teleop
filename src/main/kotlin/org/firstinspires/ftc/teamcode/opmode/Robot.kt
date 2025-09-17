package org.firstinspires.ftc.teamcode.opmode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import dev.fishies.routine.compose.delay
import dev.fishies.routine.compose.stopWhen
import dev.fishies.routine.compose.parallel
import dev.fishies.routine.compose.serial
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import dev.fishies.routine.ftc.extensions.FtcDashboard
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.degrees
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.radians
import dev.fishies.routine.util.geometry.sin
import dev.fishies.routine.waitUntil
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.constants.IVKConstants
import org.firstinspires.ftc.teamcode.constants.IntakeConstants
import org.firstinspires.ftc.teamcode.constants.PivotConstants
import org.firstinspires.ftc.teamcode.constants.SlideConstants
import org.firstinspires.ftc.teamcode.subroutine.ivk
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Pinpoint
import org.firstinspires.ftc.teamcode.subsystem.Pivot
import org.firstinspires.ftc.teamcode.subsystem.Slides
import org.firstinspires.ftc.teamcode.subsystem.Turret
import org.firstinspires.ftc.teamcode.subsystem.Wrist

abstract class Robot : LinearOpMode() {
    protected val map = HardwareMapEx()

    protected val driver by map.deferred { gamepad1 ?: error("Driver gamepad not found") }
    protected val operator by map.deferred { gamepad2 ?: error("Operator gamepad not found") }

    val drivetrain by map.deferred { Drivetrain(map) }
    val pinpoint by map.deferred { Pinpoint(map) }
    val slides by map.deferred { Slides(map) }
    val turret by map.deferred { Turret(map) }
    val intake by map.deferred { Intake(map) }
    val pivot by map.deferred { Pivot(map) }
    val wrist by map.deferred { Wrist(map) }

    init {
        map.deferred {
            slides.pivotAngle = { pivot.angle }
            pivot.slidePosition = { slides.position }
        }
    }

    final override fun runOpMode() {
        map.init(hardwareMap)
        CachingVoltageSensor.initialize(map)
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.telemetry)
        telemetry.msTransmissionInterval = 1
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE)
        run()
    }

    abstract fun run()

    enum class BucketLevel {
        LOW,
        HIGH,
    }

    fun bucketPos(level: BucketLevel) = parallel(
        pivot.rotateTo(PivotConstants.stallTopLimit.radians),
        serial(
            waitUntil { pivot.angle > PivotConstants.outtakeExtendDegrees.degrees },
            slides.extendTo(
                when (level) {
                    BucketLevel.LOW -> SlideConstants.lowBucketPos
                    BucketLevel.HIGH -> SlideConstants.bucketPos
                }.inches
            ),
        ),
    )

    fun retract() = serial(
        slides.extendTo(SlideConstants.minExtension.inches),
        pivot.rotateTo(PivotConstants.bottomLimit.degrees),
    )

    fun subPosReady(forward: Inches, turretAngle: Radians, alreadyInPosition: Boolean) = routine(name = "SubPosReady") {
        slides.lock()
        pivot.lock()
        wrist.lock()
        intake.lock()
        turret.lock()
        ready()

        if (!alreadyInPosition) {
            await(
                parallel(
                    intake.control(pos = IntakeConstants.closedPos, speed = 0.0),
                    wrist.rotateTo(if (pivot.angle > 70.0.degrees) IntakeConstants.groundPos - 0.075 else IntakeConstants.foldedPos),
                    turret.rotateTo(0.radians),
                    slides.extendTo(SlideConstants.minExtension.inches).stopWhen { slides.position < 24.inches },
                )
            )
        }

        await(intake.control(pos = IntakeConstants.singleIntakePos))

        if (!alreadyInPosition) {
            await(
                parallel(
                    routine {
                        ready()
                        await(pivot.rotateTo(PivotConstants.bottomLimit.degrees).stopWhen { pivot.angle < 45.degrees })
                        await(ivk(forward, IVKConstants.intakeReadyY.inches))
                    },
                    turret.rotateTo(turretAngle),
                )
            )
        }

        await(wrist.rotateTo(IntakeConstants.toptakePos))
    }
}