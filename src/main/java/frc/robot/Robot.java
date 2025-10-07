// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * Main robot class using TimedRobot framework.
 * Runs at 50Hz (20ms period) by default.
 */
public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;

  /**
   * Initializes robot hardware and subsystems.
   * Called once when robot code starts.
   */
  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
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
