package org.firstinspires.ftc.teamcode.subroutine

import dev.fishies.routine.compose.parallel
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.atan2
import dev.fishies.routine.util.geometry.inches
import org.firstinspires.ftc.teamcode.constants.IVKConstants as Constants
import org.firstinspires.ftc.teamcode.opmode.Robot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

private fun getTargetExtension(x: Inches, y: Inches): Inches {
    return max(sqrt(x.inches.pow(2.0) + (y - Constants.pivotPointHeightOffset).inches.pow(2.0)), 0.0).inches
}

private fun getTargetAngleDegrees(x: Inches, y: Inches): Radians {
    return atan2(y - Constants.pivotPointHeightOffset, x)
}

fun Robot.ivk(x: Inches, y: Inches, instant: Boolean = false) = parallel(
    slides.extendTo(getTargetExtension(x + Constants.ivkCenterOffset, y), instant),
    pivot.rotateTo(getTargetAngleDegrees(x + Constants.ivkCenterOffset, y), instant)
)