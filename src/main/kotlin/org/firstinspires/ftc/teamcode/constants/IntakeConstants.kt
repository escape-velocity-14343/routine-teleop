package org.firstinspires.ftc.teamcode.constants

import dev.fishies.routine.ftc.extensions.DashboardEx

object IntakeConstants {
    // wrist constants
    // It takes 0.4 seconds from scoring pos reversed to scoring Pos
    val foldedPos by DashboardEx["Intake/foldedPos", 0.25]
    val groundPos by DashboardEx["Intake/groundPos", 0.4]
    val halfFoldPos by DashboardEx["Intake/halfFoldPos", 0.41]
    val dunkScoringPos by DashboardEx["Intake/dunkScoringPos", 0.1]
    val scoringPos by DashboardEx["Intake/scoringPos", 0.25]
    val specimenScoringPos by DashboardEx["Intake/specimenScoringPos", 0.4]
    val scoringPosReversed by DashboardEx["Intake/scoringPosReversed", 0.50]
    val specimenReadyPos by DashboardEx["Intake/specimenReadyPos", 0.63]
    val intakeReadyPos by DashboardEx["Intake/intakeReadyPos", 0.8]
    val toptakePos by DashboardEx["Intake/toptakePos", 0.55]
    val wristTolerance by DashboardEx["Intake/wristTolerance", 0.1]

    // wrist command constants
    val timeMultiplier by DashboardEx["Intake/timeMultiplier", 0.6]

    // claw constants
    // fronttake
    val openPos by DashboardEx["Intake/openPos", 0.675]
    val closedPos by DashboardEx["Intake/closedPos", 0.92] //holy fuck are we stalling ts
    val singleIntakePos by DashboardEx["Intake/singleIntakePos", 0.875]
    val slightOpenPos by DashboardEx["Intake/slightOpenPos", 0.72]

    val clawOffset by DashboardEx["Intake/clawOffset", -0.35]

    // turret constants
    val minAngle by DashboardEx["Intake/minAngle", -130.0]
    val maxAngle by DashboardEx["Intake/maxAngle", 130.0]

    // randomly worked from old folded pos
    val rightAnglePos by DashboardEx["Intake/rightAnglePos", 0.86]

    // auto constants
    val autoOuttakeSpeed by DashboardEx["Intake/autoOuttakeSpeed", -0.15]
    val autoIntakeSpeed by DashboardEx["Intake/autoIntakeSpeed", 1.0]
    val autoIntakeClawLerp by DashboardEx["Intake/autoIntakeClawLerp", 0.5]

    // auto heading alignment
    val autoAlignP by DashboardEx["Intake/autoAlignP", -0.3]
    val autoAlignTol by DashboardEx["Intake/autoAlignTol", 10.0]

    // global offset
    // 1 tooth of skip = 0.05 position
    val wristOffset by DashboardEx["Intake/wristOffset", -0.04]

    val intakeSensorVoltageThres by DashboardEx["Intake/intakeSensorVoltageThres", 1.69]
    val hangReady by DashboardEx["Intake/hangReady", 0.35]
    val intakeAccelThres by DashboardEx["Intake/intakeAccelThres", 40.0]
}
