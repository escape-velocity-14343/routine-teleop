package org.firstinspires.ftc.teamcode.subroutine

import dev.fishies.routine.compose.parallel
import dev.fishies.routine.util.geometry.Inches
import dev.fishies.routine.util.geometry.atan2
import dev.fishies.routine.util.geometry.hypot
import dev.fishies.routine.util.geometry.inches
import org.firstinspires.ftc.teamcode.opmode.Robot
import org.firstinspires.ftc.teamcode.constants.IVKConstants as Constants

fun getIvkTargetDistance(x: Inches, y: Inches) =
    hypot(x, y - Constants.pivotPointHeightOffset).coerceAtLeast(0.0.inches)

fun getIvkTargetAngle(x: Inches, y: Inches) = atan2(y - Constants.pivotPointHeightOffset, x)

fun Robot.ivk(x: Inches, y: Inches, instant: Boolean = false) = parallel(
    slides.extendTo(getIvkTargetDistance(x, y), instant), pivot.rotateTo(getIvkTargetAngle(x, y), instant)
)