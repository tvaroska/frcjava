package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveToTag;

/**
 * Integration test for vision-guided driving functionality.
 * Tests the interaction between VisionSubsystem, DriveToTag command, and CommandSwerveDrivetrain.
 */
class VisionGuidedDrivingTest {
    private RobotContainer robotContainer;
    private DriveToTag driveToTagCommand;

    @BeforeEach
    void setup() {
        // Initialize HAL for simulation
        assert HAL.initialize(500, 0);

        // Enable the robot in teleop mode
        DriverStationSim.setEnabled(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.notifyNewData();

        // Create full robot container (integration test)
        robotContainer = new RobotContainer();

        // Get reference to vision subsystem through robot container
        // Create command that will be used for testing
        driveToTagCommand = new DriveToTag(
            robotContainer.getVision(),
            robotContainer.getDrivetrain()
        );
    }

    @AfterEach
    void teardown() {
        if (driveToTagCommand != null && driveToTagCommand.isScheduled()) {
            driveToTagCommand.cancel();
        }
        if (robotContainer != null && robotContainer.getDrivetrain() != null) {
            robotContainer.getDrivetrain().close();
        }
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().clearComposedCommands();
    }

    @Test
    void testRobotContainerInitializesWithVision() {
        assertNotNull(robotContainer);
        assertNotNull(robotContainer.getDrivetrain());
        assertNotNull(robotContainer.getVision());
    }

    @Test
    void testDrivetrainHasDefaultCommand() {
        assertNotNull(robotContainer.getDrivetrain().getDefaultCommand());
    }

    @Test
    void testVisionCommandSchedulesAndRuns() {
        // Schedule the DriveToTag command
        CommandScheduler.getInstance().schedule(driveToTagCommand);
        assertTrue(driveToTagCommand.isScheduled());

        // Run the command scheduler for several cycles
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                CommandScheduler.getInstance().run();
            }
        });

        // Command should still be running (no targets means it keeps searching)
        assertTrue(driveToTagCommand.isScheduled());
    }

    @Test
    void testVisionCommandInterruptsDefaultCommand() {
        var defaultCommand = robotContainer.getDrivetrain().getDefaultCommand();
        assertNotNull(defaultCommand);

        // Schedule vision command
        CommandScheduler.getInstance().schedule(driveToTagCommand);

        // Run scheduler
        CommandScheduler.getInstance().run();

        // DriveToTag should be running, not the default command
        assertTrue(driveToTagCommand.isScheduled());
        assertFalse(defaultCommand.isScheduled());
    }

    @Test
    void testVisionCommandCancellationRestoresDefault() {
        var defaultCommand = robotContainer.getDrivetrain().getDefaultCommand();

        // Schedule and run vision command
        CommandScheduler.getInstance().schedule(driveToTagCommand);
        CommandScheduler.getInstance().run();
        assertTrue(driveToTagCommand.isScheduled());

        // Cancel vision command
        driveToTagCommand.cancel();
        CommandScheduler.getInstance().run();

        // Default command should be restored
        assertFalse(driveToTagCommand.isScheduled());
        assertTrue(defaultCommand.isScheduled());
    }

    @Test
    void testPeriodicUpdatesCameraState() {
        var vision = robotContainer.getVision();

        // Run periodic updates
        for (int i = 0; i < 10; i++) {
            vision.periodic();
        }

        // Should track connection state
        assertNotNull(vision.getLatestResult());
    }

    @Test
    void testFullSystemRunsWithoutErrors() {
        // Simulate a full robot cycle with vision command
        CommandScheduler.getInstance().schedule(driveToTagCommand);

        assertDoesNotThrow(() -> {
            // Simulate 2 seconds of operation at 50Hz
            for (int i = 0; i < 100; i++) {
                // Update vision
                robotContainer.getVision().periodic();

                // Run command scheduler (handles commands and subsystem periodic)
                CommandScheduler.getInstance().run();

                // Simulate 20ms delay
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
