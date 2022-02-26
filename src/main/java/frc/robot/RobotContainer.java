// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.pilotlib.controllerwrappers.DriverController;
import frc.pilotlib.controllerwrappers.OperatorController;
import frc.robot.commands.TaxiAutoCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.commands.armcommands.ArmCommands;
import frc.robot.commands.armcommands.ArmManualCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.IntakeState;
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
    private final ArmManualCommand m_armManualCommand =
            new ArmManualCommand(m_armSubsystem, m_operatorController.getAxis(Manual_Arm_Axis));

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
                .whenPressed(ArmCommands.getArmGoToStoreCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Index_Button)
                .whenPressed(ArmCommands.getArmGoToIndexCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Collect_Button)
                .whenPressed(ArmCommands.getArmGoToCollectCommand(m_armSubsystem));
        m_operatorController
                .getButton(Operator_Collect_Button)
                .whileHeld(ArmCommands.getArmAutomaticCollectCommand(m_armSubsystem, m_hopperSubsystem))
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
