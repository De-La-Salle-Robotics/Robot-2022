package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;
import java.util.function.DoubleSupplier;

public class TeleopDrive extends CommandBase {
    private final DriveBaseSubsystem m_drivetrain;
    private final DoubleSupplier m_throttle;
    private final DoubleSupplier m_turn;

    /**
    * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public TeleopDrive(DriveBaseSubsystem subsystem, DoubleSupplier throttle, DoubleSupplier turn) {
        m_drivetrain = subsystem;
        m_throttle = throttle;
        m_turn = turn;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_drivetrain.arcadeDrive(m_throttle.getAsDouble(), m_turn.getAsDouble());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
