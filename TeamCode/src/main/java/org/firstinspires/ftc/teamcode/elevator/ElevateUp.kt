package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.util.Direction
import org.firstinspires.ftc.teamcode.SimpleCommand

/**
 * Moves the elevator up to the next preset.
 */
class ElevateUp(
    telemetry: MultipleTelemetry,
    system: ElevatorSubsystem
) : SimpleCommand<ElevatorSubsystem>(telemetry, system) {
    override fun initialize() {
        system.moveToPreset(system.findNearestPreset(Direction.UP))
    }

    override fun isFinished(): Boolean {
        return system.atTargetHeight
    }
}