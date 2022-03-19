package frc.robot.commands.hoppercommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.HopperSubsystem.HopperState;

public class HopperCommands {

    public static Command getHopperIntakeCommand(HopperSubsystem hopperSubsystem) {
        return new InstantCommand(() -> hopperSubsystem.runHopper(HopperState.Intake), hopperSubsystem);
    }

    public static Command getHopperOuttakeCommand(HopperSubsystem hopperSubsystem) {
        return new InstantCommand(
                () -> hopperSubsystem.runHopper(HopperState.Outtake), hopperSubsystem);
    }

    public static Command getHopperIdleCommand(HopperSubsystem hopperSubsystem) {
        return new InstantCommand(() -> hopperSubsystem.runHopper(HopperState.Idle), hopperSubsystem);
    }
}
