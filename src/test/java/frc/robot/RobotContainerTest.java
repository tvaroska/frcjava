package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

class RobotContainerTest {
    private RobotContainer robotContainer;

    @BeforeEach
    void setup() {
        // Initialize HAL for simulation
        assert HAL.initialize(500, 0);

        // Enable the robot in teleop mode
        DriverStationSim.setEnabled(true);
        DriverStationSim.setAutonomous(false);
        DriverStationSim.notifyNewData();

        // Create the robot container
        robotContainer = new RobotContainer();
    }

    @AfterEach
    void teardown() {
        // Clean up after each test
        if (robotContainer != null && robotContainer.getDrivetrain() != null) {
            robotContainer.getDrivetrain().close();
        }
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().clearComposedCommands();
    }

    @Test
    void testRobotInitializesAndRuns() {
        assertNotNull(robotContainer.getDrivetrain());
        assertNotNull(robotContainer.getDrivetrain().getDefaultCommand());

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                CommandScheduler.getInstance().run();
            }
        });
    }
}
