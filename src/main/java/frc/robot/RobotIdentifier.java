// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.Constants.RobotIdentity;

/**
 * Utility class for automatic robot variant detection based on MAC address.
 *
 * Real FRC teams typically have multiple robots:
 * - Practice robot for testing and driver practice
 * - Competition robot for events
 *
 * This class enables maintaining both configurations in a single codebase
 * with automatic detection based on RoboRIO hardware MAC address.
 */
public class RobotIdentifier {

    /**
     * Detects which robot variant is running based on MAC address.
     *
     * @return The appropriate BaseRobotContainer subclass instance
     */
    public static BaseRobotContainer detectRobot() {
        String mac = getRoboRioMacAddress();

        if (mac == null) {
            DriverStation.reportWarning(
                "Could not detect MAC address. Using default robot: " + RobotIdentity.DEFAULT_ROBOT,
                false
            );
            return createDefaultRobot();
        }

        DriverStation.reportWarning("Detected MAC address: " + mac, false);

        if (mac.equalsIgnoreCase(RobotIdentity.ROBOT_A_MAC)) {
            return new RobotAContainer();
        } else if (mac.equalsIgnoreCase(RobotIdentity.ROBOT_B_MAC)) {
            return new RobotBContainer();
        } else {
            DriverStation.reportWarning(
                "Unknown MAC address: " + mac + ". Using default robot: " + RobotIdentity.DEFAULT_ROBOT,
                false
            );
            return createDefaultRobot();
        }
    }

    /**
     * Creates the default robot container for simulation/unknown hardware.
     *
     * @return Default robot container instance
     */
    private static BaseRobotContainer createDefaultRobot() {
        if (RobotIdentity.DEFAULT_ROBOT.equalsIgnoreCase("RobotB")) {
            return new RobotBContainer();
        } else {
            return new RobotAContainer();
        }
    }

    /**
     * Gets the MAC address of the RoboRIO's primary network interface.
     *
     * Searches for the first non-loopback network interface with a valid MAC address.
     * On RoboRIO, this is typically eth0.
     *
     * @return MAC address as a colon-separated string (e.g., "00:80:2F:17:D7:E1"), or null if not found
     */
    private static String getRoboRioMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // Skip loopback and virtual interfaces
                if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                // Get MAC address
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac == null || mac.length == 0) {
                    continue;
                }

                // Convert to standard MAC address format (XX:XX:XX:XX:XX:XX)
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X", mac[i]));
                    if (i < mac.length - 1) {
                        sb.append(":");
                    }
                }

                return sb.toString();
            }
        } catch (SocketException e) {
            DriverStation.reportError("Failed to get network interfaces: " + e.getMessage(), false);
        }

        return null;
    }
}
