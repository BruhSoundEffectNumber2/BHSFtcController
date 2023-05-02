package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.controller.PIDFController
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.arcrobotics.ftclib.util.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.config.Hardware
import kotlin.math.abs

class ElevatorSubsystem(
    hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<MotorEx>(hardwareMap, telemetry) {
    enum class ElevatorMode {
        Stopped, Manual, Auto
    }

    var mode = ElevatorMode.Auto
        private set

    /**
     * The current height of the elevator (mm).
     *
     * Does not account for the height offset.
     */
    val currentHeight: Double
        get() = component.currentPosition / Elevator.TICKS_TO_HEIGHT

    val atTargetHeight: Boolean
        get() = component.atTargetPosition()

    private val autoController =
        PIDFController(Elevator.AUTO_KP, Elevator.AUTO_KI, Elevator.AUTO_KD, Elevator.AUTO_KF)

    /**
     * How much current the motor is drawing (mA).
     */
    private val powerDraw: Double
        get() = component.motorEx.getCurrent(CurrentUnit.MILLIAMPS)

    /** The previous power draw of the motor (mA). */
    private val previousPowerDraw = powerDraw

    /** When the previous power draw was recorded (ms). */
    // Offset by 10ms in the past to avoid divide by zero errors
    private val previousPowerDrawTime = System.currentTimeMillis() - 10

    /**
     * The current input from manual control.
     */
    private var manualInput = 0.0

    override fun createComponent(): MotorEx {
        val motor = MotorEx(
            hardwareMap, Hardware.ELEVATOR, Hardware.ELEVATOR_TYPE
        )
        
        // Setup the controllers
        autoController.setTolerance(Elevator.TOLERANCE)

        // The motor needs to be inverted because of the way it is mounted
        motor.inverted = true

        motor.resetEncoder()

        // Set the motor to keep a constant velocity
        motor.setRunMode(Motor.RunMode.VelocityControl)
        // Set the motor to brake when stopped, which is our starting state
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        motor.set(0.0)

        return motor
    }

    override fun periodic() {
        autoController.setPoint = 700.0
        when (mode) {
            ElevatorMode.Auto -> autoPeriodic()
            ElevatorMode.Manual -> manualPeriodic()
            else -> {}
        }

        telemetry.addData("Elevator Mode", mode)
        telemetry.addData("Elevator Height", currentHeight)
        telemetry.addData("Elevator Target Height", autoController.setPoint / Elevator.TICKS_TO_HEIGHT)
        telemetry.addData("Elevator Encoder Ticks", component.currentPosition)
        telemetry.addData("Elevator Power Draw", powerDraw)
        telemetry.addData(
            "Elevator Power Draw Derivative (mA/ms)",
            (powerDraw - previousPowerDraw) / (System.currentTimeMillis() - previousPowerDrawTime)
        )
    }

    private fun autoPeriodic() {
        while (autoController.atSetPoint().not()) {
            val output = autoController.calculate(currentHeight * Elevator.TICKS_TO_HEIGHT)
            component.velocity = output
        }
        
        // Stop the motor once we reach the set point
        component.stopMotor()
    }

    private fun manualPeriodic() {
        // If we are too close to the range of the elevator, stop the motor
        // We will include a small tolerance to give the motor some time to slow down
        if (currentHeight < Elevator.MIN_HEIGHT + Elevator.TOLERANCE && manualInput < 0) {
            component.stopMotor()
            return
        } else if (currentHeight > Elevator.MAX_HEIGHT - Elevator.TOLERANCE && manualInput > 0) {
            component.stopMotor()
            return
        }
        
        // Invert the input because of the way the motor is mounted
        component.velocity = -manualInput * Elevator.MANUAL_SPEED
    }

    /**
     * Stops the elevator.
     */
    fun stop() {
        mode = ElevatorMode.Stopped
        component.stopMotor()
    }

    /**
     * Moves the elevator to the given height.
     * This will set the [mode] to [ElevatorMode.Auto]
     *
     * @param height The height (mm) that the elevator will move to.
     * @param fromFloor Whether the height is from the floor.
     */
    fun moveToHeight(height: Double, fromFloor: Boolean = true) {
        mode = ElevatorMode.Auto

        // Calculate the final height we are traveling to
        val finalHeight = (height - if (fromFloor) Elevator.HEIGHT_OFFSET else 0.0).coerceIn(
            Elevator.MIN_HEIGHT..Elevator.MAX_HEIGHT
        )

        autoController.setPoint = finalHeight * Elevator.TICKS_TO_HEIGHT
    }

    /**
     * Moves the elevator at the given input.
     * This will set the [mode] to [ElevatorMode.Manual]
     *
     * The elevator will still prevent itself from moving outside of its range.
     *
     * @param input The raw input to move the elevator at (-1..1).
     */
    fun moveManually(input: Double) {
        mode = ElevatorMode.Manual
        manualInput = input
    }

    /**
     * Moves the elevator to the given preset.
     * This will set the [mode] to [ElevatorMode.Auto]
     *
     * @param preset The preset to move to. Will be clamped to the range of the elevator.
     */
    fun moveToPreset(preset: Int) {
        // Clamp the preset to the range of the elevator
        preset.coerceIn(0 until Elevator.PRESET_HEIGHTS.size).apply {
            moveToHeight(Elevator.PRESET_HEIGHTS[this])
        }
    }

    /**
     * Finds the nearest preset to the current height.
     * The direction will force the preset to be in a certain direction compared
     * to the current height. If the there is no preset in the given direction, the
     * nearest preset will be returned regardless of direction.
     *
     * @param direction The direction that the nearest preset must be in (Up or Down).
     * If null, the nearest preset will be returned regardless of direction.
     *
     * @return The index of the preset.
     *
     * @throws IllegalArgumentException If the direction is not null, Up, or Down.
     * @throws IllegalStateException If there are no presets.
     */
    fun findNearestPreset(direction: Direction?): Int {
        if (Elevator.PRESET_HEIGHTS.isEmpty()) {
            throw IllegalStateException("There are no presets")
        }

        if (direction == null) {
            return Elevator.PRESET_HEIGHTS.indices.minBy { i ->
                abs(Elevator.PRESET_HEIGHTS[i] - currentHeight)
            }
        }

        when (direction) {
            Direction.UP -> {
                // Filter out all presets that are below the current height
                // Then find the preset that has the highest height
                // If there are no presets above the current height, return the nearest preset
                return Elevator.PRESET_HEIGHTS.indices.filter { i ->
                    Elevator.PRESET_HEIGHTS[i] > currentHeight
                }.maxByOrNull { Elevator.PRESET_HEIGHTS[it] } ?: findNearestPreset(null)
            }
            Direction.DOWN -> {
                // Filter out all presets that are above the current height
                // Then find the preset that has the lowest height
                // If there are no presets below the current height, return the nearest preset
                return Elevator.PRESET_HEIGHTS.indices.filter { i ->
                    Elevator.PRESET_HEIGHTS[i] < currentHeight
                }.minByOrNull { Elevator.PRESET_HEIGHTS[it] } ?: findNearestPreset(null)
            }
            else -> throw IllegalArgumentException("Direction must be null, Up, or Down")
        }
    }
}