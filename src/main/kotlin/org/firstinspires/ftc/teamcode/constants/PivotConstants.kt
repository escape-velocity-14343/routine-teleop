package org.firstinspires.ftc.teamcode.constants

import dev.fishies.routine.ftc.extensions.DashboardEx

object PivotConstants {
    val kPRetracted by DashboardEx["Pivot/kPRetracted", 0.0125]

    val kPExtended by DashboardEx["Pivot/kPExtended", 0.03]
    val kGRetracted by DashboardEx["Pivot/kGRetracted", 0.0]
    val kGFullyExtended by DashboardEx["Pivot/kGFullyExtended", 0.1]

    val maxPivotVelocity by DashboardEx["Pivot/maxPivotVelocity", 1.0]

    val bottomLimit by DashboardEx["Pivot/bottomLimit", 0.5]
    val topLimit by DashboardEx["Pivot/topLimit", 86.0]
    val stallTopLimit by DashboardEx["Pivot/stallTopLimit", 90.0]
    val tolerance by DashboardEx["Pivot/tolerance", 1.5]
    val direction by DashboardEx["Pivot/direction", -1.0]
    val encoderInvert by DashboardEx["Pivot/encoderInvert", true]
    val encoderOffset by DashboardEx["Pivot/encoderOffset", -54.982]
    val outtakeExtendDegrees by DashboardEx["Pivot/outtakeExtendDegrees", 45.0]
    val retractDegrees by DashboardEx["Pivot/retractDegrees", bottomLimit]

    val neutralPos by DashboardEx["Pivot/neutralPos", 25.0]
    val intakeReadyPos by DashboardEx["Pivot/intakeReadyPos", 15.0]
    val intakePos by DashboardEx["Pivot/intakePos", 10.0]

    val specimenIntakeAngle by DashboardEx["Pivot/specimenIntakeAngle", topLimit]
    val specimenTopBarAngle by DashboardEx["Pivot/specimenTopBarAngle", 63.0]

    val manualControlDeadband by DashboardEx["Pivot/manualControlDeadband", 0.1]

    val bottomPMult by DashboardEx["Pivot/bottomPMult", 1.25]
}