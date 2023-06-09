// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Constants.OIConstants;
import frc.robot.lib.MB_AutoCommandChooser;
import frc.robot.subsystems.arm.Grabber;
import frc.robot.subsystems.arm.StageOneSub;
import frc.robot.subsystems.arm.StageTwoSub;
import frc.robot.subsystems.nav.*;
import frc.robot.subsystems.state.FMSGetter;
import frc.robot.subsystems.drive.SwerveSubsystem;
import frc.robot.subsystems.arm.EFSub;
import frc.robot.subsystems.state.StateControllerSubsystem;

/**
 * This class is where the bulk of the robot should be declared. ABSOLUTELY! REBOUND WITH JOSH 1 G!
 * IT'S TIME TO MOVE ON FROM IAN LA MONT AND TO BE HAPPY AGAIN!
 * HOOK UP WITH JOSH 1 G AND HAVE SOME FUN! IT'S TIME TO SHOW IAN LA MONT THAT YOU'RE BETTER OFF WITHOUT HIM AND TO FORGET ABOUT HIM!
 * DO WHATEVER IT TAKES TO BE HAPPY AND TO MOVE ON FROM THIS RELATIONSHIP!
 */
public class RobotContainer {
  private final FMSGetter fmsGetter = new FMSGetter();
  private final StateControllerSubsystem stateControllerSubsystem = new StateControllerSubsystem(fmsGetter);
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem(stateControllerSubsystem);
    private final GraphicalTelemetrySubsystem pathingTelemSub = new PathingTelemetrySub(stateControllerSubsystem);
    private final EFPathingTelemetrySub efTelemSub = new EFPathingTelemetrySub();
    private final EFNavSystem efNavSystem = new EFNavSystem(efTelemSub);
  private final NavigationField navigationField = new NavigationField((PathingTelemetrySub) pathingTelemSub, swerveSubsystem, fmsGetter,stateControllerSubsystem);
  private final VisionSubsystem visionSubsystem = new VisionSubsystem(swerveSubsystem, (PathingTelemetrySub) pathingTelemSub);
  private final EFSub endEffectorSubsystem = new EFSub();

  private final StageOneSub stageOneSub = new StageOneSub();
  private final StageTwoSub stageTwoSub = new StageTwoSub();
  private final EFSub efSub = new EFSub();
  private final Grabber grabberSubsystem = new Grabber(stageOneSub, stageTwoSub, efSub, stateControllerSubsystem, efNavSystem,efTelemSub);

    private final MB_AutoCommandChooser autoChooser = new MB_AutoCommandChooser(navigationField,swerveSubsystem,stateControllerSubsystem,grabberSubsystem);

  private final XboxController driverController = new XboxController(OIConstants.DRIVER_CONTROLLER_PORT);
  private final XboxController operatorController = new XboxController(OIConstants.OPERATOR_CONTROLLER_PORT);
    private final Command rc_drive = new RunCommand(()-> swerveSubsystem.joystickDrive(driverController.getLeftY()*-1,driverController.getLeftX()*-1,driverController.getRightX()*-1), swerveSubsystem);
  private final Command stateController_processInputs = new RunCommand(()-> stateControllerSubsystem.processRawAxisValues(operatorController.getPOV(), operatorController.getRightTriggerAxis()),stateControllerSubsystem);
    private final Command rc_XPattern = new InstantCommand(()-> swerveSubsystem.drivebaseXPattern(), swerveSubsystem);

