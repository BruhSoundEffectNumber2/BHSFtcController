package org.firstinspires.ftc.teamcode

import android.util.Log
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.SubsystemBase
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * In a lot of cases, Most subsystems will control just one component, such as a motor or servo.
 * You'll also want to have access to the hardware map and telemetry, and you want that to be consistent
 * across all subsystems.
 *
 * This class provides a simple way to do that. It must be subclassed, and it provides access
 * to the hardwareMap and telemetry. It also provides a component that can be used to control
 * the subsystem's hardware. The component is created by the subclass and can be constructed
 * and configured in whatever way is needed.
 *
 * @param T The type of the component that this subsystem controls.
 * @param hardwareMap The hardware map that this subsystem uses to get the physical component.
 * @param telemetry The telemetry that this subsystem will use.
 */
abstract class SimpleSubsystem<T : Any>(
    /**
     * The hardware map that this subsystem uses to get the physical component.
     */
    protected val hardwareMap: HardwareMap,

    /**
     * The telemetry that this subsystem will use.
     */
    protected val telemetry: MultipleTelemetry
) : SubsystemBase() {
    /**
     * The component that this subsystem controls.
     */
    protected val component by lazy { createComponent() }

    /**
     * Creates the component that this subsystem controls. It's lazily initialized, so it won't always
     * be constructed quite at construction time.
     * 
     * The method should never need to be called manually.
     *
     * @return The created and configured component that this subsystem controls.
     */
    protected abstract fun createComponent(): T

    init {
        // Log that the subsystem was constructed
        // This also makes sure that the component is constructed early
        Log.d(
            "SimpleSubsystem",
            "Initialized subsystem ${this.javaClass.simpleName} with component ${component.javaClass.simpleName}"
        )
    }
}