package org.firstinspires.ftc.teamcode.claw

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Claw
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * Subsystem for the claw.
 */
class ClawSubsystem(
        hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<SimpleServo>(hardwareMap, telemetry) {
    var open = true
        private set

    override fun createComponent(): SimpleServo {
        val servo = SimpleServo(
                hardwareMap,
                Hardware.CLAW,
                Claw.MIN_ANGLE,
                Claw.MAX_ANGLE
        )

        open()

        return servo
    }

    fun toggle() {
        if (open) {
            close()
        } else {
            open()
        }
    }

    fun open() {
        component.turnToAngle(Claw.OPEN_ANGLE)
    }

    fun close() {
        component.turnToAngle(Claw.CLOSE_ANGLE)
    }
}