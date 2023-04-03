package org.firstinspires.ftc.teamcode.systems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;

public class ClawSystem extends SubsystemBase {
    SimpleServo servo;
    boolean open;

    Telemetry tel;

    public ClawSystem(final HardwareMap hardwareMap, Telemetry tel) {
        servo = new SimpleServo(
                hardwareMap,
                Config.SERVO_CLAW,
                -150,
                150);

        this.tel = tel;

        open();
    }

    @Override
    public void periodic() {
        tel.addData("claw", servo.getAngle());
        tel.addData("open", open);
        tel.update();
    }

    public void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        open = true;
        servo.turnToAngle(Config.CLAW_OPEN_ANGLE);
    }

    public void close() {
        open = false;
        servo.turnToAngle(Config.CLAW_CLOSE_ANGLE);
    }
}
