package org.firstinspires.ftc.teamcode.subsystem

import org.fishnpotatoes.routine.Subsystem
import org.fishnpotatoes.routine.ftc.drivers.GoBildaPinpoint
import org.fishnpotatoes.routine.ftc.extensions.HardwareMapEx
import org.fishnpotatoes.routine.util.geometry.Pose2
import org.fishnpotatoes.routine.util.geometry.inches
import org.fishnpotatoes.routine.util.geometry.mm
import org.fishnpotatoes.routine.util.geometry.radians

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