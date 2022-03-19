package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.pilotlib.utils.PlayableSubsystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HopperSubsystem extends SubsystemBase implements PlayableSubsystem {
    public static final double Intake_Speed = 0.5;
    public static final double Outtake_Speed = -0.5;
    public static final double Idle_Speed = 0;

    private PilotFX m_lowerHopper = new PilotFX(Lower_Hopper_ID);
    private PilotFX m_upperHopper = new PilotFX(Upper_Hopper_ID);
    private HopperState m_hopperState = HopperState.Idle;
    private boolean m_isPlaying = false;

    @Override
    public Collection<TalonFX> getPlayableDevices() {
        List<TalonFX> talons = new ArrayList<TalonFX>();
        talons.add(m_lowerHopper);
        talons.add(m_upperHopper);
        return talons;
    }
    @Override
    public void beginPlaying() {
        m_isPlaying = true;
    }
    @Override
    public void stopPlaying() {
        m_isPlaying = false;
    }

    public PilotFX getLowerHopperFx() {
        return m_lowerHopper;
    }

    public PilotFX getUpperHopperFx() {
        return m_upperHopper;
    }

    public enum HopperState {
        Idle,
        Intake,
        Outtake,
    }

    public HopperSubsystem() {
        addChild("Lower Hopper", m_lowerHopper);
        addChild("Upper Hopper", m_upperHopper);
    }

    public void runHopper(HopperState state) {
        m_hopperState = state;
        m_lowerHopper.setInverted(TalonFXInvertType.Clockwise);
        m_upperHopper.setInverted(TalonFXInvertType.CounterClockwise);
    }

    @Override
    public void periodic() {
        if(m_isPlaying) return;
        switch (m_hopperState) {
            case Idle:
                m_lowerHopper.set(Idle_Speed);
                m_upperHopper.set(Idle_Speed);
                break;
            case Intake:
                m_lowerHopper.set(Intake_Speed);
                m_upperHopper.set(Intake_Speed);
                break;
            case Outtake:
                m_lowerHopper.set(Outtake_Speed);
                m_upperHopper.set(Outtake_Speed);
                break;
        }
    }
}
