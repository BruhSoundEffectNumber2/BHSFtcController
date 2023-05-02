package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.elevator.ElevateDown
import org.firstinspires.ftc.teamcode.elevator.ElevateUp
import org.firstinspires.ftc.teamcode.elevator.ElevatorSubsystem
import org.firstinspires.ftc.teamcode.claw.ClawSubsystem
import org.firstinspires.ftc.teamcode.move.DriveManually
import org.firstinspires.ftc.teamcode.move.MoveSubsystem

@TeleOp(name = "Main TeleOp")
class TeleOpMain : CommandOpMode() {
    private lateinit var robotTelemetry: MultipleTelemetry

    override fun initialize() {
        val controls1 = GamepadEx(gamepad1)
        val controls2 = GamepadEx(gamepad2)

        robotTelemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val moveSubsystem = MoveSubsystem(hardwareMap, robotTelemetry)
        val driveManually = DriveManually(robotTelemetry, moveSubsystem, controls1)

        val clawSubsystem = ClawSubsystem(hardwareMap, robotTelemetry)

        val elevatorSubsystem = ElevatorSubsystem(hardwareMap, robotTelemetry)

        schedule(driveManually)

        controls2.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
            ElevateUp(
                robotTelemetry, elevatorSubsystem
            )
        )

        controls2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
            ElevateDown(
                robotTelemetry, elevatorSubsystem
            )
        )
    }

    override fun run() {
        super.run()

        // We want to have one telemetry update at the end of the loop
        telemetry.update()
    }
}