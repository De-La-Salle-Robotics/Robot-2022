package frc.pilotlib.utils;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.Collection;

public interface PlayableSubsystem extends Subsystem {
    public Collection<TalonFX> getPlayableDevices();

    public void beginPlaying();

    public void stopPlaying();
}
