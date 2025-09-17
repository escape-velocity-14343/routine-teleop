package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.Servo.Direction.REVERSE
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.routine
import dev.fishies.routine.util.Timer
import org.firstinspires.ftc.teamcode.constants.IntakeConstants as Constants
import kotlin.time.Duration.Companion.seconds

class Intake(map: HardwareMapEx) : Subsystem() {
    private val resetTimer = Timer()

    private val intake by map.deferred<CRServo>("intake")
    private val clawer by map.deferred<Servo>("clawer") { direction = REVERSE }
    private val prox by map.deferred<AnalogInput>("clawProx")

    /**
     * Speeds from 1.0 to -1.0. Positive is outtake, negative is intake
     */
    var intakeSpeed = 0.0
    var clawPos = 0.0
        set(value) {
            field = value
            clawer.position = field + Constants.clawOffset
        }

    val proximity get() = prox.voltage

    override fun tick() {
        // reset intake servos every 25 seconds so they don't suddenly stop every 30 seconds
        if (resetTimer.elapsed > 25.seconds) {
            intake.power = 0.0
            if (resetTimer.elapsed > 25.1.seconds) resetTimer.reset()
        } else {
            intake.power = intakeSpeed
        }
    }

    fun control(pos: Double? = null, speed: Double? = null) = routine(name = "IntakeControl$pos") {
        this@Intake.lock()
        ready()
        pos?.let { clawPos = it }
        speed?.let { intakeSpeed = it }
    }
}