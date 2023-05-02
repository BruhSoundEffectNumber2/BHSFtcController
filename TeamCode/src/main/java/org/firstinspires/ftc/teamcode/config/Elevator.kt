package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
object Elevator {
    /**
     * How close does the elevator need to be to it's target position (ticks).
     */
    @JvmField var TOLERANCE = 6.0

    /**
     * The number of ticks in the motor that correspond to 1mm of linear extension.
     */
    @JvmField var TICKS_TO_HEIGHT = 4.80089285714

    /**
     * How high the elevator is off the floor (mm).
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

    @JvmField var INPUT_DEADZONE = 0.1
    @JvmField var INPUT_CURVE = 1.5
    @JvmField var INPUT_SCALE = 1.0

    @JvmField var PRESET_HEIGHTS = listOf(0.0, 100.0, 200.0, 300.0)

    /**
     * The maximum speed of the elevator when manually controlled (ticks per second).
     */
    @JvmField var MANUAL_SPEED = 1000.0

    @JvmField var AUTO_KP = 0.0
    @JvmField var AUTO_KI = 0.0
    @JvmField var AUTO_KD = 0.0
    @JvmField var AUTO_KF = 0.0
}