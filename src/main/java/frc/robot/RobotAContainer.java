// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * RobotA configuration - Practice robot.
 *
 * Current configuration:
 * - Swerve drivetrain (shared)
 * - Vision subsystem (shared)
 *
 * Future additions (Tag 4+):
 * - Simple intake mechanism (SparkMax motor)
 * - Arm mechanism (Tag 6)
 */
public class RobotAContainer extends BaseRobotContainer {

    /**
     * Initialize RobotA-specific subsystems and configurations.
     */
    public RobotAContainer() {
        super();
        System.out.println("Initialized RobotA configuration");
    }

    /**
     * Configure RobotA-specific button bindings.
     * Currently uses only shared drivetrain bindings.
     *
     * Future mechanisms will add bindings here (Tag 4+):
     * - Intake controls
     * - Arm position controls
     */
    @Override
    protected void configureBindings() {
        // Currently no robot-specific bindings
        // Mechanisms will be added in future tags
    }

    /**
     * Gets the robot variant name.
     * @return "RobotA"
     */
    @Override
    public String getRobotName() {
        return "RobotA";
    }
}
