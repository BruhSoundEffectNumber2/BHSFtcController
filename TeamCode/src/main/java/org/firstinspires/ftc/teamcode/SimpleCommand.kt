package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.command.Subsystem

/**
 * A simple command that controls a single subsystem.
 * Telemetry and subsystem registration is automatically provided.
 */
abstract class SimpleCommand<T : Subsystem>(
    /**
     * The combined telemetry that this subsystem uses.
     */
    protected val telemetry: MultipleTelemetry,

    /**
     * The subsystem that this command controls.
     */
    protected val system: T
) : CommandBase() {
    init {
        addRequirements(system)
    }
}