package frc.robot.subsystems;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;

public class HopperSubsystem extends SubsystemBase {
    private final double Intake_Speed = 1;
    private final double Outtake_Speed = -1;

    private PilotFX m_lowerHopper = new PilotFX(Lower_Hopper_ID);
    private PilotFX m_upperHopper = new PilotFX(Upper_Hopper_ID);
    private HopperState m_hopperState = HopperState.Idle;

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
    }

    @Override
    public void periodic() {
        switch (m_hopperState) {
            case Idle:
                m_lowerHopper.set(0);
                m_upperHopper.set(0);
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
