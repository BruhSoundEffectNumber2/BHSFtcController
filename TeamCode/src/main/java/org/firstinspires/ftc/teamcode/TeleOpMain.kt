package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.claw.ClawSubsystem
import org.firstinspires.ftc.teamcode.claw.ToggleClaw
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.elevator.ElevatorDown
import org.firstinspires.ftc.teamcode.elevator.ElevatorSubsystem
import org.firstinspires.ftc.teamcode.elevator.ElevatorUp
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
        val toggleClaw = ToggleClaw(robotTelemetry, clawSubsystem)

        val elevatorSubsystem = ElevatorSubsystem(
            hardwareMap, robotTelemetry
        ) {
            inputToPower(
                controls2.rightY,
                Elevator.INPUT_CURVE,
                Elevator.INPUT_DEADZONE,
                Elevator.INPUT_SCALE
            )
        }

        val elevatorUp = ElevatorUp(robotTelemetry, elevatorSubsystem)
        val elevatorDown = ElevatorDown(robotTelemetry, elevatorSubsystem)

        controls2.getGamepadButton(GamepadKeys.Button.X)
            .whenPressed(toggleClaw)

        controls2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
            .whenPressed(elevatorUp)

        controls2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
            .whenPressed(elevatorDown)

        schedule(driveManually)
    }

    override fun run() {
        super.run()

        // We want to have one telemetry update at the end of the loop
        robotTelemetry.update()
    }
}