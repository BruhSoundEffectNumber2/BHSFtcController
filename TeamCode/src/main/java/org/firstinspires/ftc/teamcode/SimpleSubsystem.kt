package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * A simple subsystem that provides access to the hardware map and telemetry.
 * It's designed for systems that control a single component, such as a motor or servo.
 *
 * @param T The type of the component that this subsystem controls.
 */
abstract class SimpleSubsystem<T>(
    /**
     * The hardware map that this subsystem references.
     */
    protected val hardwareMap: HardwareMap,

    /**
     * The combined telemetry that this subsystem uses.
     */
    protected val telemetry: MultipleTelemetry
) : SubsystemBase() {
    /**
     * The component that this subsystem controls.
     */
    protected val component by lazy { createComponent() }

    /**
     * Creates the component that this subsystem controls.
     */
    protected abstract fun createComponent(): T
}