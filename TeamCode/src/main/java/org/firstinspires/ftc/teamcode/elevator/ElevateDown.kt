package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.util.Direction
import org.firstinspires.ftc.teamcode.SimpleCommand

/**
 * Moves the elevator down to the next preset.
 */
class ElevateDown(
    telemetry: MultipleTelemetry,
    system: ElevatorSubsystem
) : SimpleCommand<ElevatorSubsystem>(telemetry, system) {
    override fun initialize() {
        system.moveToPreset(system.findNearestPreset(Direction.DOWN))
    }

    override fun isFinished(): Boolean {
        return system.atTargetHeight
    }
}