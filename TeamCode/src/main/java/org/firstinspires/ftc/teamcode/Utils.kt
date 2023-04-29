package org.firstinspires.ftc.teamcode

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sign

/**
 * Applies a deadzone, curve, and scale to the given input.
 * @return The power to apply to the motor.
 */
fun inputToPower(
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