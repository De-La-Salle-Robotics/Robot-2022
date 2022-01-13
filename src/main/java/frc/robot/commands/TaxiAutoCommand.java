// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveBaseSubsystem;

/** An example command that uses an example subsystem. */
public class TaxiAutoCommand extends SequentialCommandGroup {
    public TaxiAutoCommand(DriveBaseSubsystem driveBaseSubsystem) {
        addCommands(
                /* Drive straight at half power for 1 second */
                new TimedDrive(driveBaseSubsystem, 0.5, 0, 1.0)
                /* If there's any more add them here */
                );
    }
}
