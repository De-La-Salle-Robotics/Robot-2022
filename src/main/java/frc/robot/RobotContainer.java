// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ArmCommands.ArmManualCommand;
import frc.robot.commands.TaxiAutoCommand;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveBaseSubsystem;

;

/**
* This class is where the bulk of the robot should be declared. Since Command-based is a
* "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
* periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
* subsystems, commands, and button mappings) should be declared here.
*/
public class RobotContainer {
    /* Controllers are created here */
    private final XboxController m_driverController = new XboxController(Driver_Controller_Port);
    private final XboxController m_operatorController = new XboxController(Operator_Controller_Port);

    /* Subsystems are created here */
    private final DriveBaseSubsystem m_driveBaseSubsystem = new DriveBaseSubsystem();
    private final ArmSubsystem m_armSubsystem = new ArmSubsystem();

    /* Commands are created here */
    private final TaxiAutoCommand m_defaultAutoCommand = new TaxiAutoCommand(m_driveBaseSubsystem);
    private final TeleopDriveCommand m_teleopDrive =
            new TeleopDriveCommand(
                    m_driveBaseSubsystem, m_driverController::getLeftY, m_driverController::getRightX);
    private final ArmManualCommand m_armManualCommand =
            new ArmManualCommand(m_armSubsystem, m_operatorController::getLeftY);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        m_driveBaseSubsystem.setDefaultCommand(m_teleopDrive);
        m_armSubsystem.setDefaultCommand(m_armManualCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    private void configureButtonBindings() {}

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
