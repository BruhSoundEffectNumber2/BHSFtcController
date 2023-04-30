package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.SelectCommand
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.move.DriveForTime
import org.firstinspires.ftc.teamcode.move.MoveSubsystem
import org.firstinspires.ftc.teamcode.signal.ReadSignalState
import org.firstinspires.ftc.teamcode.signal.SignalState

@Autonomous(name = "Simple Auto")
class AutoSimple : CommandOpMode() {
    private val stores = mutableMapOf<String, Any>()
    
    // TODO: This is a copy of TeleOpMain. It should be refactored
    private val dashboard: FtcDashboard = FtcDashboard.getInstance()
    private val robotTelemetry = MultipleTelemetry(telemetry, dashboard.telemetry)

    private val moveSubsystem = MoveSubsystem(hardwareMap, robotTelemetry)
    
    private val readSignalState = ReadSignalState(hardwareMap) {
        stores["signal_state"] = it
    }

    override fun initialize() {
        schedule(
            SequentialCommandGroup(
                readSignalState,
                DriveForTime(robotTelemetry, moveSubsystem, 1500, 0.5),
                WaitCommand(500),
                SelectCommand(
                    mapOf(
                        SignalState.Left to DriveForTime(robotTelemetry, moveSubsystem, 1500, strafe = -0.5),
                        SignalState.Right to DriveForTime(robotTelemetry, moveSubsystem, 1500, strafe = 0.5),
                    )
                ) { stores["signal_state"] as SignalState }
            )
        )
    }
}