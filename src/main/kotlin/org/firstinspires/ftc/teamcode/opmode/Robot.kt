package org.firstinspires.ftc.teamcode.opmode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import dev.fishies.routine.Routine
import dev.fishies.routine.compose.stopWhen
import dev.fishies.routine.compose.parallel
import dev.fishies.routine.compose.serial
import dev.fishies.routine.ftc.drivers.CachingVoltageSensor
import dev.fishies.routine.ftc.extensions.FtcDashboard
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.instant
import dev.fishies.routine.routine
import dev.fishies.routine.unit
import dev.fishies.routine.util.SquIDController
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.Pose2
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.abs
import dev.fishies.routine.util.geometry.degrees
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.radians
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
import kotlin.let

abstract class Robot : LinearOpMode() {
    enum class State {
        READY,
        INTAKE_READY,
        INTAKING,
        OUTTAKING,
    }

    protected var currentState = State.READY

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
        slides.reset()
        run()
    }

    abstract fun run()

    enum class BucketLevel {
        LOW,
        HIGH,
    }

    fun bucketPos(level: BucketLevel) = parallel(
        pivot.rotateTo(PivotConstants.stallTopLimit.radians)
            .stopWhen { pivot.angle > PivotConstants.outtakeExtendDegrees.degrees },
        slides.extendTo(
            when (level) {
                BucketLevel.LOW -> SlideConstants.lowBucketPos
                BucketLevel.HIGH -> SlideConstants.bucketPos
            }.inches
        ),
        wrist.rotateTo(IntakeConstants.scoringPos),
        changeTo(State.OUTTAKING),
    )

    fun retract() = serial(
        wrist.rotateTo(IntakeConstants.foldedPos),
        slides.extendTo(SlideConstants.minExtension.inches),
        pivot.rotateTo(PivotConstants.bottomLimit.degrees),
        changeTo(State.READY),
    )

    fun subPosReady(
        forward: Inches = 20.inches,
        turretAngle: Radians = 0.degrees,
        inPosition: Boolean = currentState in listOf(State.INTAKING, State.INTAKE_READY),
    ) = serial(
        if (!inPosition) parallel(
            intake.control(pos = IntakeConstants.closedPos, speed = 0.0),
            wrist.rotateTo(if (pivot.angle > 70.0.degrees) IntakeConstants.groundPos - 0.075 else IntakeConstants.foldedPos),
            turret.rotateTo(0.radians),
            slides.extendTo(SlideConstants.minExtension.inches).stopWhen { slides.position < 24.inches },
        ) else unit(),

        intake.control(pos = IntakeConstants.singleIntakePos, speed = 0.0),

        parallel(
            serial(
                if (!inPosition) {
                    pivot.rotateTo(PivotConstants.bottomLimit.degrees).stopWhen { pivot.angle < 45.degrees }
                } else unit(),
                ivk(forward, IVKConstants.intakeReadyY.inches),
            ),
            turret.rotateTo(turretAngle),
        ),

        wrist.rotateTo(IntakeConstants.toptakePos),
        changeTo(State.INTAKE_READY),
    )

    fun subPos(forward: Inches = 20.inches) = serial(
        intake.control(speed = 1.0), ivk(forward, IVKConstants.intakeY.inches, instant = true), changeTo(State.INTAKING)
    )

    private val translationkP = 0.025
    private val headingkP = 0.004
    private val tolerance = 2.inches
    private val headingTolerance = 4.degrees
    fun driveTo(pose: Pose2): Routine {

        val xCtl = SquIDController(translationkP)
        val yCtl = SquIDController(translationkP)
        val hCtl = SquIDController(headingkP)

        return routine {
            drivetrain.lock()
            ready()
            while (pinpoint.pose.distanceTo(pose).let { it.first < tolerance && abs(it.second) < headingTolerance }) {
                drivetrain.drive(
                    Pose2(
                        xCtl.calculate(pose.x, pinpoint.pose.x),
                        yCtl.calculate(pose.y, pinpoint.pose.y),
                        hCtl.calculate(pose.h, pinpoint.pose.h)
                    )
                )
                yield()
            }
        }
    }

    fun changeTo(newState: State) = instant { currentState = newState }
}