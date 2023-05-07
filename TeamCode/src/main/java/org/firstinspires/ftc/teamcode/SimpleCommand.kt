package org.firstinspires.ftc.teamcode

import android.util.Log
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.command.Subsystem

/**
 * Just like the [SimpleSubsystem], most commands are fairly simple and only use one subsystem.
 * You also still want access to the telemetry, and you want that to be consistent across all commands.
 *
 * The subsystem is automatically added as a requirement on construction.
 *
 * @param T The type of [SimpleSubsystem] of the subsystem that this command controls.
 * @param telemetry The telemetry that this command will use.
 * @param system An instance of the [SimpleSubsystem] defined by [T].
 */
abstract class SimpleCommand<T : Subsystem>(
    /**
     * The telemetry that this command will use.
     */
    protected val telemetry: MultipleTelemetry,

    /**
     * The subsystem that this command controls.
     */
    protected val system: T
) : CommandBase() {
    init {
        addRequirements(system)
        Log.d("SimpleCommand", "Initialized command ${this.javaClass.simpleName}")
    }
}