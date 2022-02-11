package frc.robot.commands.armcommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import java.util.function.DoubleSupplier;

public class ArmManualCommand extends CommandBase {
    private ArmSubsystem m_armSubsystem;
    private DoubleSupplier m_manualPower;
    private boolean m_isFinished;

    public ArmManualCommand(ArmSubsystem subsystem, DoubleSupplier manualPower) {
        m_armSubsystem = subsystem;
        m_manualPower = manualPower;
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
        m_armSubsystem.manualControl(m_manualPower.getAsDouble());
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
