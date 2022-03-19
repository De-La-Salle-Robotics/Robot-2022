package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.pilotlib.utils.PlayableSubsystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class PlayMusicCommand extends CommandBase {
    private Orchestra m_orchestra;
    private Supplier<String> m_songNameGetter;
    private boolean m_isFinished;
    private Collection<PlayableSubsystem> m_subsystems;
    /**
    * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public PlayMusicCommand(Supplier<String> songNameGetter, PlayableSubsystem... subsystems) {
        Collection<TalonFX> instruments = new ArrayList<TalonFX>();
        m_subsystems = new ArrayList<PlayableSubsystem>();
        for (PlayableSubsystem system : subsystems) {
            instruments.addAll(system.getPlayableDevices());
            addRequirements(system);
            m_subsystems.add(system);
        }
        m_orchestra = new Orchestra(instruments);
        m_songNameGetter = songNameGetter;
        m_isFinished = false;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_orchestra.loadMusic(m_songNameGetter.get());
        m_orchestra.play();
        m_isFinished = false;
        for(PlayableSubsystem system : m_subsystems) {
            system.beginPlaying();
        }
        System.out.println("Starting to play music");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_orchestra.stop();
        for(PlayableSubsystem system : m_subsystems) {
            system.stopPlaying();
        }
        System.out.println("Stopping music");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_isFinished;
    }
}
