package frc.robot.commands.armcommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.ArmPosition;
import frc.robot.subsystems.ArmSubsystem.IntakeState;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.HopperSubsystem.HopperState;

public class ArmCommands {

    public static Command getArmGoToCollectCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(
                () -> armSubsystem.automaticControl(ArmPosition.Collecting), armSubsystem);
    }

    public static Command getArmGoToIndexCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(
                () -> armSubsystem.automaticControl(ArmPosition.Indexing), armSubsystem);
    }

    public static Command getArmGoToStoreCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(
                () -> armSubsystem.automaticControl(ArmPosition.Stowed), armSubsystem);
    }

    public static Command getArmDoNothingCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(() -> armSubsystem.doNothing(), armSubsystem);
    }

    public static Command getArmRunIntakeCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(() -> armSubsystem.runIntake(IntakeState.Collect), armSubsystem);
    }

    public static Command getArmIndexIntakeCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(() -> armSubsystem.runIntake(IntakeState.Index), armSubsystem);
    }

    public static Command getArmRunOuttakeCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(() -> armSubsystem.runIntake(IntakeState.Spit), armSubsystem);
    }

    public static Command getArmRunIdleCommand(ArmSubsystem armSubsystem) {
        return new InstantCommand(() -> armSubsystem.runIntake(IntakeState.Idle), armSubsystem);
    }

    public static Command getRunHopperCommand(HopperSubsystem hopperSubsystem) {
        return new InstantCommand(() -> hopperSubsystem.runHopper(HopperState.Intake), hopperSubsystem);
    }

    public static Command getArmAutomaticCollectCommand(
            ArmSubsystem armSubsystem, HopperSubsystem hopperSubsystem) {
        /* Execute these commands in sequence */
        return new SequentialCommandGroup(
                /* Do the following */
                new RunCommand(
                                () -> {
                                    armSubsystem.automaticControl(ArmPosition.Collecting);
                                    armSubsystem.runIntake(IntakeState.Collect);
                                },
                                armSubsystem)
                        /* Until we have a ball */
                        .withInterrupt(armSubsystem::hasBall),
                /* Then, move the arm to the index position */
                new RunCommand(
                                () -> {
                                    armSubsystem.automaticControl(ArmPosition.Indexing);
                                    armSubsystem.runIntake(IntakeState.Idle);
                                })
                        /* Until it's at the index position */
                        .withInterrupt(armSubsystem::isIndexed),
                /* Then, run the intake to put the ball in the hopper */
                new RunCommand(
                        () -> {
                            armSubsystem.runIntake(IntakeState.Index);
                            hopperSubsystem.runHopper(HopperState.Intake);
                        },
                        armSubsystem,
                        hopperSubsystem));
    }
}
