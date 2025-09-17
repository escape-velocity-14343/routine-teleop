package org.firstinspires.ftc.teamcode.constants

import dev.fishies.routine.ftc.extensions.DashboardEx

object SlideConstants {
    val kP by DashboardEx["Extension/kP", 0.004]
    val kS by DashboardEx["Extension/kS", 0.004]
    val ticksPerInch by DashboardEx["Extension/ticksPerInch", 54.9 / 1.5555555555555556]

    /**
     * Feedforward value that is multiplied by `Math.cos(slideAngle)`
     */
    val feedForwardBottom by DashboardEx["Extension/feedForwardBottom", 0.2]
    val feedForwardTop by DashboardEx["Extension/feedForwardTop", 0.25]
    val direction by DashboardEx["Extension/direction", 1.0]

    val bucketPos by DashboardEx["Extension/bucketPos", 31.0]

    val maxExtension by DashboardEx["Extension/maxExtension", 31.0]

    val autonBucketPos by DashboardEx["Extension/autonBucketPos", 30.0]
    val lowBucketPos by DashboardEx["Extension/lowBucketPos", 11.5]

    val minExtension by DashboardEx["Extension/minExtension", 0.0]
    val tolerance by DashboardEx["Extension/tolerance", 1.0]
    val alertCurrent by DashboardEx["Extension/alertCurrent", 4.0]

    val submersibleIntakeMinExtension by DashboardEx["Extension/submersibleIntakeMinExtension", 10.0]
    val submersibleIntakeMaxExtension by DashboardEx["Extension/submersibleIntakeMaxExtension", 20.0]

    val specimenRaisePosition by DashboardEx["Extension/specimenRaisePosition", 0.0]
    val specimenHighRaisePosition by DashboardEx["Extension/specimenHighRaisePosition", 13.0]
    val specimenHookPosition by DashboardEx["Extension/specimenHookPosition", 16.75]

    val millisPerInch by DashboardEx["Extension/millisPerInch", 5 * ticksPerInch]

    val highExtendInches by DashboardEx["Extension/highExtendInches", 1.5]
    val lowExtendInches by DashboardEx["Extension/lowExtendInches", -1.0]
    val extendedThreshold by DashboardEx["Extension/extendedThreshold", 3.0]

    val manualControlDeadband by DashboardEx["Extension/manualControlDeadband", 0.1]
    val highExtend by DashboardEx["Extension/highExtend", false]
    val lowExtend by DashboardEx["Extension/lowExtend", false]
}