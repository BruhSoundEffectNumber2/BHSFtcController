package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
object Elevator {
    /**
     * How close does the elevator need to be to it's target position (cm).
     */
    @JvmField var TOLERANCE = 0.25

    /**
     * The number of ticks in the motor that correspond to 1cm of linear extension.
     */
    @JvmField var TICKS_TO_HEIGHT = 68.460793464

    /**
     * How high the elevator is off the floor (cm).
     */
    @JvmField var HEIGHT_OFFSET = 0.0

    /**
     * The minimum height for the elevator (cm).
     */
    @JvmField var MIN_HEIGHT = 0.0

    /**
     * The maximum height for the elevator (cm).
     */
    @JvmField var MAX_HEIGHT = 72.0

    /**
     * The preset heights for the elevator (cm) above the ground.
     */
    // FIXME: Fix these to be consistent with the actual junction heights and the elevator height
    @JvmField var PRESET_HEIGHTS = mutableListOf(0.0, 10.0, 36.0, 70.0)

    @JvmField var INPUT_DEADZONE = 0.1
    @JvmField var INPUT_CURVE = 1.5
    @JvmField var INPUT_SCALE = 1.0
    @JvmField var INPUT_VELOCITY = 10.0
    @JvmField var MANUAL_THRESHOLD = 0.05

    @JvmField var AUTO_KP = 0.4
    @JvmField var AUTO_KI = 0.0
    @JvmField var AUTO_KD = 0.0
    @JvmField var AUTO_KF = 0.0
    
    @JvmField var AUTO_I_MAX = 1.0
}