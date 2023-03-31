package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ControlClaw;
import org.firstinspires.ftc.teamcode.commands.ControlElevator;
import org.firstinspires.ftc.teamcode.commands.DriveTele;
import org.firstinspires.ftc.teamcode.systems.ClawSystem;
import org.firstinspires.ftc.teamcode.systems.DriveSystem;
import org.firstinspires.ftc.teamcode.systems.ElevatorSystem;

@TeleOp(name = "Main TeleOp")
public class TeleOpMain extends CommandOpMode {
    @Override
    public void initialize() {
        DriveSystem driveSystem = new DriveSystem(hardwareMap);
        ElevatorSystem elevatorSystem = new ElevatorSystem(hardwareMap);
        ClawSystem clawSystem = new ClawSystem(hardwareMap, telemetry);
        
        schedule(
            new WaitUntilCommand(this::isStarted),
            new DriveTele(driveSystem, gamepad1),
            new ControlElevator(elevatorSystem, gamepad1),
            new ControlClaw(clawSystem, gamepad1));
    }
}
