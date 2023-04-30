package org.firstinspires.ftc.teamcode.move

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.SimpleCommand

/**
 * Command to move the robot for a certain amount of time with a certain input.
 * 
 * @param time The amount of time to move for in milliseconds.
 * @param forward The forward power to move with (-1..1).
 * @param strafe The strafe power to move with (-1..1).
 * @param turn The turn power to move with (-1..1).
 */
class DriveForTime(
    telemetry: MultipleTelemetry,
    system: MoveSubsystem,
    private val time: Long,
    private val forward: Double = 0.0,
    private val strafe: Double = 0.0,
    private val turn: Double = 0.0
) : SimpleCommand<MoveSubsystem>(telemetry, system) {
    private val startTime = System.currentTimeMillis()
    
    override fun initialize() {
        system.forward = forward
        system.strafe = strafe
        system.turn = turn
    }

    override fun end(interrupted: Boolean) {
        system.stop()
    }

    override fun isFinished(): Boolean {
        return System.currentTimeMillis() - startTime >= time
    }
} 