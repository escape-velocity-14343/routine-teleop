package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import org.fishnpotatoes.routine.RoutineManager

@TeleOp(name = "Test TeleOp")
open class TestTeleOp : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        while (opModeIsActive()) {
            //RoutineManager.tick()
            //telemetry.addLine(RoutineManager.toString())
            telemetry.addLine("hi")
            telemetry.update()
        }
    }
}
