# frcjava - FRC Robot Development Learning Project

Progressive learning project for building competition-ready FRC robot software from basic drivetrain to full autonomous capabilities.

**Learning Approach:** Start with core driving, add vision early for engagement, then build up mechanisms and autonomous capabilities.

## Quick Start

```bash
./gradlew build          # Compile and run tests
./gradlew deploy         # Deploy to RoboRIO
./gradlew simulateJava   # Run in simulation
```

## Project Status: Tag 3 Complete ✅

Currently at **Tag 3: `multi-config`** - Multiple robot variant support with automatic detection.

### What's Working
- ✅ Swerve drivetrain with field-centric control
- ✅ Xbox controller bindings
- ✅ Slew rate limiting for smooth driving
- ✅ Telemetry via CTRE SignalLogger
- ✅ PhotonVision integration with AprilTag detection
- ✅ DriveToTag command for autonomous alignment
- ✅ **Automatic MAC address-based robot detection**
- ✅ **BaseRobotContainer abstract class with inheritance**
- ✅ **RobotA and RobotB configurations**
- ✅ **Comprehensive test coverage (22 tests passing)**

### Current Controls
- **Left stick**: Translation (forward/backward, strafe left/right)
- **Right stick X**: Rotation
- **A button**: **Drive to nearest AprilTag** (vision-guided)
- **Left bumper**: Reset field-centric heading
- **X button**: Brake mode
- **Right bumper**: Point wheels
- **Back/Start + X/Y**: SysId characterization

## Development Roadmap

This project follows a 7-tag progressive learning path. Each tag builds on the previous, teaching specific skills and concepts.

### Tag 1: `drive` ✅ COMPLETED

**Goal:** Basic teleop swerve driving

**What You Built:**
- CommandSwerveDrivetrain subsystem
- Xbox controller bindings
- Field-centric control with slew rate limiting
- Telemetry logging

**Skills Learned:**
- Command-based framework structure
- Subsystem creation and lifecycle
- Controller input mapping
- CTRE Phoenix 6 swerve API
- Field-centric coordinate systems

---

### Tag 2: `basic-vision` ✅ COMPLETED

**Goal:** Drive to nearest AprilTag using vision

**What You Built:**
- VisionSubsystem using PhotonVision
- DriveToTag command with dual P-controllers
- Error handling and connection monitoring
- SmartDashboard telemetry
- Comprehensive test suite (22 tests)

**Skills Learned:**
- PhotonVision integration and configuration
- AprilTag detection
- Vision-guided robot control with P-controllers
- Multi-subsystem commands
- Error handling for vision failures
- Optional-based null safety
- Unit and integration testing

**Files Created:**
```
subsystems/VisionSubsystem.java
commands/DriveToTag.java
vendordeps/photonlib.json
test/java/frc/robot/VisionSubsystemTest.java
test/java/frc/robot/DriveToTagTest.java
test/java/frc/robot/VisionGuidedDrivingTest.java
```

**Code Quality Improvements:**
- All magic numbers extracted to Constants
- Consistent camelCase naming conventions
- Private fields with getter methods
- Removed unused code and empty methods
- Performance optimizations (direct setControl calls)

---

### Tag 3: `multi-config` ✅ COMPLETED

**Goal:** Support multiple robot variants automatically

**What You Built:**
- BaseRobotContainer abstract class
- RobotAContainer and RobotBContainer implementations
- RobotIdentifier utility for MAC address-based detection
- Automatic robot variant selection in Robot.java
- RobotIdentity constants for MAC addresses
- Backward-compatible RobotContainer for existing tests

**Skills Learned:**
- Inheritance and polymorphism
- Hardware-based auto-configuration
- Code organization and abstraction
- Protected vs public methods
- Java NetworkInterface API for hardware detection

**Files Created:**
```
BaseRobotContainer.java        # Abstract base with shared functionality
RobotAContainer.java           # Practice robot configuration
RobotBContainer.java           # Competition robot configuration
RobotIdentifier.java           # MAC address detection utility
```

**Files Modified:**
```
Robot.java                     # Now uses automatic detection
RobotContainer.java            # Extends RobotAContainer for compatibility
Constants.java                 # Added RobotIdentity class with MAC addresses
```

**Code Quality:**
- All 22 tests passing
- Backward compatible with existing tests
- Clear console output showing detected robot
- Extensible design for future mechanisms

