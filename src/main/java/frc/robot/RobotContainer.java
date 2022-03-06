// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.pilotlib.controllerwrappers.DriverController;
import frc.pilotlib.controllerwrappers.OperatorController;
import frc.pilotlib.utils.OverrideableCommand;
import frc.robot.commands.TaxiAutoCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.commands.armcommands.ArmCommands;
import frc.robot.commands.armcommands.ArmManualCommand;
import frc.robot.commands.hoppercommands.HopperCommands;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.HopperSubsystem;

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
    private final Command m_defaultArmCommand =
            new OverrideableCommand(
                    ArmCommands.getArmDoNothingCommand(m_armSubsystem),
                    m_armSubsystem,
                    new OverrideableCommand.TriggerCommandPair(
                            m_operatorController.getThreshold(Manual_Arm_Axis, 0.1, true),
                            new ArmManualCommand(m_armSubsystem, m_operatorController.getAxis(Manual_Arm_Axis))),
                    new OverrideableCommand.TriggerCommandPair(
                            m_operatorController.getButtonSupplier(Operator_Stow_Button),
                            ArmCommands.getArmGoToStoreCommand(m_armSubsystem)),
                    new OverrideableCommand.TriggerCommandPair(
                            m_operatorController.getButtonSupplier(Operator_Index_Button),
                            ArmCommands.getArmGoToIndexCommand(m_armSubsystem)),
                    new OverrideableCommand.TriggerCommandPair(
                            m_operatorController.getButtonSupplier(Operator_Collect_Button),
                            ArmCommands.getArmGoToCollectCommand(m_armSubsystem)));

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        m_driveBaseSubsystem.setDefaultCommand(m_teleopDrive);
        m_armSubsystem.setDefaultCommand(m_defaultArmCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        /* Bind the arm buttons */

        Trigger intakeButton = m_operatorController.getButton(Operator_Intake_Hopper_Button);
        Trigger outtakeButton = m_operatorController.getButton(Operator_Outtake_Hopper_Button);
        Trigger automaticTrigger = m_operatorController.getButton(Operator_Automatic_Collect_Button);
        Trigger manualIntake = m_operatorController.getButton(Operator_Intake_Manual_Intake_Button);
        Trigger manualOuttake = m_operatorController.getButton(Operator_Intake_Manual_Outtake_Button);
        automaticTrigger.whenActive(
                ArmCommands.getArmAutomaticCollectCommand(m_armSubsystem, m_hopperSubsystem));
        intakeButton
                .negate()
                .and(outtakeButton.negate())
                .and(automaticTrigger.negate())
                .whenActive(HopperCommands.getHopperIdleCommand(m_hopperSubsystem));
        intakeButton
                .and(outtakeButton.negate())
                .and(automaticTrigger.negate())
                .whenActive(HopperCommands.getHopperIntakeCommand(m_hopperSubsystem));
        intakeButton
                .negate()
                .and(outtakeButton)
                .and(automaticTrigger.negate())
                .whenActive(HopperCommands.getHopperOuttakeCommand(m_hopperSubsystem));

        manualIntake
                .and(manualOuttake.negate())
                .and(automaticTrigger.negate())
                .whenActive(ArmCommands.getArmRunIntakeCommand(m_armSubsystem));
        manualIntake
                .negate()
                .and(manualOuttake)
                .and(automaticTrigger.negate())
                .whenActive(ArmCommands.getArmRunOuttakeCommand(m_armSubsystem));
        manualIntake
                .negate()
                .and(manualOuttake.negate())
                .and(automaticTrigger.negate())
                .whenActive(ArmCommands.getArmRunIdleCommand(m_armSubsystem));
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
