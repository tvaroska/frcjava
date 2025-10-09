// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * Legacy RobotContainer for backward compatibility.
 *
 * DEPRECATED: This class is maintained for backward compatibility with existing tests.
 * New code should use BaseRobotContainer and its subclasses (RobotAContainer, RobotBContainer).
 *
 * The robot now automatically selects between RobotA and RobotB configurations
 * based on MAC address detection in Robot.java.
 */
public class RobotContainer extends RobotAContainer {

    /**
     * Initializes using RobotA configuration.
     * This provides backward compatibility for tests that directly instantiate RobotContainer.
     */
    public RobotContainer() {
        super();
    }
}