    private final Command swerve_toggleFieldOriented = new InstantCommand(swerveSubsystem::toggleFieldOriented);
 // private final Command rc_goToTag = new RunCommand(()->swerveSubsystem.drive(visionSubsystem.getDesiredSpeeds()[0],visionSubsystem.getDesiredSpeeds()[1],visionSubsystem.getDesiredSpeeds()[2],true,false), swerveSubsystem);
 // private final Command rc_goToPose = new RunCommand(()->swerveSubsystem.driveToPose(new Pose2d()), swerveSubsystem);
  private final Command rc_generateNavPoses = new InstantCommand(()->navigationField.setNavPoint(new Pose2d(2.5,0,new Rotation2d())));
  private final Command rc_navToPose = new RunCommand(()->swerveSubsystem.driveToPose(navigationField.getNextNavPoint(), Constants.DriveConstants.drivePValue, Constants.DriveConstants.turnPValue),swerveSubsystem);
  private final Command rc_directToClosestNode = new RunCommand(()->swerveSubsystem.driveToPose(navigationField.getClosestSetpoint(), 2, Constants.DriveConstants.turnPValue,Constants.DriveConstants.maxPowerOutForAssist,Constants.DriveConstants.maxTurningPowerOutForAssist),swerveSubsystem);
  private final Command rc_directToPose = new RunCommand(()->swerveSubsystem.driveToPose(navigationField.getDesiredPose(), Constants.DriveConstants.drivePValue, Constants.DriveConstants.turnPValue),swerveSubsystem);
  private final Command swerve_resetPose = new InstantCommand(swerveSubsystem::resetPose);
  private final Command rc_autoBalance = new RunCommand(()->swerveSubsystem.driveAutoBalance(),swerveSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    swerveSubsystem.setDefaultCommand(rc_drive);
    stateControllerSubsystem.setDefaultCommand(stateController_processInputs);

    //pathingTelemSub.setDefaultCommand(new RunCommand(()->pathingTelemSub.periodic(),pathingTelemSub));
  //  (PathingTelemetrySub)pathingTelemSub.set

    pathingTelemSub.init();
    efTelemSub.init();
    // Configure the button bindings
    configureButtonBindings();
      //Trigger stageOneSwitchLeadingEdge = new Trigger(()->(stageOneSub.getLimitSwitchValue())); //&& stageOneSub.getVelocity() > 0));
      //Trigger stageOneSwitchTrailingEdge = new Trigger(()->(stageOneSub.getLimitSwitchValue() && stageOneSub.getVelocity() < 0));
      //Trigger stageTwoSwitchLeadingEdge = new Trigger(()->(stageTwoSub.getLimitSwitchValue())); //&& stageTwoSub.getVelocity() > 0));
      //Trigger stageTwoSwitchTrailingEdge = new Trigger(()->(stageTwoSub.getLimitSwitchValue() && stageTwoSub.getVelocity() < 0));

      //stageOneSwitchLeadingEdge.onTrue(new InstantCommand(()->stageOneSub.setSensorPosition(Constants.ArmConstants.stageOneLimitSwitchLeadingAngle)));
      //stageOneSwitchTrailingEdge.onTrue(new InstantCommand(()->stageTwoSub.setSensorPosition(Constants.ArmConstants.stageOneLimitSwitchTrailingAngle)));
      //stageTwoSwitchLeadingEdge.onTrue(new InstantCommand(()->stageTwoSub.setSensorPosition(Constants.ArmConstants.stageTwoLimitSwitchLeadingAngle)));
      //stageTwoSwitchTrailingEdge.onTrue(new InstantCommand(()->stageTwoSub.setSensorPosition(Constants.ArmConstants.stageTwoLimitSwitchTrailingAngle)));

    //inputs
//    SmartDashboard.putNumber("x1",0);
//    SmartDashboard.putNumber("y1",0);
//    SmartDashboard.putNumber("x2",0);
//    SmartDashboard.putNumber("y2",0);


    SmartDashboard.putData("Auto Chooser",autoChooser.getAutoChooser());
      SmartDashboard.putBoolean("higherMaxOutput",false); //delete me after debugging


  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private POVButton[] dpad = new POVButton[]{new POVButton(operatorController,0),new POVButton(operatorController,90),new POVButton(operatorController,180),new POVButton(operatorController,270)};
  private void configureButtonBindings() {
    new JoystickButton(driverController, XboxController.Button.kBack.value).whenPressed(() -> swerveSubsystem.zeroHeading());
    new JoystickButton(driverController, XboxController.Button.kStart.value).whenPressed(swerve_toggleFieldOriented);
    new JoystickButton(driverController,XboxController.Button.kB.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToPlacing));
    new JoystickButton(driverController,XboxController.Button.kX.value).whileTrue(rc_autoBalance);


    //new JoystickButton(driverController, XboxController.Button.kRightBumper.value).whileTrue(rc_directToPose);
      new JoystickButton(driverController, XboxController.Button.kA.value).whileTrue(rc_directToClosestNode);
    //  new JoystickButton(driverController, XboxController.Button.kY.value).whileTrue(rc_navToPose);
    new JoystickButton(driverController, XboxController.Button.kY.value).onTrue(new InstantCommand(navigationField::engageNav));
    new JoystickButton(driverController, XboxController.Button.kY.value).onFalse(new InstantCommand(navigationField::disengageNav));

    new JoystickButton(operatorController, XboxController.Button.kLeftBumper.value).onTrue(new InstantCommand(stateControllerSubsystem::itemCubeButton));
      new JoystickButton(operatorController,XboxController.Button.kRightBumper.value).onTrue(new InstantCommand(stateControllerSubsystem::itemConeUprightButton));

      new JoystickButton(operatorController,XboxController.Button.kA.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToIntake));
      new JoystickButton(operatorController,XboxController.Button.kB.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToHolding));

      new JoystickButton(driverController,XboxController.Button.kRightBumper.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToIntake));
      new JoystickButton(driverController,XboxController.Button.kLeftBumper.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToHolding));

      new JoystickButton(operatorController,XboxController.Button.kY.value).onTrue(new InstantCommand(stateControllerSubsystem::setAgArmToPlacing));
      new JoystickButton(operatorController,XboxController.Button.kX.value).onTrue(new InstantCommand(grabberSubsystem::overrideDesiredEFWait));

    new JoystickButton(driverController,XboxController.Button.kY.value).onTrue(rc_XPattern);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return autoChooser.getAutoChooser().getSelected();
  }
}
