package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
/**
 * The parameters for the grabber.
 */
object Grabber {
    @JvmField var MIN_ANGLE = -150.0
    @JvmField var MAX_ANGLE = 150.0

    @JvmField var OPEN_ANGLE = 50.0
    @JvmField var CLOSE_ANGLE = -20.0
}