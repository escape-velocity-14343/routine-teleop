package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.PwmControl
import com.qualcomm.robotcore.hardware.ServoImplEx
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.routine
import dev.fishies.routine.compose.delay
import org.firstinspires.ftc.teamcode.constants.IntakeConstants
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

class Wrist(map: HardwareMapEx) : Subsystem() {
    private val wrist by map.deferred<ServoImplEx>("wrist") { pwmRange = PwmControl.PwmRange(500.0, 2500.0) }

    var position = 0.0
        set(value) {
            field = value
            wrist.position = field + IntakeConstants.wristOffset
        }

    var pwmDisabled = false
        set(disabled) {
            field = disabled
            if (disabled) wrist.setPwmDisable() else wrist.setPwmEnable()
        }

    fun rotateTo(target: Double) = routine(name = "WristTo$target") {
        ready()
        val rotationTime = IntakeConstants.timeMultiplier.seconds * abs(position - target)
        position = target
        delay(rotationTime)
    }
}