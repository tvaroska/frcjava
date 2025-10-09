// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * Main robot class using TimedRobot framework.
 * Runs at 50Hz (20ms period) by default.
 *
 * Automatically detects robot variant (RobotA/RobotB) based on MAC address
 * and loads the appropriate configuration.
 */
public class Robot extends TimedRobot {
  private BaseRobotContainer m_robotContainer;

  /**
   * Initializes robot hardware and subsystems.
   * Called once when robot code starts.
   *
   * Uses RobotIdentifier to automatically detect which robot variant
   * is running and instantiate the appropriate container.
   */
  @Override
  public void robotInit() {
    m_robotContainer = RobotIdentifier.detectRobot();
    System.out.println("==============================================");
    System.out.println("  Robot Initialized: " + m_robotContainer.getRobotName());
    System.out.println("==============================================");
  }

  /**
   * Runs the command scheduler.
   * Called every 20ms regardless of robot mode.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
}