---

### Tag 4: `simple-mechanism` 🎯 NEXT

**Goal:** Add first game piece mechanism

**What You'll Build:**
- **RobotA**: Simple intake (SparkMax motor)
- **RobotB**: Simple flywheel (TalonFX motor)
- Open-loop motor control
- Current limiting and safety

**Skills You'll Learn:**
- REV SparkMax vs CTRE TalonFX APIs
- Motor configuration and safety
- Button-to-command bindings
- Telemetry for mechanisms

---

### Tag 5: `elevator`

**Goal:** Master closed-loop PID control

**What You'll Build (RobotB only):**
- Elevator with position control
- PID tuning via SmartDashboard
- Limit switches and soft limits
- Preset positions (bottom, mid, top)

**Skills You'll Learn:**
- PID controller fundamentals
- Encoder configuration
- Live tuning workflow
- Safety interlocks

---

### Tag 6: `game-mechanisms`

**Goal:** Coordinate multiple subsystems

**What You'll Build:**
- **RobotA**: Arm + intake coordination
- **RobotB**: Elevator + mechanism integration
- State machines for game piece flow
- Operator controller (2nd Xbox)

**Skills You'll Learn:**
- Command groups and sequences
- Multi-subsystem coordination
- State machine design
- Driver vs operator controls

---

### Tag 7: `autonomous`

**Goal:** Path following and autonomous routines

**What You'll Build:**
- PathPlanner integration
- **RobotA**: Simple 1-piece auto
- **RobotB**: Complex 3-piece auto
- Named commands for event markers
- Autonomous selector

**Skills You'll Learn:**
- PathPlanner GUI usage
- Trajectory generation and following
- Odometry and pose estimation
- Auto command composition

---

## Learning Timeline

**For Practice/Learning (recommended):**
- Tag 1: 1 week ✅ DONE
- Tag 2: 1 week ✅ DONE (basic vision - exciting!)
- Tag 3: 2-3 days ✅ DONE (architecture)
- Tag 4: 1 week (motor APIs) 🎯 NEXT
- Tag 5: 1-2 weeks (PID tuning)
- Tag 6: 2 weeks (coordination)
- Tag 7: 2-3 weeks (paths and auto)

**Total: 10-12 weeks for complete mastery**
**Progress: 3/7 tags complete (43%)**

**For Competition (condensed):**
- Focus on tags 1-6 first (8-10 weeks)
- Add tag 7 during build season (2-3 weeks)

## Git Workflow

Each tag is a git branch and tag:

```bash
# Tag your current work
git add .
git commit -m "Complete Tag 1: drive"
git tag -a drive -m "Tag 1: Basic swerve drivetrain"

# Start next tag
git checkout -b basic-vision
# ... work on Tag 2 ...
git commit -m "Complete Tag 2: basic vision"
git tag -a basic-vision -m "Tag 2: Basic vision with AprilTag driving"

# Continue pattern for each tag
```

This allows you to:
- Return to any learning stage
- Compare implementations
- Share specific tags with teammates

## Project Structure

```
frcjava/
├── src/main/java/frc/robot/
│   ├── Robot.java                    # Main robot class with auto-detection
│   ├── BaseRobotContainer.java       # Abstract base for robot variants
│   ├── RobotAContainer.java          # Practice robot configuration
│   ├── RobotBContainer.java          # Competition robot configuration
│   ├── RobotContainer.java           # Legacy compatibility wrapper
│   ├── RobotIdentifier.java          # MAC address detection utility
│   ├── Constants.java                # Robot constants (organized by subsystem)
│   ├── Telemetry.java                # Swerve telemetry
│   ├── subsystems/
│   │   ├── CommandSwerveDrivetrain.java
│   │   └── VisionSubsystem.java      # PhotonVision AprilTag detection
│   ├── commands/
│   │   └── DriveToTag.java           # Vision-guided alignment command
│   └── generated/
│       ├── TunerConstants.java       # CTRE Tuner X - DO NOT EDIT
│       └── ModuleConstants.java
├── src/test/java/frc/robot/          # Test suite (22 tests)
│   ├── RobotContainerTest.java
│   ├── TelemetryTest.java
│   ├── VisionSubsystemTest.java
│   ├── DriveToTagTest.java
│   └── VisionGuidedDrivingTest.java
├── vendordeps/                       # Vendor libraries
│   ├── Phoenix6-frc2025-latest.json  # CTRE Phoenix 6
│   ├── photonlib.json                # PhotonVision
│   ├── AdvantageKit.json             # Logging (not active)
│   └── WPILibNewCommands.json
└── build.gradle                      # Build configuration
```

