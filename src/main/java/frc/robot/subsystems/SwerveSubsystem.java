// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.sensors.Pigeon2.AxisDirection;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.RobotConstants;

public class SwerveSubsystem extends SubsystemBase {
  //Modules
    private final SwerveModule frontLeft = new SwerveModule(DriveConstants.kFrontLeftDriveMotorPort, 
                                                            DriveConstants.kFrontLeftTurningMotorPort, 
                                                            DriveConstants.kFrontLeftDriveEncoderReversed, 
                                                            DriveConstants.kFrontLeftTurningEncoderReversed, 
                                                            DriveConstants.kFrontLeftDriveAbsoluteEncoderPort, 
                                                            DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad, 
                                                            DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed);
    private final SwerveModule frontRight = new SwerveModule(DriveConstants.kFrontRightDriveMotorPort, 
                                                            DriveConstants.kFrontRightTurningMotorPort, 
                                                            DriveConstants.kFrontRightDriveEncoderReversed, 
                                                            DriveConstants.kFrontRightTurningEncoderReversed, 
                                                            DriveConstants.kFrontRightDriveAbsoluteEncoderPort, 
                                                            DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad, 
                                                            DriveConstants.kFrontRightDriveAbsoluteEncoderReversed);
    private final SwerveModule backLeft = new SwerveModule(DriveConstants.kBackLeftDriveMotorPort, 
                                                          DriveConstants.kBackLeftTurningMotorPort, 
                                                          DriveConstants.kBackLeftDriveEncoderReversed, 
                                                          DriveConstants.kBackLeftTurningEncoderReversed, 
                                                          DriveConstants.kBackLeftDriveAbsoluteEncoderPort, 
                                                          DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad, 
                                                          DriveConstants.kBackLeftDriveAbsoluteEncoderReversed);
    private final SwerveModule backRight = new SwerveModule(DriveConstants.kBackRightDriveMotorPort, 
                                                            DriveConstants.kBackRightTurningMotorPort, 
                                                            DriveConstants.kBackRightDriveEncoderReversed, 
                                                            DriveConstants.kBackRightTurningEncoderReversed, 
                                                            DriveConstants.kBackRightDriveAbsoluteEncoderPort, 
                                                            DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad, 
                                                            DriveConstants.kBackRightDriveAbsoluteEncoderReversed);
  //Gyro
    private WPI_Pigeon2 gyro = new WPI_Pigeon2(0);

  //Odometry
    private SwerveDrivePoseEstimator odometry;
    private Pose2d initialPose;
    private ChassisSpeeds chassisSpeeds;

  //Values
    static boolean fieldOriented;

  /** Creates a new SwerveSubsystem. */
  public SwerveSubsystem() {
    initialPose = new Pose2d();
    constructOdometry();
        //put in thread so it doesn't stop the rest of our code from running
        new Thread(() -> {
                try {
                        Thread.sleep(1000);
                        gyro.configFactoryDefault();
                        gyro.configMountPose(AxisDirection.NegativeY, AxisDirection.PositiveZ);
                        zeroHeading();
                        Thread.sleep(1000);
                        constructOdometry(); //custructs odometry with newly corrct gyro values
                } catch (Exception e) {
                }
        
        }).start();
        //allows gyro to calibrate for 1 sec before requesting to reset^^
  }

  //Configuration
    public void zeroHeading() { //reset gyroscope to have it set the current direction as the forward direction of field when robot boots up
          gyro.zeroGyroBiasNow();
          gyro.setYaw(0);
    }

    public void constructOdometry() { //constructs odometry object
      odometry = new SwerveDrivePoseEstimator(getRotation2d(), 
      initialPose, 
      DriveConstants.kDriveKinematics, 
      DriveConstants.kSDOdo, 
      DriveConstants.kSDState, 
      DriveConstants.kSDVision, 
      RobotConstants.kMainLoopPeriod);  
    }
  
  //Getters
    //A number equal to x - (y Q), where Q is the quotient of x / y rounded to the nearest integer
    //(if x / y falls halfway between two integers, the even integer is returned)
    public double getHeading() {
          return Math.IEEEremainder(gyro.getYaw(), 360); //clamps value between -/+ 180 deg where zero is forward
    }
    public SwerveModuleState[] getStates() { //module states
      return new SwerveModuleState[] {frontLeft.getState(), frontRight.getState(), backLeft.getState(), backRight.getState()};
    }
    public ChassisSpeeds getChassisSpeeds() { //chassis speeds
      chassisSpeeds = DriveConstants.kDriveKinematics.toChassisSpeeds(getStates());
      return fieldOriented ? 
      ChassisSpeeds.fromFieldRelativeSpeeds(chassisSpeeds.vxMetersPerSecond, chassisSpeeds.vyMetersPerSecond, chassisSpeeds.omegaRadiansPerSecond, getRotation2d()) :
      chassisSpeeds;
    }
    public double getAngularVelocity() {//since wpilib often wants heading in format of Rotation2d//get rotational velocity for closed loop
        return -gyro.getRate() * DriveConstants.kDegreesToRadians;
    }
    public Rotation2d getRotation2d() { //since wpilib often wants heading in format of Rotation2d
      return Rotation2d.fromDegrees(getHeading());
    }

  //Setters 
    public void fieldOriented(boolean isFieldOriented) {
      fieldOriented = isFieldOriented;
    }
    public void setModuleStates(SwerveModuleState[] desiredStates) {
      SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
      //normalizes wheel speeds in case max speed reached^^
      frontLeft.setDesiredState(desiredStates[0]);
      frontRight.setDesiredState(desiredStates[1]);
      backLeft.setDesiredState(desiredStates[2]);
      backRight.setDesiredState(desiredStates[3]);
    }

    public void stopModules() {
      frontLeft.stop();
      frontRight.stop();
      backLeft.stop();
      backRight.stop();
    }

    public void updateOdometry() {
      odometry.updateWithTime(Timer.getFPGATimestamp(), getRotation2d(), getStates());
      //debug output: SmartDashboard.putNumber("OdoH", odometry.getEstimatedPosition().getRotation().getDegrees());
      //debug output: SmartDashboard.putNumber("gyroH", getHeading());
      //debug output: SmartDashboard.putNumber("OdoY", Units.metersToFeet(odometry.getEstimatedPosition().getY()));
      //debug output: SmartDashboard.putNumber("OdoX", Units.metersToFeet(odometry.getEstimatedPosition().getX()));      
    }

    public void updateOdometryVision(Pose2d visionRobotPoseMeters, double timestampSeconds, Vector<N3> visionMeasurementStdDevs) {
      odometry.addVisionMeasurement(visionRobotPoseMeters, timestampSeconds, visionMeasurementStdDevs);
    }

    //Dashboard
    public void debugOutputs() {
      //debug output: SmartDashboard.putNumber("CSH", getChassisSpeeds().omegaRadiansPerSecond);
      //debug output: SmartDashboard.putNumber("CSY", getChassisSpeeds().vyMetersPerSecond);
      //debug output: SmartDashboard.putNumber("CSX", getChassisSpeeds().vxMetersPerSecond);
    }

    @Override
    public void periodic() {
      //update odometry
        updateOdometry();
      //dashboard outputs
        debugOutputs();
    }        
}