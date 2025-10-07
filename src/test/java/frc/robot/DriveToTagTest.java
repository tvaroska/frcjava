package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveToTag;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;

class DriveToTagTest {
    private VisionSubsystem vision;
    private CommandSwerveDrivetrain drivetrain;
    private DriveToTag command;

    @BeforeEach
    void setup() {
        // Initialize HAL for simulation
        assert HAL.initialize(500, 0);

        // Enable the robot in teleop mode
        DriverStationSim.setEnabled(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.notifyNewData();

        // Create subsystems
        vision = new VisionSubsystem("testcamera");
        drivetrain = TunerConstants.createDrivetrain();
        command = new DriveToTag(vision, drivetrain);
    }

    @AfterEach
    void teardown() {
        if (command != null && command.isScheduled()) {
            command.cancel();
        }
        if (drivetrain != null) {
            drivetrain.close();
        }
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().clearComposedCommands();
    }

    @Test
    void testCommandInitializes() {
        assertNotNull(command);
    }

    @Test
    void testCommandRequiresSubsystems() {
        var requirements = command.getRequirements();
        assertTrue(requirements.contains(vision));
        assertTrue(requirements.contains(drivetrain));
    }

    @Test
    void testCommandRunsWithoutTargets() {
        // Command should run without throwing even when no targets visible
        assertDoesNotThrow(() -> {
            command.initialize();
            for (int i = 0; i < 10; i++) {
                command.execute();
            }
            command.end(false);
        });
    }

    @Test
    void testCommandNotFinishedWithoutTargets() {
        command.initialize();
        command.execute();

        // Should not finish when no targets are visible
        assertFalse(command.isFinished());
    }

    @Test
    void testCommandSchedules() {
        // Command should schedule without errors
        assertDoesNotThrow(() -> {
            CommandScheduler.getInstance().schedule(command);
            assertTrue(command.isScheduled());
        });
    }

    @Test
    void testCommandCancels() {
        CommandScheduler.getInstance().schedule(command);
        assertTrue(command.isScheduled());

        command.cancel();
        assertFalse(command.isScheduled());
    }

    @Test
    void testCommandEndsCleanly() {
        command.initialize();
        command.execute();

        // Should end without throwing
        assertDoesNotThrow(() -> command.end(false));
        assertDoesNotThrow(() -> command.end(true));
    }

    @Test
    void testCommandWithScheduler() {
        // Schedule command and run through scheduler
        CommandScheduler.getInstance().schedule(command);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 20; i++) {
                CommandScheduler.getInstance().run();
            }
        });
    }
}
