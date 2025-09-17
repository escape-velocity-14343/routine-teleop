package org.firstinspires.ftc.teamcode.constants

import dev.fishies.routine.ftc.extensions.DashboardEx

object IVKConstants {
    val slideLength by DashboardEx["IVK/slideLength", 12.0]

    // in inches
    val lengthOfBot by DashboardEx["IVK/lengthOfBot", 13.38]

    // in inches
    val widthOfBot by DashboardEx["IVK/widthOfBot", 12.94]

    // in inches
    val intakeLength by DashboardEx["IVK/intakeLength", 7.0]

    //Offset constants
    val ivkCenterOffset by DashboardEx["IVK/ivkCenterOffset", 7.0]
    val backOfSlidesToCenterOffset by DashboardEx["IVK/backOfSlidesToCenterOffset", 5.25]
    val neutralX by DashboardEx["IVK/neutralX", 12.0]
    val neutralY by DashboardEx["IVK/neutralY", 8.0]
    val intakeY by DashboardEx["IVK/intakeY", 9.0]
    val intakeReadyY by DashboardEx["IVK/intakeReadyY", 11.0]
    val slideBackOffset by DashboardEx["IVK/slideBackOffset", 1.5]
    val slideRotationOffset by DashboardEx["IVK/slideRotationOffset", 0.0]
    val pivotPointForwardOffset by DashboardEx["IVK/pivotPointForwardOffset", -3.36]
    val pivotPointHeightOffset by DashboardEx["IVK/pivotPointHeightOffset", 5.28]
    val cameraOffsetForward by DashboardEx["IVK/cameraOffsetForward", 4.78]

    val cameraOffsetUp by DashboardEx["IVK/cameraOffsetUp", -2.33]
    val clawOffsetForward by DashboardEx["IVK/clawOffsetForward", 9.25]
    val clawOffsetUp by DashboardEx["IVK/clawOffsetUp", -9.5]
    val extensionScalar by DashboardEx["IVK/extensionScalar", 0.85]
    val clawIntakeIVKHeight by DashboardEx["IVK/clawIntakeIVKHeight", 0.45]
}
