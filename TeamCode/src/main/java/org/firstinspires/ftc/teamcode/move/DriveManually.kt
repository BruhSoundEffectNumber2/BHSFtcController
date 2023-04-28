package org.firstinspires.ftc.teamcode.move

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import org.firstinspires.ftc.teamcode.SimpleCommand
import org.firstinspires.ftc.teamcode.config.DriverControl
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

/**
 * Command that allows the driver to control the robot manually.
 */
class DriveManually(
    telemetry: MultipleTelemetry, system: MoveSubsystem, private val controls: GamepadEx
) : SimpleCommand<MoveSubsystem>(telemetry, system) {
    override fun initialize() {
        telemetry.addLine("DriveManually initialized")
        telemetry.update()
    }

    override fun execute() {
        system.forward = inputToPower(
            controls.leftY,
            DriverControl.FORWARD_CURVE,
            DriverControl.FORWARD_DEADZONE,
            DriverControl.FORWARD_SCALE
        )

        // Strafe is the difference between the left and right triggers
        // The comparison of the two trigger gives that difference
        val strafe = controls.getButton(GamepadKeys.Button.RIGHT_BUMPER)
            .compareTo(controls.getButton(GamepadKeys.Button.LEFT_BUMPER))

        system.strafe = inputToPower(
            strafe.toDouble(),
            DriverControl.STRAFE_CURVE,
            DriverControl.STRAFE_DEADZONE,
            DriverControl.STRAFE_SCALE
        )

        system.turn = inputToPower(
            controls.leftX,
            DriverControl.TURN_CURVE,
            DriverControl.TURN_DEADZONE,
            DriverControl.TURN_SCALE
        )
    }

    override fun end(interrupted: Boolean) {
        system.stop()
    }

    /**
     * Applies a deadzone, curve, and scale to the given input.
     * @return The power to apply to the motor.
     */
    private fun inputToPower(
        input: Double, curve: Double, deadzone: Double, scale: Double
    ): Double {
        // We don't want the input to "jump" once it's out of the deadzone
        // So we subtract the deadzone from the absolute value of the input
        // then take the max of our result and 0
        var adjustedInput = max(abs(input) - deadzone, 0.0)

        // Apply the curve to the input
        adjustedInput.pow(curve)

        // Because we start curving later with a deadzone, we need to scale the result
        // to end up with the same maximum value as before at 1.0
        adjustedInput *= (1 - deadzone).pow(-curve)

        // Apply the real scale to the input
        adjustedInput *= scale

        // Finally, apply the sign of the input since we lost it when we took the absolute value
        adjustedInput *= input.sign

        return adjustedInput
    }
}