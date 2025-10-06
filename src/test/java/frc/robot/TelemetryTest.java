package frc.robot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;

class TelemetryTest {
    private Telemetry telemetry;
    private static final double MAX_SPEED = 4.5; // meters per second

    @BeforeEach
    void setup() {
        // Initialize HAL for NetworkTables
        assert HAL.initialize(500, 0);

        telemetry = new Telemetry(MAX_SPEED);
    }

    @AfterEach
    void teardown() {
        NetworkTableInstance.getDefault().close();
    }

    @Test
    void testTelemetryPublishesData() {
        SwerveDriveState state = new SwerveDriveState();
        state.Pose = new Pose2d(3.0, 4.0, new Rotation2d(1.0));
        state.Speeds = new ChassisSpeeds();
        state.ModuleStates = new SwerveModuleState[4];
        state.ModuleTargets = new SwerveModuleState[4];
        state.ModulePositions = new SwerveModulePosition[4];

        for (int i = 0; i < 4; i++) {
            state.ModuleStates[i] = new SwerveModuleState();
            state.ModuleTargets[i] = new SwerveModuleState();
            state.ModulePositions[i] = new SwerveModulePosition();
        }

        state.Timestamp = 2.0;
        state.OdometryPeriod = 0.02;

        assertDoesNotThrow(() -> telemetry.telemeterize(state));
    }
}
