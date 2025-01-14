package org.firstinspires.ftc.teamcode;

@com.acmerobotics.dashboard.config.Config
public class Config {
    // Drivetrain
    public static volatile String DRIVE_FRONT_LEFT = "drive_front_left";
    public static volatile String DRIVE_FRONT_RIGHT = "drive_front_right";
    public static volatile String DRIVE_BACK_LEFT = "drive_back_left";
    public static volatile String DRIVE_BACK_RIGHT = "drive_back_right";

    public static volatile double DRIVE_CURVE = 1.5;
    public static volatile double DRIVE_DEAD_ZONE = 0.05;
    public static volatile double DRIVE_STRAFE_SENSITIVITY = 1;
    public static volatile double DRIVE_FORWARD_SENSITIVITY = 1;
    public static volatile double DRIVE_TURN_SENSITIVITY = 1;

    // Elevator
    public static volatile String MOTOR_ELEVATOR = "motor_elevator";

    // Claw
    public static volatile String SERVO_CLAW = "servo_claw";
    public static volatile double CLAW_CLOSE_ANGLE = -20;
    public static volatile double CLAW_OPEN_ANGLE = 50;
}
