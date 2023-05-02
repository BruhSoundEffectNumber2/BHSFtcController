package org.firstinspires.ftc.teamcode.grab

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Claw
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * Subsystem for the grabber.
 */
class GrabSubsystem(
        hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<SimpleServo>(hardwareMap, telemetry) {
    var open = true
        private set

    override fun createComponent(): SimpleServo {
        return SimpleServo(
                hardwareMap,
                Hardware.CLAW,
                Claw.MIN_ANGLE,
                Claw.MAX_ANGLE
        )
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