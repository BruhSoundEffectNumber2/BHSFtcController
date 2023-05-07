package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config

@Config
object Claw {
    @JvmField var MIN_ANGLE = -150.0
    @JvmField var MAX_ANGLE = 150.0

    @JvmField var OPEN_ANGLE = 5.0
    @JvmField var CLOSE_ANGLE = -15.0
}