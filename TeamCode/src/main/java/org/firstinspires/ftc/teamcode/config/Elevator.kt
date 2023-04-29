package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
/**
 * Parameters for the elevator.
 */
object Elevator {
    /**
     * The coefficient for the elevator's P controller.
     */
    @JvmField var POS_COEFFICIENT = 1.0

    /**
     * How close does the elevator need to be to it's target position (ticks).
     */
    @JvmField var POS_TOLERANCE = 6.0

    /**
     * The number of ticks in the motor that correspond to 1mm of linear extension.
     */
    @JvmField var TICKS_TO_HEIGHT = 4.80089285714

    /**
     * How high the elevator is off the ground (mm)
     */
    @JvmField var HEIGHT_OFFSET = 60.0

    /**
     * The minimum height for the elevator (mm).
     */
    @JvmField var MIN_HEIGHT = 0.0

    /**
     * The maximum height for the elevator (mm).
     */
    @JvmField var MAX_HEIGHT = 1500.0

    @JvmField var TEST_MOTOR_SET = 1.0

    @JvmField var ELEVATOR_DEADZONE = 0.1
    @JvmField var ELEVATOR_CURVE = 1.5
    @JvmField var ELEVATOR_SCALE = 1.0

    @JvmField var ELEVATOR_PRESET_HEIGHTS = mutableListOf(0.0, 0.0, 0.0, 0.0)
}