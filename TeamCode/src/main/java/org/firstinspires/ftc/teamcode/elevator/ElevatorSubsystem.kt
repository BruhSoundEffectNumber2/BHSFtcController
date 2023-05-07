package org.firstinspires.ftc.teamcode.elevator

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.controller.PIDFController
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.arcrobotics.ftclib.util.Direction
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.config.Hardware
import java.util.function.DoubleSupplier
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Subsystem that controls the elevator.
 *
 * The elevator is a [MotorEx] that can be moved using PIDF control.
 * The elevator will not go outside of the ranges set in [Elevator.MIN_HEIGHT] and [Elevator.MAX_HEIGHT].
 *
 * @param input The manual source of input for the elevator. Must be in the range -1 to 1.
 * If null, the elevator will always be in auto mode.
 */
class ElevatorSubsystem(
    hardwareMap: HardwareMap, telemetry: MultipleTelemetry, private val input: DoubleSupplier?
) : SimpleSubsystem<MotorEx>(hardwareMap, telemetry) {
    enum class ElevatorMode {
        MANUAL, AUTO
    }

    var mode = ElevatorMode.AUTO
        private set

    /**
     * The distance the elevator has traveled in cm.
     */
    val height: Double
        get() = component.currentPosition / Elevator.TICKS_TO_HEIGHT

    /**
     * The set point for the elevator in cm.
     */
    var setPoint: Double
        get() = pidController.setPoint
        private set(value) {
            pidController.setPoint = value
        }

    /**
     * Whether the elevator is at it's set point.
     */
    val atSetPoint: Boolean
        get() = pidController.atSetPoint()

    private var targetPreset = 0

    /**
     * The PIDF controller used to control the elevator in auto.
     */
    private lateinit var pidController: PIDFController

    override fun createComponent(): MotorEx {
        val motor = MotorEx(hardwareMap, Hardware.ELEVATOR, Hardware.ELEVATOR_TYPE)

        // Set the motor to brake when power is 0
        motor.setRunMode(Motor.RunMode.RawPower)
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

        // Create the PIDF controller
        pidController =
            PIDFController(Elevator.AUTO_KP, Elevator.AUTO_KI, Elevator.AUTO_KD, Elevator.AUTO_KF)
        pidController.setIntegrationBounds(-Elevator.AUTO_I_MAX, Elevator.AUTO_I_MAX)
        pidController.setTolerance(Elevator.TOLERANCE)

        // Reset the encoder
        motor.resetEncoder()

        return motor
    }

    // TODO: This currently runs at an median of 200hz, but dips to around <100hz 
    //  on a fairly regular basis. We should try to get this to run at a more consistent rate.
    //  We might try multithreading using Kotlin or even different languages in the background.
    override fun periodic() {
        // For testing purposes, set the controller properties to the values in the config
        pidController.p = Elevator.AUTO_KP
        pidController.i = Elevator.AUTO_KI
        pidController.d = Elevator.AUTO_KD
        pidController.f = Elevator.AUTO_KF
        pidController.setIntegrationBounds(-Elevator.AUTO_I_MAX, Elevator.AUTO_I_MAX)
        pidController.setTolerance(Elevator.TOLERANCE)

        // Check if we should switch to manual mode
        if (input != null && abs(input.asDouble) > Elevator.MANUAL_THRESHOLD) {
            // If in auto mode, reset the set point to the current height
            if (mode == ElevatorMode.AUTO) {
                setPoint = height
            }
            
            mode = ElevatorMode.MANUAL
        }

        // If in manual mode, adjust the set point based on the input
        if (mode == ElevatorMode.MANUAL) {
            setPoint =
                (setPoint - input!!.asDouble * Elevator.INPUT_VELOCITY * pidController.period).coerceIn(
                    Elevator.MIN_HEIGHT, Elevator.MAX_HEIGHT
                )
        }

        // Get the output from the PID controller if we are not already at the set point
        val pidOutput = pidController.calculate(height)
        val pidPower = if (atSetPoint) 0.0 else pidOutput

        component.set(pidPower)

        telemetry.addData("Elevator Height", height)
        telemetry.addData("Elevator Mode", mode)
        telemetry.addData("Elevator Set Point", pidController.setPoint)
        // Convert the period time to hz
        telemetry.addData("Elevator Rate", (1 / pidController.period).roundToInt())
        telemetry.addData("Elevator Power", pidPower)
    }

    /**
     * Finds the closest preset to the current elevator position in the given direction.
     * If the direction is null, it will find the closest preset in either direction.
     *
     * If there are no presets in the given direction, it will return the closest preset regardless of direction.
     *
     * @param direction The direction to search in. Can be UP, DOWN, or null.
     *
     * @return The name of the closest preset found.
     *
     * @throws IllegalArgumentException if the direction is not UP, DOWN, or null.
     * @throws IllegalStateException if there are no presets.
     */
    fun getClosestPreset(direction: Direction?): Int {
        if (Elevator.PRESET_HEIGHTS.isEmpty()) {
            throw IllegalStateException("There are no presets")
        }

        if (direction == null) {
            // Get the closest preset
            return Elevator.PRESET_HEIGHTS.indices.minBy { heightIndex ->
                abs(Elevator.PRESET_HEIGHTS[heightIndex] - height)
            }
        }

        // Get the closest preset in the given direction
        when (direction) {
            Direction.UP -> {
                // Get the closest preset above the current height, or the closest preset if there are none above
                return Elevator.PRESET_HEIGHTS.indices.filter { heightIndex ->
                    Elevator.PRESET_HEIGHTS[heightIndex] > height + Elevator.TOLERANCE
                }.minByOrNull { heightIndex ->
                    Elevator.PRESET_HEIGHTS[heightIndex] - height
                } ?: getClosestPreset(null)
            }
            Direction.DOWN -> {
                // Get the closest preset below the current height, or the closest preset if there are none below
                return Elevator.PRESET_HEIGHTS.indices.filter { heightIndex ->
                    Elevator.PRESET_HEIGHTS[heightIndex] < height - Elevator.TOLERANCE
                }.minByOrNull { heightIndex ->
                    height - Elevator.PRESET_HEIGHTS[heightIndex]
                } ?: getClosestPreset(null)
            }
            else -> {
                // An invalid direction was given
                throw IllegalArgumentException("Direction must be UP, DOWN, or null")
            }
        }
    }


    /**
     * Begins moving the elevator to the given height. This also sets the [mode] to [ElevatorMode.AUTO].
     *
     * @param height The height to move to in cm. The height will be coerced to be
     * within the range of [Elevator.MIN_HEIGHT] and [Elevator.MAX_HEIGHT].
     * @param fromGround Whether the height is from the ground or from the bottom of the elevator.
     */
    fun moveToHeight(height: Double, fromGround: Boolean = true) {
        // The elevator considers the bottom of the elevator to be height 0, so we may need to adjust
        val heightFromBase = if (fromGround) height - Elevator.MIN_HEIGHT else height

        // Coerce the height to be within the range of the elevator
        val finalHeight = heightFromBase.coerceIn(Elevator.MIN_HEIGHT..Elevator.MAX_HEIGHT)

        pidController.setPoint = finalHeight
        
        mode = ElevatorMode.AUTO
    }

    /**
     * Begins moving the elevator to the given preset.
     *
     * @param preset The name of the preset to move to.
     *
     * @throws IllegalArgumentException if the preset does not exist.
     */
    fun moveToPreset(preset: Int) {
        if (preset !in Elevator.PRESET_HEIGHTS.indices) {
            throw IllegalArgumentException("Preset $preset does not exist")
        }

        targetPreset = preset
        moveToHeight(Elevator.PRESET_HEIGHTS[preset])
    }

    fun movePresetUp() {
        if (mode == ElevatorMode.MANUAL) {
            targetPreset = getClosestPreset(Direction.UP)
            moveToPreset(targetPreset)
        } else if (targetPreset < Elevator.PRESET_HEIGHTS.size - 1) {
            targetPreset++
            moveToPreset(targetPreset)
        }
    }

    fun movePresetDown() {
        if (mode == ElevatorMode.MANUAL) {
            targetPreset = getClosestPreset(Direction.DOWN)
            moveToPreset(targetPreset)
        } else if (targetPreset > 0) {
            targetPreset--
            moveToPreset(targetPreset)
        }
    }
}