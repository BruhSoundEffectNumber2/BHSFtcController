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

    var stopped = true

    var mode = ElevatorMode.Controlled
        private set

    val currentPos: Double
        get() = component.currentPosition / Elevator.TICKS_TO_HEIGHT

    override fun createComponent(): Motor {
        val motor = Motor(
            hardwareMap,
            Hardware.MOTOR_ELEVATOR,
            Hardware.ELEVATOR_MOTOR_TYPE
        )

        motor.setRunMode(Motor.RunMode.VelocityControl)
        motor.setPositionTolerance(Elevator.POS_TOLERANCE)

        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        motor.set(0.0)

        return motor
    }

    override fun periodic() {
        // If the motor is stopped, set speed to 0, otherwise set it to our test value
        component.set((if (stopped) 0 else Elevator.TEST_MOTOR_SET) as Double)
    }

    /**
     * Moves the elevator to the given height.
     * This will set the [mode] to [ElevatorMode.Controlled]
     *
     * @param height The height (mm) that the elevator will move to.
     * @param useBase Whether to consider the elevators existing height off the ground.
     */
    fun moveToPos(height: Double, useBase: Boolean = true) {
        mode = ElevatorMode.Controlled

        // Calculate the final height we are traveling to
        val trueHeight =
            (height - (if (useBase) Elevator.HEIGHT_OFFSET else 0) as Double).coerceIn(
                Elevator.MIN_HEIGHT,
                Elevator.MAX_HEIGHT
            )

        component.setTargetPosition((trueHeight * Elevator.TICKS_TO_HEIGHT).toInt())
    }
}