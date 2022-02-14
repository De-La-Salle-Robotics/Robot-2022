// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.pilotlib.controllerwrappers.DriverController;
import frc.pilotlib.controllerwrappers.OperatorController;
import frc.robot.commands.TaxiAutoCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.commands.armcommands.ArmGoToCollectCommand;
import frc.robot.commands.armcommands.ArmGoToIndexCommand;
import frc.robot.commands.armcommands.ArmGoToStowCommand;
import frc.robot.commands.armcommands.ArmManualCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.ArmPosition;
import frc.robot.subsystems.ArmSubsystem.IntakeState;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.HopperSubsystem.HopperState;

;

/**
* This class is where the bulk of the robot should be declared. Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
* subsystems, commands, and button mappings) should be declared here.
*/
public class RobotContainer {
    /* Controllers are created here */
    private final DriverController m_driverController = new DriverController(Driver_Controller_Port);
    private final OperatorController m_operatorController =
            new OperatorController(Operator_Controller_Port);

    /* Subsystems are created here */
    private final DriveBaseSubsystem m_driveBaseSubsystem = new DriveBaseSubsystem();
    private final ArmSubsystem m_armSubsystem = new ArmSubsystem();
    private final HopperSubsystem m_hopperSubsystem = new HopperSubsystem();

    /* Commands are created here */
    private final TaxiAutoCommand m_defaultAutoCommand = new TaxiAutoCommand(m_driveBaseSubsystem);
    private final TeleopDriveCommand m_teleopDrive =
            new TeleopDriveCommand(
                    m_driveBaseSubsystem,
                    m_driverController.getAxis(Throttle_Axis),
                    m_driverController.getAxis(Wheel_Axis));
    private final ArmManualCommand m_armManualCommand =
            new ArmManualCommand(m_armSubsystem, m_operatorController.getAxis(Manual_Arm_Axis));

    private final Command m_automaticCollectBalls =
            /* Execute these commands in sequence */
            new SequentialCommandGroup(
                            /* Execute these in parallel */
                            new ParallelCommandGroup(
                                            /* Put the arm in the collect position */
                                            new RunCommand(
                                                    () -> m_armSubsystem.automaticControl(ArmPosition.Collecting),
                                                    m_armSubsystem),
                                            /* And Run the intake to collect balls */
                                            new RunCommand(
                                                    () -> m_armSubsystem.runIntake(IntakeState.Collect), m_armSubsystem))
                                    /* Until we have a ball */
                                    .withInterrupt(m_armSubsystem::hasBall),
                            /* Then, move the arm to the index position */
                            new RunCommand(
                                            () -> m_armSubsystem.automaticControl(ArmPosition.Indexing), m_armSubsystem)
                                    /* Until it's at the index position */
                                    .withInterrupt(m_armSubsystem::isIndexed),
                            /* Then, run the intake to put the ball in the hopper */
                            new ParallelCommandGroup(
                                            new RunCommand(
                                                    () -> m_armSubsystem.runIntake(IntakeState.Collect), m_armSubsystem),
                                            new RunCommand(
                                                    () -> m_hopperSubsystem.runHopper(HopperState.Intake), m_hopperSubsystem)
                                            )
                                    /* For a second */
                                    .withTimeout(1),
                            /* Then stop the hopper */
                            new InstantCommand(() -> m_hopperSubsystem.runHopper(HopperState.Idle), m_hopperSubsystem))
                    /* Once it's done, repeat it */
                    .perpetually();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        m_driveBaseSubsystem.setDefaultCommand(m_teleopDrive);
        m_armSubsystem.setDefaultCommand(m_armManualCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        /* Bind the arm buttons */
        m_operatorController
                .getButton(Operator_Stow_Button)
                .whenPressed(new ArmGoToStowCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Index_Button)
                .whenPressed(new ArmGoToIndexCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Collect_Button)
                .whenPressed(new ArmGoToCollectCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Collect_Button)
                .whileHeld(m_automaticCollectBalls)
                .whenReleased(() -> m_armSubsystem.runIntake(IntakeState.Idle));
    }

    /**
    * Use this to pass the autonomous command to the main {@link Robot} class.
    *
    * @return the command to run in autonomous
    */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return m_defaultAutoCommand;
    }
}
