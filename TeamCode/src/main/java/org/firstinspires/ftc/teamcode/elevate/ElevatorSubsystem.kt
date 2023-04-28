package org.firstinspires.ftc.teamcode.elevate

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * System that controls the elevator.
 */
class ElevatorSubsystem(
        hardwareMap: HardwareMap,
        telemetry: MultipleTelemetry
) : SimpleSubsystem<Motor>(hardwareMap, telemetry) {
    enum class ElevatorMode {
        Free,
        Controlled
    }

    var mode = ElevatorMode.Controlled
       private set

    val currentPos: Double
        get() = component.currentPosition / Elevator.TICKS_TO_HEIGHT

    override fun createComponent(): Motor {
        return Motor(
                hardwareMap,
                Hardware.MOTOR_ELEVATOR,
                Hardware.ELEVATOR_MOTOR_TYPE
        )
    }

    /**
     * Moves the elevator to the given height.
     * This will set the [mode] to [ElevatorMode.Controlled]
     *
     * @param height The height (mm) that the elevator will move to.
     * @param useBase Whether to consider the elevators existing height off the ground.
     */
    fun moveToPos(height: Double, useBase: Boolean = true) {
        val trueHeight = height
    }
}