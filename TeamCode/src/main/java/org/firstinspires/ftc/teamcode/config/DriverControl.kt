package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
/**
 * Parameters for driver control.
 */
object DriverControl {
    // Dead zones
    @JvmField var FORWARD_DEADZONE = 0.0
    @JvmField var STRAFE_DEADZONE = 0.0
    @JvmField var TURN_DEADZONE = 0.0

    // Power curves
    @JvmField var FORWARD_CURVE = 1.0
    @JvmField var STRAFE_CURVE = 1.0
    @JvmField var TURN_CURVE = 1.0

    // Sensitivity to input
    @JvmField var FORWARD_SCALE = 1.0
    @JvmField var STRAFE_SCALE = 1.0
    @JvmField var TURN_SCALE = 1.0
}