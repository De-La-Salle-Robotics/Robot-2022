package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class TeleopDriveCommand extends CommandBase {
    private final double Slowdown_Ratio = 0.25;

    private final DriveBaseSubsystem m_drivetrain;
    private final DoubleSupplier m_throttle;
    private final DoubleSupplier m_turn;
    private final BooleanSupplier m_slowdown;

    /**
    * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public TeleopDriveCommand(
            DriveBaseSubsystem subsystem,
            DoubleSupplier throttle,
            DoubleSupplier turn,
            BooleanSupplier slowdownButton) {
        m_drivetrain = subsystem;
        m_throttle = throttle;
        m_turn = turn;
        m_slowdown = slowdownButton;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double throttle = m_throttle.getAsDouble();
        double turn = m_turn.getAsDouble();
        turn *= 0.65;
        throttle *= 0.75;

        if (m_slowdown.getAsBoolean()) {
            throttle *= Slowdown_Ratio;
            turn *= Slowdown_Ratio;
        }
        m_drivetrain.arcadeDrive(throttle, turn);
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
