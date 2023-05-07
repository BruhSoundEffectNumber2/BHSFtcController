package org.firstinspires.ftc.teamcode.claw

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.SimpleSubsystem
import org.firstinspires.ftc.teamcode.config.Claw
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * Subsystem that controls the claw.
 *
 * The claw is a [SimpleServo] that can be opened, closed, or toggled.
 *
 * @param hardwareMap The hardware map that this subsystem uses to get the physical component.
 * @param telemetry The telemetry that this subsystem will use.
 */
class ClawSubsystem(
    hardwareMap: HardwareMap, telemetry: MultipleTelemetry
) : SimpleSubsystem<SimpleServo>(hardwareMap, telemetry) {
    /** Whether the claw is open or closed. */
    var open = true
        private set

    override fun createComponent(): SimpleServo {
        val servo = SimpleServo(
            hardwareMap, Hardware.CLAW, Claw.MIN_ANGLE, Claw.MAX_ANGLE
        )

        // The claw should be open at the start
        servo.turnToAngle(Claw.OPEN_ANGLE)

        return servo
    }

    /**
     * Toggles the claw between open and closed.
     * If the claw is open, it will close it. If it is closed, it will open it.
     */
    fun toggle() {
        if (open) {
            close()
        } else {
            open()
        }
    }

    /** Opens the claw. */
    fun open() {
        component.turnToAngle(Claw.OPEN_ANGLE)
        open = true
    }

    /** Closes the claw. */
    fun close() {
        component.turnToAngle(Claw.CLOSE_ANGLE)
        open = false
    }
}