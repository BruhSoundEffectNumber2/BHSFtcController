package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.elevator.Elevate
import org.firstinspires.ftc.teamcode.elevator.ElevatorSubsystem
import org.firstinspires.ftc.teamcode.grab.Grab
import org.firstinspires.ftc.teamcode.grab.GrabSubsystem
import org.firstinspires.ftc.teamcode.move.DriveManually
import org.firstinspires.ftc.teamcode.move.MoveSubsystem

@TeleOp(name = "Main TeleOp")
class TeleOpMain : CommandOpMode() {
    private lateinit var robotTelemetry: MultipleTelemetry

    override fun initialize() {
        robotTelemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val moveSubsystem = MoveSubsystem(hardwareMap, robotTelemetry)
        val driveManually = DriveManually(robotTelemetry, moveSubsystem, GamepadEx(gamepad1))

        val grabSubsystem = GrabSubsystem(hardwareMap, robotTelemetry)
        val grab = Grab(robotTelemetry, grabSubsystem, GamepadEx(gamepad2))

        val elevatorSubsystem = ElevatorSubsystem(hardwareMap, robotTelemetry)
        val elevate = Elevate(robotTelemetry, elevatorSubsystem, GamepadEx(gamepad2))

        schedule(driveManually, grab, elevate)
    }

    override fun run() {
        super.run()
        
        // We want to have one telemetry update at the end of the loop
        telemetry.update()
    }
}