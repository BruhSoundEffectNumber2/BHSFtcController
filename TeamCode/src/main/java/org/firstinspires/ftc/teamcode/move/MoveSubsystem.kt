package org.firstinspires.ftc.teamcode.move

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * System that controls the drivetrain in Auto and TeleOp.
 */
class MoveSubsystem(
    hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<MecanumDrive>(hardwareMap, telemetry) {

    var forward = 0.0
        set(value) {
            field = clampPower(value)
        }
    var strafe = 0.0
        set(value) {
            field = clampPower(value)
        }
    var turn = 0.0
        set(value) {
            field = clampPower(value)
        }

    fun stop() {
        forward = 0.0
        strafe = 0.0
        turn = 0.0
        component.stop()
    }

    override fun periodic() {
        // The motors are inverted because of the way they are mounted
        component.driveRobotCentric(-strafe, -forward, -turn)

        telemetry.addData("Forward", forward)
        telemetry.addData("Strafe", strafe)
        telemetry.addData("Turn", turn)
    }

    override fun createComponent(): MecanumDrive {
        return MecanumDrive(
            // We need to invert the right motors because of the way they are mounted
            true,
            Motor(hardwareMap, Hardware.FRONT_LEFT, Hardware.DRIVE_TYPE),
            Motor(hardwareMap, Hardware.FRONT_RIGHT, Hardware.DRIVE_TYPE),
            Motor(hardwareMap, Hardware.BACK_LEFT, Hardware.DRIVE_TYPE),
            Motor(hardwareMap, Hardware.BACK_RIGHT, Hardware.DRIVE_TYPE),
        )
    }

    /**
     * Clamps the given power to be between -1 and 1.
     */
    private fun clampPower(power: Double): Double {
        return power.coerceIn(-1.0, 1.0)
    }
}