## Hardware Configuration

**Swerve Drivetrain:**
- 4x TalonFX drive motors (CAN IDs: 1, 3, 5, 7)
- 4x TalonFX steer motors (CAN IDs: 2, 4, 6, 8)
- 4x CANcoder encoders (CAN IDs: 20, 21, 22, 23)
- Pigeon 2 IMU (CAN ID: 24)
- All on "rio" CAN bus
- Max speed: 4.99 m/s at 12V

**Tuner Constants:**
- Located in `generated/TunerConstants.java`
- **Never edit manually** - use CTRE Tuner X tool
- Contains PID gains, gear ratios, encoder offsets

## Key Documentation

- **Full Plan**: See `/home/boris/robotics/plan.md` for detailed implementation guide
- **Workspace Guide**: See `/home/boris/robotics/CLAUDE.md` for project context
- **WPILib Docs**: https://docs.wpilib.org
- **CTRE Phoenix 6**: https://v6.docs.ctr-electronics.com
- **PhotonVision** (Tag 2+): https://docs.photonvision.org

## Common Commands

```bash
# Build and deploy
./gradlew build                # Compile code
./gradlew deploy               # Deploy to robot
./gradlew clean                # Clean build artifacts

# Testing
./gradlew test                 # Run unit tests
./gradlew simulateJava         # Run simulation with GUI

# Useful tools
./gradlew Glass                # Telemetry viewer
./gradlew ShuffleBoard         # Dashboard
```

## Troubleshooting

**Build Errors:**
- Ensure Java 17 is installed
- Run `./gradlew clean build`
- Check vendordeps are up to date

**Deploy Errors:**
- Verify robot is powered on
- Check Driver Station connection
- Ensure team number is set in `.wpilib/wpilib_preferences.json`

**Simulation Issues:**
- "Joystick not available" - Normal, no physical controller needed
- "CAN frame too stale" - Normal in simulation during startup

## Contributing to Your Learning

As you progress through tags:

1. **Document issues** - Keep notes on problems and solutions
2. **Experiment** - Try variations beyond the plan
3. **Test thoroughly** - Don't skip validation steps
4. **Teach others** - Best way to solidify learning
5. **Ask questions** - Understanding > completion

## Skills You'll Master

By completing all 7 tags, you'll gain:

**Software Engineering:**
- Command-based framework architecture
- Object-oriented design (inheritance, abstraction)
- State machine design
- Multi-subsystem coordination
- Configuration management

**Control Systems:**
- Open-loop motor control
- Closed-loop PID control
- Swerve drive kinematics
- Path following control
- Vision-based alignment

**Hardware Integration:**
- CTRE Phoenix 6 (TalonFX, CANcoder, Pigeon 2)
- REV Robotics (SparkMax, NEO)
- Limit switches and sensors
- Cameras (PhotonVision)

**Tools & Workflow:**
- WPILib suite (Shuffleboard, Glass)
- CTRE Tuner X
- PathPlanner
- PhotonVision
- Git version control
- Robot simulation

## Next Steps

Ready to start **Tag 4: simple-mechanism**?

1. Review Tag 4 details in `/home/boris/robotics/plan.md`
2. Add intake subsystem to RobotA (SparkMax motor)
3. Add flywheel subsystem to RobotB (TalonFX motor)
4. Configure motor safety and current limits
5. Bind controls to operator buttons
6. Add telemetry for mechanism states

**What to expect:**
- Learn REV SparkMax and CTRE TalonFX motor APIs
- Understand open-loop motor control
- Practice vendor library integration
- See the multi-config architecture in action

**Configuration Notes:**
- Update MAC addresses in `Constants.RobotIdentity` with your actual RoboRIO addresses
- Find MAC address: `ip link` on RoboRIO or check admin panel
- Default robot for simulation/unknown hardware is RobotA

Good luck! 🤖

---

*This is a learning project following a progressive curriculum. For the full detailed plan with implementation specifics, see `/home/boris/robotics/plan.md`.*
