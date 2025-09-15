package org.firstinspires.ftc.teamcode.utility

import dev.fishies.routine.util.geometry.Vector2

fun Double.halfLinearHalfCubic() = this / 2.0 + this * this * this / 2.0
fun Vector2.halfLinearHalfCubic() = this / 2.0 + this * this * this / 2.0