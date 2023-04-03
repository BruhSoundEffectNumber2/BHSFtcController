package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.systems.ClawSystem;

public class ControlClaw extends CommandBase {
    ClawSystem system;
    GamepadEx controls;

    public ControlClaw(ClawSystem system, Gamepad controls) {
        this.system = system;
        this.controls = new GamepadEx(controls);

        addRequirements(system);

        new GamepadButton(this.controls, GamepadKeys.Button.A).whenPressed(system::toggle);
    }
}
