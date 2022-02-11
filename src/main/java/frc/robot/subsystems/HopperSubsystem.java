package frc.robot.subsystems;

import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;

public class HopperSubsystem extends SubsystemBase {
    PilotFX m_lowerHopper = new PilotFX(Lower_Hopper_ID);
    PilotFX m_upperHopper = new PilotFX(Upper_Hopper_ID);

    public HopperSubsystem() {
        addChild("Lower Hopper", m_lowerHopper);
        addChild("Upper Hopper", m_upperHopper);
    }

    @Override
    public void periodic() {
    }
}
