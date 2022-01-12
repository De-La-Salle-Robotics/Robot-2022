// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.configurations.DriveTrainConfiguration;
import frc.robot.wrappers.PilotFX;
import frc.robot.wrappers.PilotPigeon;

public class DriveBaseSubsystem extends SubsystemBase {
    private final PilotFX m_rightLeader = new PilotFX(Constants.Right_Leader_ID);
    private final PilotFX m_leftLeader = new PilotFX(Constants.Left_Leader_ID);
    private final PilotFX m_rightFollower = new PilotFX(Constants.Right_Follower_ID);
    private final PilotFX m_leftFollower = new PilotFX(Constants.Left_Follower_ID);
    private final PilotPigeon m_pigeon = new PilotPigeon(Constants.Pigeon_ID);

    /** Creates a new ExampleSubsystem. */
    public DriveBaseSubsystem() {
        addChild("Right Leader", m_rightLeader);
        addChild("Left Leader", m_leftLeader);
        addChild("Right Follower", m_rightFollower);
        addChild("Left Follower", m_leftFollower);
        addChild("Chassis IMU", m_pigeon);

        DriveTrainConfiguration.configure(m_rightLeader, m_rightFollower);
        DriveTrainConfiguration.configure(m_leftLeader, m_leftFollower);
    }

    public void arcadeDrive(double throttle, double turn) {
        m_rightLeader.set(ControlMode.PercentOutput, throttle + turn);
        m_leftLeader.set(ControlMode.PercentOutput, throttle - turn);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
