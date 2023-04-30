package org.firstinspires.ftc.teamcode.signal

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.vision.AprilTagDetector
import com.arcrobotics.ftclib.vision.DetectorState
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.config.Hardware

/**
 * Reads the state of the signal.
 * 
 * @param returnCallback Called when the signal state is read.
 */
class ReadSignalState(
    hardwareMap: HardwareMap,
    private val returnCallback: ((SignalState) -> Unit)? = null
) : CommandBase() {
    private val detector = AprilTagDetector(hardwareMap, Hardware.CAMERA)
    private var finalState = SignalState.Unknown
    
    override fun initialize() {
        // Search for tags 0, 1, and 2 from the 36h11 family
        detector.setTargets(0, 1, 2)
        detector.init()
    }
    
    override fun end(interrupted: Boolean) {
        // Only use the callback if we ended normally
        if (interrupted.not()) {
            returnCallback?.invoke(finalState)
        }
    }

    override fun isFinished(): Boolean {
        // Don't finish while setting up the detector
        if (detector.detectorState != DetectorState.RUNNING) {
            return false
        }

        // Don't finish if we don't have a detection
        val detection = detector.detection ?: return false

        // Set the final state based on the detection
        finalState = when (detection["id"]) {
            0 -> SignalState.Left
            1 -> SignalState.Center
            2 -> SignalState.Right
            else -> SignalState.Unknown
        }
        
        // We have a detection, so we can finish
        return true
    }
}