package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.subsystems.VisionSubsystem;

class VisionSubsystemTest {
    private VisionSubsystem vision;

    @BeforeEach
    void setup() {
        // Initialize HAL for simulation
        assert HAL.initialize(500, 0);

        // Enable the robot in teleop mode
        DriverStationSim.setEnabled(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.notifyNewData();

        // Create the vision subsystem
        vision = new VisionSubsystem("testcamera");
    }

    @AfterEach
    void teardown() {
        // VisionSubsystem cleanup handled by WPILib
    }

    @Test
    void testVisionSubsystemInitializes() {
        assertNotNull(vision);
    }

    @Test
    void testNoTargetsInitially() {
        // Without a real camera or simulation, should have no targets
        assertFalse(vision.hasTargets());
        assertEquals(-1, vision.getTargetID());
        assertEquals(0.0, vision.getTargetYaw());
        assertEquals(0.0, vision.getTargetPitch());
        assertEquals(0.0, vision.getTargetArea());
    }

    @Test
    void testGetBestTargetReturnsEmpty() {
        // Without targets, should return empty Optional
        assertTrue(vision.getBestTarget().isEmpty());
    }

    @Test
    void testPeriodicDoesNotThrow() {
        // Periodic should not crash even without camera connected
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                vision.periodic();
            }
        });
    }

    @Test
    void testCameraConnectionTracking() {
        // Initially should be connected (optimistic)
        assertTrue(vision.isCameraConnected());

        // After many periodic calls with no data, should detect disconnect
        for (int i = 0; i < 60; i++) {
            vision.periodic();
        }

        // Should now be marked as disconnected
        assertFalse(vision.isCameraConnected());
    }

    @Test
    void testGetLatestResultNotNull() {
        assertNotNull(vision.getLatestResult());
    }
}
