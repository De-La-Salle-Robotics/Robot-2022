// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.wrappers.PilotFX;

public class ArmSubsystem extends SubsystemBase {
    private final PilotFX m_armMotor = new PilotFX(Arm_Pivot_ID);

    /** Creates a new ExampleSubsystem. */
    public ArmSubsystem() {
        addChild("Arm Pivot", m_armMotor);
    }

    public void close() {
        m_armMotor.close();
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}
}
