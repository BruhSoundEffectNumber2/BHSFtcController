package org.firstinspires.ftc.teamcode.elevate

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.util.Direction
import org.firstinspires.ftc.teamcode.SimpleCommand
import org.firstinspires.ftc.teamcode.config.Elevator
import org.firstinspires.ftc.teamcode.inputToPower
import kotlin.math.abs

/**
 * Command to control the elevator.
 *
 * @param controls The gamepad to use for manual control. A null value means autonomous control.
 */
class Elevate(
    telemetry: MultipleTelemetry, system: ElevatorSubsystem, private val controls: GamepadEx?
) : SimpleCommand<ElevatorSubsystem>(telemetry, system) {
    /**
     * Which preselected height we are at.
     * -1 means we are not at a preselected height.
     */
    private var preset = -1

    override fun initialize() {
        // If we are in autonomous mode, we don't want to do anything here
        if (controls == null) return
        
        GamepadButton(controls, GamepadKeys.Button.DPAD_UP).whenPressed(this::increasePreset)
        GamepadButton(controls, GamepadKeys.Button.DPAD_DOWN).whenPressed(this::decreasePreset)
    }

    override fun execute() {
        // If we are in autonomous mode, we don't want to do anything here
        if (controls == null) return
        
        val manualInput = inputToPower(
            controls.rightY,
            Elevator.ELEVATOR_CURVE,
            Elevator.ELEVATOR_DEADZONE,
            Elevator.ELEVATOR_SCALE
        )

        // Manual control takes priority over automatic control
        // We only care about changing if we aren't in manual mode
        if (system.mode != ElevatorSubsystem.ElevatorMode.Free) {
            if (abs(manualInput) > 0.001) {
                system.move(manualInput)
            }
        } else {
            // If we are already in manual mode, just move
            system.move(manualInput)
        }
    }

    override fun end(interrupted: Boolean) {
        system.stopped = true
    }

    private fun increasePreset() {
        changePreset(Direction.UP)
    }

    private fun decreasePreset() {
        changePreset(Direction.DOWN)
    }

    private fun changePreset(direction: Direction) {
        if (preset == -1) {
            preset = closestPreset(direction)
        } else if (preset < Elevator.ELEVATOR_PRESET_HEIGHTS.lastIndex) {
            preset += if (direction == Direction.UP) 1 else -1
        }

        system.moveToPos(Elevator.ELEVATOR_PRESET_HEIGHTS[preset])
    }

    /**
     * Returns the closest preset to the current position in the given direction.
     *
     * @param direction The direction to search in. Up, Down, or null for closest.
     *
     * @return The index of the closest preset. -1 if there are no presets in the given direction.
     */
    private fun closestPreset(direction: Direction?): Int {
        val currentPos = system.currentPos

        /*
        We want to find the preset with the minimum difference between the preset height and the current height.
        We only want to consider presets in the given direction, if any.
         */

        when (direction) {
            null -> {
                // We only want the closest
                return Elevator.ELEVATOR_PRESET_HEIGHTS.withIndex().minByOrNull {
                    abs(it.value - currentPos)
                }!!.index
            }
            Direction.UP, Direction.DOWN -> {
                // We want the closest preset above or below the current position

                // Start with the closest preset
                var closest = closestPreset(null)

                if (direction == Direction.UP) {
                    if (closest == Elevator.ELEVATOR_PRESET_HEIGHTS.lastIndex) {
                        // We are already at the top
                        return closest
                    }

                    // Move up until we find a preset above the current position
                    while (Elevator.ELEVATOR_PRESET_HEIGHTS[closest] < currentPos) {
                        closest++
                    }
                } else {
                    if (closest == 0) {
                        // We are already at the bottom
                        return 0
                    }

                    // Move down until we find a preset below the current position
                    while (Elevator.ELEVATOR_PRESET_HEIGHTS[closest] > currentPos) {
                        closest--
                    }
                }

                return closest
            }
            else -> {
                // Invalid direction
                return -1
            }
        }
    }
}