package org.firstinspires.ftc.teamcode.config

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.hardware.motors.Motor.GoBILDA

@Config
object Hardware {
    // Drivetrain
    @JvmField var FRONT_LEFT = "drive_front_left"
    @JvmField var FRONT_RIGHT = "drive_front_right"
    @JvmField var BACK_LEFT = "drive_back_left"
    @JvmField var BACK_RIGHT = "drive_back_right"
    @JvmField var DRIVE_TYPE = GoBILDA.RPM_312

    // Elevator
    @JvmField var ELEVATOR = "motor_elevator"
    @JvmField var ELEVATOR_TYPE = GoBILDA.RPM_312

    // Grabber
    @JvmField var CLAW = "servo_claw"
    
    // Camera
    @JvmField var CAMERA = "Webcam 1"
}