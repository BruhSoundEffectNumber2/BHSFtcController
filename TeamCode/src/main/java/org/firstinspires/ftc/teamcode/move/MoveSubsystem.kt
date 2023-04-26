package org.firstinspires.ftc.teamcode.move

import kotlin.ranges.coerceIn
import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * System that controls the drivetrain in Auto and TeleOp.
 */
class MoveSubsystem(private val hardwareMap: HardwareMap) : SubsystemBase() {
    private val drivetrain = createDrivetrain()

    // TODO: These variables are shared
    var forward = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }
    var strafe = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }
    var turn = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    fun stop() {
        forward = 0.0
        strafe = 0.0
        turn = 0.0
        drivetrain.stop()
    }

    override fun periodic() {

    }

    private fun createDrivetrain(): MecanumDrive {
        return MecanumDrive(
                // Our drivetrain motors need to be inverted
                true,
                Motor(hardwareMap, Hardware.DRIVE_FRONT_LEFT, Hardware.DRIVE_MOTOR_TYPE),
                Motor(hardwareMap, Hardware.DRIVE_FRONT_RIGHT, Hardware.DRIVE_MOTOR_TYPE),
                Motor(hardwareMap, Hardware.DRIVE_BACK_LEFT, Hardware.DRIVE_MOTOR_TYPE),
                Motor(hardwareMap, Hardware.DRIVE_BACK_RIGHT, Hardware.DRIVE_MOTOR_TYPE),
        )
    }
}