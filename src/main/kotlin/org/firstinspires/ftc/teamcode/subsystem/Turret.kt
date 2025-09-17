package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.ServoImplEx
import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.routine
import dev.fishies.routine.util.geometry.Radians
import dev.fishies.routine.util.geometry.degrees
import org.firstinspires.ftc.teamcode.constants.IntakeConstants as Constants

class Turret(map: HardwareMapEx) : Subsystem() {
    private val turret by map.deferred<ServoImplEx>("turret")

    var position = Radians.ZERO
        set(value) {
            field = value.coerceIn(Constants.minAngle.degrees, Constants.maxAngle.degrees)
            turret.position = (field.degrees - Constants.minAngle) / (Constants.maxAngle - Constants.minAngle)
        }

    fun rotateTo(target: Radians) = routine(name = "TurretTo$target") {
        ready()
        position = target
    }

    override fun tick() = Unit
}