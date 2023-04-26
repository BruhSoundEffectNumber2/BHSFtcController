package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.hardware.motors.Motor.GoBILDA

@Config
/**
 * The names for the robot's hardware.
 */
object Hardware {
    // Drivetrain
    @JvmField var DRIVE_FRONT_LEFT = "drive_front_left"
    @JvmField var DRIVE_FRONT_RIGHT = "drive_front_right"
    @JvmField var DRIVE_BACK_LEFT = "drive_back_left"
    @JvmField var DRIVE_BACK_RIGHT = "drive_back_right"
    @JvmField var DRIVE_MOTOR_TYPE = GoBILDA.RPM_312

    // Elevator
    @JvmField var MOTOR_ELEVATOR = "motor_elevator"

    // Grabber
    @JvmField var SERVO_GRABBER = "motor_elevator"
}