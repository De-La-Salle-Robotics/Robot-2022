package frc.robot.commands.ArmCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.ArmPosition;

public class ArmStowCommand extends CommandBase {
    private ArmSubsystem m_armSubsystem;
    private boolean m_isFinished;

    public ArmStowCommand(ArmSubsystem subsystem) {
        m_armSubsystem = subsystem;
        m_isFinished = false;

        addRequirements(subsystem);
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_isFinished = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_armSubsystem.automaticControl(ArmPosition.Stowed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_isFinished = true;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isFinished;
    }
}
