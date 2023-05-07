package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.SimpleCommand

/**
 * Moves the elevator up to the next preset.
 * 
 * @param finishInstant Whether the command should finish instantly. This is useful for
 * TeleOp, or if you want to wait for the elevator to reach the preset. If this is false,
 * then the command cannot be interrupted by itself until the preset is reached.
 */
class ElevatorUp(
    telemetry: MultipleTelemetry,
    system: ElevatorSubsystem,
    private val finishInstant: Boolean = true
) : SimpleCommand<ElevatorSubsystem>(telemetry, system) {
    override fun initialize() {
        system.movePresetUp()
    }

    override fun isFinished(): Boolean {
        return finishInstant || system.atSetPoint
    }
}