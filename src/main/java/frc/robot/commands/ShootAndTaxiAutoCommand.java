// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.HopperSubsystem;

/** An example command that uses an example subsystem. */
public class ShootAndTaxiAutoCommand extends SequentialCommandGroup {
    public ShootAndTaxiAutoCommand(
            DriveBaseSubsystem driveBaseSubsystem, HopperSubsystem hopperSubsystem) {
        addCommands(
                /* If there's any more add them here */
                );
    }
}
