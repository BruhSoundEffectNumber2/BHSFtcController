package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.SimpleCommand
import org.firstinspires.ftc.teamcode.elevator.ElevatorSubsystem

/**
 * Moves the elevator down to the next preset.
 *
 * The command is finished when the elevator reaches the preset.
 * 
 * @param finishInstant Whether the command should finish instantly. This is useful for
 * TeleOp, or if you want to wait for the elevator to reach the preset. If this is false,
 * then the command cannot be interrupted by itself until the preset is reached.
 */
class ElevatorDown(
    telemetry: MultipleTelemetry,
    system: ElevatorSubsystem,
    private val finishInstant: Boolean = true
) : SimpleCommand<ElevatorSubsystem>(telemetry, system) {
    override fun initialize() {
        system.movePresetDown()
    }

    override fun isFinished(): Boolean {
        return finishInstant || system.atSetPoint
    }
}