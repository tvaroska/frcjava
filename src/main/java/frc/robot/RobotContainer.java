// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.commands.DriveToTag;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.VisionConstants;

/**
 * Central configuration for robot subsystems and operator controls.
 * Instantiates subsystems, configures button bindings, and provides autonomous commands.
 */
public class RobotContainer {
    private final double maxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    private final double maxAngularRate = RotationsPerSecond.of(DriveConstants.MAX_ANGULAR_RATE).in(RadiansPerSecond);

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(maxSpeed * DriveConstants.DEADBAND_PERCENT)
            .withRotationalDeadband(maxAngularRate * DriveConstants.DEADBAND_PERCENT)
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(maxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);

    private final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final VisionSubsystem vision = new VisionSubsystem(VisionConstants.CAMERA_NAME);

    private final SlewRateLimiter xSpeedLimiter = new SlewRateLimiter(DriveConstants.SLEW_RATE_LIMIT);
    private final SlewRateLimiter ySpeedLimiter = new SlewRateLimiter(DriveConstants.SLEW_RATE_LIMIT);
    private final SlewRateLimiter rotLimiter = new SlewRateLimiter(DriveConstants.SLEW_RATE_LIMIT);

    /**
     * Initializes subsystems and configures button bindings.
     */
    public RobotContainer() {
        configureBindings();
    }

    /**
     * Configures Xbox controller button bindings for drivetrain control.
     *
     * Default command: Field-centric swerve drive with slew rate limiting
     * - Left stick: Translation (X/Y)
     * - Right stick X: Rotation
     *
     * Button mappings:
     * - X: Brake mode (X-formation)
     * - A: Drive to nearest AprilTag (aligns and reaches target distance)
     * - Right bumper: Point wheels toward left stick direction
     * - Left bumper: Reset field-centric heading
     * - Back+Y/X: SysId dynamic characterization
     * - Start+Y/X: SysId quasistatic characterization
     */
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(
                    maxSpeed * xSpeedLimiter.calculate(-joystick.getLeftY())
                    ) // Drive forward with negative Y (forward)
                    .withVelocityY(
                        maxSpeed * ySpeedLimiter.calculate(-joystick.getLeftX())
                    ) // Drive left with negative X (left)
                    .withRotationalRate(
                        maxAngularRate * rotLimiter.calculate(-joystick.getRightX())
                    ) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // X button: brake mode
        joystick.x().whileTrue(drivetrain.applyRequest(() -> brake));

        // A button: Drive to AprilTag
        joystick.a().whileTrue(new DriveToTag(vision, drivetrain));

        // Right bumper: point wheels
        joystick.rightBumper().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    /**
     * Gets the drivetrain subsystem.
     * @return The CommandSwerveDrivetrain instance
     */
    public CommandSwerveDrivetrain getDrivetrain() {
        return drivetrain;
    }

    /**
     * Gets the vision subsystem.
     * Package-private for testing purposes.
     * @return The VisionSubsystem instance
     */
    VisionSubsystem getVision() {
        return vision;
    }

}
