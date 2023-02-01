// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {


    public static final class SimulationConstants{
        public static final boolean simulationEnabled = true;
        public static final double speedMultiplier = 0.6;
        public static final double turningSpeedMultiplier = 50;

    }

    public static final class EndEffectorConstants {
        public static final int kEFMotorTopID = 0; //not yet decided
        public static final int kEFMotorBottomID = 1; //not yet decided

        public static final double kIntakeConeOutput = 0.8; //not yet decided, might make separate speeds for motors
        public static final double kIntakeCubeOutput = 0.8; //not yet decided, might make separate speeds for motors
        public static final double kHoldConeOutput = 0.1; //not yet decided
        public static final double kHoldCubeOutput = 0.1; //not yet decided
        public static final double kPlaceConeOutput = -0.8; //not yet decided
        public static final double kPlaceCubeOutput = -0.8; //not yet decided 
    }

    public static final class ArmConstants{
        public static final double stageOne_kP = 0.5; //undecided
        public static final double stageOne_kI = 0; //undecided
        public static final double stageOne_kD = 0; //undecided

        public static final double stageTwo_kP = 0.5; //undecided
        public static final double stageTwo_kI = 0; //undecided
        public static final double stageTwo_kD = 0; //undecided

        public static final double kstageOne_encoderTicksToRadians =  Math.PI * 2;
        public static final double kstageTwo_encoderTicksToRadians = 1 * Math.PI * 2;


        public static final int kStageOne_MotorLeftID = 0; //undecided
        public static final int kStageOne_MotorRightID = 0; //undecided
        public static final int kStageOne_MagEncoderID = 0; //undecided

        public static final double kAbsToRadians = 2.0 * Math.PI;

        public static final boolean kStageOne_AbsEncoderReversed = false; //undecided

        public static final double kStageOne_AbsEncoderInitialOffset = 0; //undecided

        public static final double kStageOne_LimitSwitchAngleRad = 0; //undecided


        public static final int kStageTwo_MotorLeftChannel = 0; //undecided
        public static final int kStageTwo_MotorRightChannel = 1; //undecided

        public static final boolean kStageTwo_AbsEncoderReversed = false; //undecided

        public static final double kStageTwo_AbsEncoderInitialOffset = 0; //undecided

        public static final double kStageTwo_LimitSwitchAngleRad = 0; //undecided

        public static final double[] intakingConesUprightArmPos = {0,0}; //undecided
        public static final double[] intakingConesFallenArmPos = {0,0}; //undecided
        public static final double[] intakingCubesArmPos = {0,0}; //undecided
        public static final double[] holdingArmPos = {0,0}; //undecided
        public static final double[] placingConeArmPosOne = {0,0}; //undecided
        public static final double[] placingConeArmPosTwo = {0,0}; //undecided
        public static final double[] placingConeArmPosThree = {0,0}; //undecided

        public static final double[] placingCubeArmPosOne = {0,0}; //undecided
        public static final double[] placingCubeArmPosTwo = {0,0}; //undecided
        public static final double[] placingCubeArmPosThree = {0,0}; //undecided


        public static final double angleToleranceToUpdateEF = 0.1;
    }


    public static final class ModuleConstants {
        //Physical
        public static final double kWheelDiameterMeters = Units.inchesToMeters(3.8);
        public static final double kDriveMotorGearRatio = 6.75;
        public static final double kTurningMotorGearRatio = 21.4286;

        //Conversions
        public static final double kFalconTicks = 2048;
        public static final double kRadiansToFalcon = kFalconTicks / (2 * Math.PI);
        public static final double kRadiansToTurning = kRadiansToFalcon * kTurningMotorGearRatio;
        public static final double kWheelCircumference = kWheelDiameterMeters * Math.PI;
        public static final double kMetersToRotations = (1 / kWheelCircumference);
        public static final double kMetersToDrive = kMetersToRotations * kDriveMotorGearRatio * kFalconTicks;
        public static final double kMetersToDriveVelocity = kMetersToDrive / 10;
        public static final double kAbsToRadians = 2.0 * Math.PI;

        //Gains
            //Turn
            public static final double kPTurning = 0.21; //0.21 //works from 0.1-0.3 but 0.21 seems to offer low chattering and pretty quick alignment
            //Drive
            public static final double kAFFDrive = 0.015; //0.0151 //0.015
            public static final double kFDrive = .045012; //0.04390375 //0.03751 //.045012
            public static final double kPDrive = 0.02; //0.08 //0.02

        public static final double kNeutralDeadband = 0.01;
    }

    public static final class DriveConstants {

        public static final double kTrackWidth = Units.inchesToMeters(22.5); //need to find
        // Distance between right and left wheels
        public static final double kWheelBase = Units.inchesToMeters(22.5); //need to find


        // Distance between front and back wheels
        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
            new Translation2d(kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
            new Translation2d(-kWheelBase / 2, -kTrackWidth / 2)
        );

        //TBD
        public static final int kFrontLeftDriveMotorPort = 1;
        public static final int kFrontRightDriveMotorPort = 2;
        public static final int kBackLeftDriveMotorPort = 3;
        public static final int kBackRightDriveMotorPort = 4;

        //TBD
        public static final int kFrontLeftTurningMotorPort = 5;
        public static final int kFrontRightTurningMotorPort = 6;
        public static final int kBackLeftTurningMotorPort = 7;
        public static final int kBackRightTurningMotorPort = 8;

        public static final boolean kFrontLeftDriveEncoderReversed = false;
        public static final boolean kFrontRightDriveEncoderReversed = true;
        public static final boolean kBackLeftDriveEncoderReversed = false;
        public static final boolean kBackRightDriveEncoderReversed = true;

        public static final boolean kFrontLeftTurningEncoderReversed = true;
        public static final boolean kFrontRightTurningEncoderReversed = true;
        public static final boolean kBackLeftTurningEncoderReversed = true;
        public static final boolean kBackRightTurningEncoderReversed = true;

        //TBD
        public static final int kFrontLeftDriveAbsoluteEncoderPort = 0;
        public static final int kFrontRightDriveAbsoluteEncoderPort = 1;
        public static final int kBackLeftDriveAbsoluteEncoderPort = 2;
        public static final int kBackRightDriveAbsoluteEncoderPort = 3;

        //sort of calculated
        public static final double kFrontLeftDriveAbsoluteEncoderOffsetRad = -2.29+Math.PI;
        public static final double kFrontRightDriveAbsoluteEncoderOffsetRad = -6.06+Math.PI;
        public static final double kBackLeftDriveAbsoluteEncoderOffsetRad = -2.25;
        public static final double kBackRightDriveAbsoluteEncoderOffsetRad = -0.75;

        public static final boolean kFrontLeftDriveAbsoluteEncoderReversed = false;
        public static final boolean kFrontRightDriveAbsoluteEncoderReversed = false;
        public static final boolean kBackLeftDriveAbsoluteEncoderReversed = false;
        public static final boolean kBackRightDriveAbsoluteEncoderReversed = false;

        public static final double kRadius = Units.inchesToMeters(32/2);

        public static final double kPhysicalMaxSpeedMetersPerSecond = 4.8; //13.3 adjusted, 16.4 free //empirical 4.8 meters when not on ground
        public static final double kPhysicalMaxAngularSpeedRadiansPerSecond = kPhysicalMaxSpeedMetersPerSecond / kRadius;

        public static final double kTeleDriveMaxAccelerationUnitsPerSecond = Units.feetToMeters(100); //10 //100 for testing
        public static final double kTeleDriveMaxAngularAccelerationUnitsPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond * 2 * Math.PI; //idk
        public static final double kTeleDriveMaxSpeedMetersPerSecond = kPhysicalMaxSpeedMetersPerSecond;
        public static final double kTeleDriveMaxAngularSpeedRadiansPerSecond = kPhysicalMaxAngularSpeedRadiansPerSecond;

        public static final double kDegreesToRadians = (2*Math.PI) / 360;

        public static final double kPTurning = 0.0015; //0.0015 low-no oscillation
        public static final double kDTurning = 0.0; //0.0 unnecissary

        public static final double kPFudge = 0.02; //0.2 seems pretty close

        public static final boolean kUseNavXOverPigeon = false;


        public static final double posTolerance = Units.inchesToMeters(0.5);
        public static final double rotationTolerance = 2;

        
        public static final double kGyroZError = 0.674; //.674
        public static final double kGyroMountPosePitch = 0;
        public static final double kGyroMountPoseYaw = 0;
        public static final double kGyroMountPoseRoll = 0;

        public static final double drivePValue = 0.5; //% speed for every meter away from target
        public static final double turnPValue = 1/180.0; //% speed for every degree away from target

        public static final double maxPowerOut = 0.3;
        public static final double maxTurningPowerOut = 0.3;

    }

    public static final class OIConstants {
        public static final int kDriverControllerPort = 0;
        public static final int kOperatorControllerPort = 1;

        public static final int kDriverYAxis = XboxController.Axis.kLeftY.value;
        public static final int kDriverXAxis = XboxController.Axis.kLeftX.value;
        public static final int kDriverRotAxis = XboxController.Axis.kRightX.value;
        public static final int kDriverFieldOrientedButtonIdx = XboxController.Button.kRightBumper.value;

        public static final double kDeadband = 0.06; //0.0275-0.03 //0.06
    }

    public static final class RobotConstants {
        public static final double kMainLoopPeriod = 0.02;
        public static final double kRobotNominalVoltage = 12.0;
    }

    public static final class PathingConstants{
        public static final double kRobotWidth = DriveConstants.kTrackWidth;
        public static final double kRobotLength = DriveConstants.kWheelBase;
        public static final double kRobotRadius = Math.sqrt(Math.pow(kRobotWidth/2,2)+Math.pow(kRobotLength/2,2));

        public static final double maxLineDist = 8.0;
        public static final double lineDistIterator = 1.5;
        public static final double moveAngles = 8;
        public static final int maxRecursionDepth = 2;
        public static final int innerLineTestCount = 12;

        public static final double reachedInBetweenPointThreshold = Units.inchesToMeters(2);

      //  public static final double maxCPUTime = 0.30; //max fraction of thread time to spend on pathing
        public static final int minPathingDelay = 50; //min time to take in ms

        public static final double recalcThreshold = Units.inchesToMeters(2); // max distance to travel before recalculating trajectory



    }

    public static final class VisionConstants {
        public static final double kMaxAmbiguity = 0.2;
        public static final double kCamXOffset = Units.inchesToMeters(6);
        public static final double kCamYOffset = 0;
        public static final double camDirFromCenter = Math.atan2(kCamYOffset,kCamXOffset);
        public static final double camDistFromCenter = Math.sqrt(Math.pow(kCamXOffset,2)+Math.pow(kCamYOffset,2));

    }

    public static final class FieldConstants{
        public static final double width1 = 16.56;
        public static final double height1 = 8.05; // 8.176  //we might need to re-measure some things 😭

        public static final double leftWallPos = -width1/2;
        public static final double rightWallPos = width1/2;
        public static final double bottomWallPos = -height1/2;
        public static final double topWallPos = height1/2;

        public static final double nodesWidth = 1.55;
        public static final double nodesHeight = 5.497;

        public static final double barrierLength = 1.984;

        public static final double chargeStationX = 4.417;
        public static final double chargeStationY = 1.267;
        public static final double chargeStationWidth = 1.931;
        public static final double chargeStationHeight = 2.471;

        public static final double chargeStationFarX = chargeStationX + chargeStationWidth/2; //roughly 5.374
        public static final double chargeStationCloseX = chargeStationX - chargeStationWidth/2;
        public static final double chargeStationTopY = chargeStationY + chargeStationHeight/2;
        public static final double chargeStationBottomY = chargeStationY - chargeStationHeight/2;

        public static final double doubleSubstationDepth = 0.404;

        public static final double distFromFarEdgesToTags = 1.013143;

        public static final double distBetweenTags = 1.676400;

        public static final double centerTagY = 1.234789;

        public static final double aprilTagX = width1/2 - nodesWidth;
        public static final double lonesomeAprilTagY = -2.741613;
        public static final double lonesomeAprilTagX = width1/2 - doubleSubstationDepth;

        public static final double aprilTagOriginX = rightWallPos;
        public static final double aprilTagOriginY = topWallPos;
        public static final double[] aprilTagYDiffsFromOriginInches = {610.77,610.77,610.77,636.96,14.25,40.45,40.45,40.45}; //https://firstfrc.blob.core.windows.net/frc2023/FieldAssets/2023LayoutMarkingDiagram.pdf
        public static final double[] aprilTagXDiffsFromOriginInches = {42.19,108.19,174.19,265.74,265.74,174.19,108.19,42.19};

        public static final double nodeX1 = 7.071332;
        public static final double nodeX2 = 7.426611 + Units.inchesToMeters(1);
        public static final double nodeX3 = nodeX2+0.432133;

        public static final double topNodeY = 3.495389;
        public static final double yDistBetweenNodes = 0.558800;

        public static final double chargeStationEdgeLength = Units.inchesToMeters(6);




    }

}
