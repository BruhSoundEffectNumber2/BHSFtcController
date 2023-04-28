package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.grab.Grab
import org.firstinspires.ftc.teamcode.grab.GrabSubsystem
import org.firstinspires.ftc.teamcode.move.DriveManually
import org.firstinspires.ftc.teamcode.move.MoveSubsystem

@TeleOp(name = "Main TeleOp")
class TeleOpMain : CommandOpMode() {
    private val dashboard: FtcDashboard = FtcDashboard.getInstance()
    private val robotTelemetry = MultipleTelemetry(telemetry, dashboard.telemetry)

    private val moveSubsystem = MoveSubsystem(hardwareMap, robotTelemetry)
    private val driveManually = DriveManually(robotTelemetry, moveSubsystem, GamepadEx(gamepad1))

    private val grabSubsystem = GrabSubsystem(hardwareMap, robotTelemetry)
    private val grab = Grab(robotTelemetry, grabSubsystem, GamepadEx(gamepad2))

    override fun initialize() {
        schedule(driveManually)
        schedule(grab)
    }
}