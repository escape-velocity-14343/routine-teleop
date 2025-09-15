package org.firstinspires.ftc.teamcode.subsystem

import dev.fishies.routine.Subsystem
import dev.fishies.routine.ftc.drivers.GoBildaPinpoint
import dev.fishies.routine.ftc.extensions.HardwareMapEx
import dev.fishies.routine.util.geometry.Pose2
import dev.fishies.routine.util.geometry.inches
import dev.fishies.routine.util.geometry.mm
import dev.fishies.routine.util.geometry.radians

class Pinpoint(map: HardwareMapEx) : Subsystem() {
    private val p by map.deferred<GoBildaPinpoint>("pinpoint") {
        initialize()
        recalibrateIMU()
        setEncoderResolution(GoBildaPinpoint.GoBildaOdometryPods.GOBILDA_4_BAR_POD)
        setEncoderDirections(
            GoBildaPinpoint.EncoderDirection.REVERSED, GoBildaPinpoint.EncoderDirection.FORWARD
        )
        xOffset = 65.mm
        yOffset = 45.mm
    }

    private var lastGoodPose = Pose2.ZERO
    private var lastPose: Pose2? = null
    val pose
        get() = lastPose ?: lastGoodPose
    val velocity
        get() = p.vel

    override fun tick() {
        p.update()
        if (p.pose.x.inches.isNaN() || p.pose.y.inches.isNaN() || p.pose.heading.radians.isNaN() || (p.pose.x == 0.0.inches && p.pose.y == 0.0.inches && p.pose.heading == 0.0.radians)) {
            lastPose = null
        } else {
            lastPose = p.pose
            lastGoodPose = lastPose!!
        }
    }

    fun resetYaw() {
        p.pose = Pose2(p.pos, 0.0.radians)
    }
}