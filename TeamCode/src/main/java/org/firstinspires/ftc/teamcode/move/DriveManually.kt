package org.firstinspires.ftc.teamcode.move

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import org.firstinspires.ftc.teamcode.SimpleCommand
import org.firstinspires.ftc.teamcode.config.DriverControl
import org.firstinspires.ftc.teamcode.inputToPower

/**
 * Command that allows the driver to control the robot manually.
 */
class DriveManually(
    telemetry: MultipleTelemetry, system: MoveSubsystem, private val controls: GamepadEx
) : SimpleCommand<MoveSubsystem>(telemetry, system) {
    override fun execute() {
        system.forward = inputToPower(
            controls.leftY,
            DriverControl.FORWARD_CURVE,
            DriverControl.FORWARD_DEADZONE,
            DriverControl.FORWARD_SCALE
        )

        // Strafe is the difference between the left and right triggers
        // The comparison of the two trigger gives that difference
        val strafe = controls.getButton(GamepadKeys.Button.RIGHT_BUMPER)
            .compareTo(controls.getButton(GamepadKeys.Button.LEFT_BUMPER))

        system.strafe = inputToPower(
            strafe.toDouble(),
            DriverControl.STRAFE_CURVE,
            DriverControl.STRAFE_DEADZONE,
            DriverControl.STRAFE_SCALE
        )

        system.turn = inputToPower(
            controls.rightX,
            DriverControl.TURN_CURVE,
            DriverControl.TURN_DEADZONE,
            DriverControl.TURN_SCALE
        )
    }

    override fun end(interrupted: Boolean) {
        system.stop()
    }
}