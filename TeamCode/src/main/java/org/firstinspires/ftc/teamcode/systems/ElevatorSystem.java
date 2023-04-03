package org.firstinspires.ftc.teamcode.systems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config;

public class ElevatorSystem extends SubsystemBase {
    Motor motor;

    double velocity = 0;

    public ElevatorSystem(final HardwareMap hardwareMap) {
        motor = new Motor(hardwareMap, Config.MOTOR_ELEVATOR);
        motor.setRunMode(Motor.RunMode.VelocityControl);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void periodic() {
        motor.set(-velocity);
    }

    public void setVelocity(double vel) {
        velocity = vel;
    }

    public void stop() {
        velocity = 0;
        motor.stopMotor();
    }
}
