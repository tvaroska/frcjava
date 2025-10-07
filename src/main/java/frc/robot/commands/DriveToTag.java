package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;

/**
 * Command to drive toward and align with the nearest AprilTag.
 * Uses simple P-controllers for both rotation (yaw) and forward drive (area/distance).
 */
public class DriveToTag extends Command {
    private final VisionSubsystem vision;
    private final CommandSwerveDrivetrain drivetrain;

    private final SwerveRequest.FieldCentric driveRequest = new SwerveRequest.FieldCentric()
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    // Cached command to avoid lambda creation every execute cycle
    private Command cachedDriveCommand;

    /**
     * Creates a new DriveToTag command.
     * @param vision VisionSubsystem for target detection
     * @param drivetrain CommandSwerveDrivetrain for robot movement
     */
    public DriveToTag(VisionSubsystem vision, CommandSwerveDrivetrain drivetrain) {
        this.vision = vision;
        this.drivetrain = drivetrain;
        addRequirements(vision, drivetrain);
    }

    @Override
    public void initialize() {
        cachedDriveCommand = null; // Reset cached command on initialization
    }

    @Override
    public void execute() {
        if (!vision.hasTargets()) {
            // No target visible - stop moving
            drivetrain.setControl(driveRequest
                .withVelocityX(0)
                .withVelocityY(0)
                .withRotationalRate(0)
            );
            return;
        }

        // Get target information
        double yawError = vision.getTargetYaw();
        double currentArea = vision.getTargetArea();
        double areaError = VisionConstants.AREA_TARGET - currentArea;

        // Calculate rotation speed (align with target)
        double calculatedRotation = -yawError * VisionConstants.ROTATION_P;
        if (Math.abs(yawError) > VisionConstants.ANGLE_TOLERANCE) {
            if (Math.abs(calculatedRotation) < VisionConstants.MIN_ROTATION_SPEED) {
                calculatedRotation = Math.copySign(VisionConstants.MIN_ROTATION_SPEED, calculatedRotation);
            }
        }
        final double rotationSpeed = Math.max(-VisionConstants.MAX_ROTATION_SPEED,
            Math.min(VisionConstants.MAX_ROTATION_SPEED, calculatedRotation));

        // Calculate forward drive speed (approach target)
        // Positive area error means we're too far, need to drive forward
        double calculatedDrive = areaError * VisionConstants.DRIVE_P;

        // Only drive forward if we're reasonably aligned
        if (Math.abs(yawError) > VisionConstants.MAX_YAW_ERROR_FOR_DRIVE) {
            calculatedDrive = 0; // Rotate first, then drive
        } else if (Math.abs(areaError) > VisionConstants.AREA_TOLERANCE) {
            if (Math.abs(calculatedDrive) < VisionConstants.MIN_DRIVE_SPEED) {
                calculatedDrive = Math.copySign(VisionConstants.MIN_DRIVE_SPEED, calculatedDrive);
            }
        }
        final double driveSpeed = Math.max(-VisionConstants.MAX_DRIVE_SPEED,
            Math.min(VisionConstants.MAX_DRIVE_SPEED, calculatedDrive));

        // Apply movement (forward X, no strafe Y, rotation)
        // Cache command to avoid creating new lambda every cycle
        if (cachedDriveCommand == null) {
            cachedDriveCommand = drivetrain.applyRequest(() -> driveRequest
                .withVelocityX(driveSpeed)
                .withVelocityY(0)
                .withRotationalRate(rotationSpeed)
            );
        }

        // Update the request with new values and execute
        drivetrain.setControl(driveRequest
            .withVelocityX(driveSpeed)
            .withVelocityY(0)
            .withRotationalRate(rotationSpeed)
        );
    }

    @Override
    public void end(boolean interrupted) {
        // Stop moving when command ends
        drivetrain.setControl(driveRequest
            .withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(0)
        );
        cachedDriveCommand = null; // Clear cache
    }

    @Override
    public boolean isFinished() {
        // Finish when both aligned and at target distance
        if (!vision.hasTargets()) {
            return false; // Keep running until we see and reach target, or get interrupted
        }

        double yawError = Math.abs(vision.getTargetYaw());
        double areaError = Math.abs(VisionConstants.AREA_TARGET - vision.getTargetArea());

        return yawError < VisionConstants.ANGLE_TOLERANCE && areaError < VisionConstants.AREA_TOLERANCE;
    }
}
