package org.firstinspires.ftc.teamcode.grab

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import org.firstinspires.ftc.teamcode.SimpleCommand

class Grab(telemetry: MultipleTelemetry, system: GrabSubsystem, private val controls: GamepadEx
) : SimpleCommand<GrabSubsystem>(telemetry, system) {
    override fun initialize() {
        GamepadButton(controls, GamepadKeys.Button.X).whenPressed(system::toggle)
    }
}