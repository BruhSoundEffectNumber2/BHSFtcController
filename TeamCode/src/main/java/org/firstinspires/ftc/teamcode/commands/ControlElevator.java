package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.systems.ElevatorSystem;

public class ControlElevator extends CommandBase {
    ElevatorSystem system;
    GamepadEx controls;

    public ControlElevator(ElevatorSystem elevatorSystem, Gamepad systemsPad) {
        system = elevatorSystem;
        controls = new GamepadEx(systemsPad);

        addRequirements(system);
    }

    @Override
    public void execute() {
        double input = controls.getRightY();

        system.setVelocity(input);
    }
}
