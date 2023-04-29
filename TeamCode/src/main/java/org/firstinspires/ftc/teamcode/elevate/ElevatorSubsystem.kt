package org.firstinspires.ftc.teamcode.elevate

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * System that controls the elevator.
 */
class ElevatorSubsystem(
    hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<MotorEx>(hardwareMap, telemetry) {
    enum class ElevatorMode {
        Free, Controlled
    }

    var stopped = true
        set(value) {
            field = value
            if (value) {
                component.stopMotor()
            }
        }

    var mode = ElevatorMode.Controlled
        private set

    /**
     * The current height of the elevator.
     *
     * Does not account for the height offset.
     */
    val currentPos: Double
        get() = component.currentPosition / Elevator.TICKS_TO_HEIGHT

    /**
     * The current the elevator's motor is drawing. (mA)
     */
    private val currentDraw: Double
        get() = component.motorEx.getCurrent(CurrentUnit.MILLIAMPS)

    private val previousDraw = currentDraw
    private val previousDrawTime = System.currentTimeMillis()

    private var manualInput = 0.0

    override fun createComponent(): MotorEx {
        val motor = MotorEx(
            hardwareMap, Hardware.MOTOR_ELEVATOR, Hardware.ELEVATOR_MOTOR_TYPE
        )

        motor.setRunMode(Motor.RunMode.VelocityControl)
        motor.setPositionTolerance(Elevator.POS_TOLERANCE)
        motor.positionCoefficient = Elevator.POS_COEFFICIENT

        motor.set(0.0)

        return motor
    }

    override fun periodic() {
        /*
        If the motor is stopped, set the motor to 0. If the motor is controlled, set the motor to
        our test value. Otherwise, set the motor to the manual input.
         */
        component.set(
            if (stopped) 0.0 else when (mode) {
                ElevatorMode.Controlled -> Elevator.TEST_MOTOR_SET
                ElevatorMode.Free -> manualInput
            }
        )

        telemetry.addData("Elevator Mode", if (stopped) "Stopped" else mode)
        telemetry.addData("Elevator Position", currentPos)
        telemetry.addData("Elevator Current Draw", currentDraw)
        telemetry.addData(
            "Elevator Current Draw Derivative",
            (currentDraw - previousDraw) / (System.currentTimeMillis() - previousDrawTime)
        )
        telemetry.update()
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
        val trueHeight = (height - if (useBase) Elevator.HEIGHT_OFFSET else 0.0).coerceIn(
            Elevator.MIN_HEIGHT..Elevator.MAX_HEIGHT
        )

        component.setTargetPosition((trueHeight * Elevator.TICKS_TO_HEIGHT).toInt())
    }

    /**
     * Moves the elevator at the given input.
     * This will set the [mode] to [ElevatorMode.Free]
     *
     * @param input The input to move the elevator at. Ranges from [-1, 1].
     */
    fun move(input: Double) {
        mode = ElevatorMode.Free

        // If we are too close to the range of the elevator, stop the motor
        // We will include a small tolerance to give the motor some time to slow down
        if (currentPos < Elevator.MIN_HEIGHT + Elevator.POS_TOLERANCE && input < 0) {
            component.set(0.0)
            return
        } else if (currentPos > Elevator.MAX_HEIGHT - Elevator.POS_TOLERANCE && input > 0) {
            component.set(0.0)
            return
        }

        manualInput = input
    }
}