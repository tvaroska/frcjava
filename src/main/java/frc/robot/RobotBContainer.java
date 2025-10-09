// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * RobotB configuration - Competition robot.
 *
 * Current configuration:
 * - Swerve drivetrain (shared)
 * - Vision subsystem (shared)
 *
 * Future additions (Tag 4+):
 * - Simple flywheel mechanism (TalonFX motor)
 * - Elevator with PID control (Tag 5)
 * - Coordinated mechanisms (Tag 6)
 */
public class RobotBContainer extends BaseRobotContainer {

    /**
     * Initialize RobotB-specific subsystems and configurations.
     */
    public RobotBContainer() {
        super();
        System.out.println("Initialized RobotB configuration");
    }

    /**
     * Configure RobotB-specific button bindings.
     * Currently uses only shared drivetrain bindings.
     *
     * Future mechanisms will add bindings here (Tag 4+):
     * - Flywheel controls
     * - Elevator position controls
     */
    @Override
    protected void configureBindings() {
        // Currently no robot-specific bindings
        // Mechanisms will be added in future tags
    }

    /**
     * Gets the robot variant name.
     * @return "RobotB"
     */
    @Override
    public String getRobotName() {
        return "RobotB";
    }
}
