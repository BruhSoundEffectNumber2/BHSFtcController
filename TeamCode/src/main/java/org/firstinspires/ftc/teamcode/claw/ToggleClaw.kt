package org.firstinspires.ftc.teamcode.claw

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import org.firstinspires.ftc.teamcode.SimpleCommand

class ToggleClaw(telemetry: MultipleTelemetry, system: ClawSubsystem, private val controls: GamepadEx
) : SimpleCommand<ClawSubsystem>(telemetry, system) {
    override fun initialize() {
        system.toggle()
    }
}