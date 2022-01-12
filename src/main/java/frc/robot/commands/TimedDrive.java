package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveBaseSubsystem;

public class TimedDrive extends CommandBase {
    private final DriveBaseSubsystem m_drivetrain;

    private final Timer m_timer = new Timer();
    private double m_throttle, m_turn, m_timeToRun;

    /**
    * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public TimedDrive(DriveBaseSubsystem subsystem, double throttle, double turn, double timeToRun) {
        m_drivetrain = subsystem;
        m_throttle = throttle;
        m_turn = turn;
        m_timeToRun = timeToRun;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (isFinished()) {
            m_drivetrain.arcadeDrive(0, 0);
        } else {
            m_drivetrain.arcadeDrive(m_throttle, m_turn);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_timer.get() > m_timeToRun;
    }
}
