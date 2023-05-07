package org.firstinspires.ftc.teamcode.claw

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.teamcode.SimpleCommand

/**
 * Toggles the [ClawSubsystem] between open and closed.
 * 
 * The command is finished immediately.
 */
class ToggleClaw(
    telemetry: MultipleTelemetry,
    system: ClawSubsystem
) : SimpleCommand<ClawSubsystem>(telemetry, system) {
    override fun initialize() {
        system.toggle()
    }

    override fun isFinished(): Boolean {
        return true
    }
}