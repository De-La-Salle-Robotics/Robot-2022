package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;

public class HopperSubsystem extends SubsystemBase {
    public static final double Intake_Speed = 0.6;
    public static final double Outtake_Speed = -0.5;
    public static final double Idle_Speed = 0;

    private PilotFX m_lowerHopper = new PilotFX(Lower_Hopper_ID);
    private PilotFX m_upperHopper = new PilotFX(Upper_Hopper_ID);
    private HopperState m_hopperState = HopperState.Idle;

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
