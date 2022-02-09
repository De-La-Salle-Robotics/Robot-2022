// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotCoder;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.robot.configurations.ArmConfiguration;

public class ArmSubsystem extends SubsystemBase {
    private final PilotFX m_armMotor = new PilotFX(Arm_Pivot_ID);
    private final PilotCoder m_armCanCoder = new PilotCoder(Arm_Cancoder_ID);

    public PilotFX getArmMotor() {
        return m_armMotor;
    }

    public PilotCoder getArmCanCoder() {
        return m_armCanCoder;
    }

    private ArmState m_currentState;
    private ArmPosition m_currentPosition;
    private double m_manualPower;

    private enum ArmState {
        Manual, // The arm is controlled manually through manualControl
        Automatic, // The arm is controlled through automaticControl
    }

    public enum ArmPosition {
        Stowed,
        Indexing,
        Collecting,
    }

    /** Creates a new ExampleSubsystem. */
    public ArmSubsystem() {
        addChild("Arm Pivot", m_armMotor);
        addChild("Arm Cancoder", m_armCanCoder);

        ArmConfiguration.configure(m_armMotor, m_armCanCoder);

        m_currentState = ArmState.Manual;
    }

    public void close() {
        m_armMotor.close();
    }

    public void manualControl(double armPower) {
        m_currentState = ArmState.Manual;
        m_manualPower = armPower;
    }

    public void automaticControl(ArmPosition armPosition) {
        m_currentState = ArmState.Automatic;
        m_currentPosition = armPosition;
    }

    @Override
    public void periodic() {
        switch (m_currentState) {
            case Manual:
                m_armMotor.set(ControlMode.PercentOutput, m_manualPower);
                break;
            case Automatic:
                switch (m_currentPosition) {
                    case Stowed:
                        m_armMotor.set(ControlMode.Position, angleToNative(Stowed_Position));
                        break;
                    case Indexing:
                        m_armMotor.set(ControlMode.Position, angleToNative(Indexing_Position));
                        break;
                    case Collecting:
                        m_armMotor.set(ControlMode.Position, angleToNative(Collecting_Position));
                        break;
                }
                break;
        }
    }

    @Override
    public void simulationPeriodic() {}

    public static double angleToNative(double armAngle) {
        /* Convert from degrees to rotations */
        double cancoderRots = armAngle / 360.0;
        /* Apply gear ratio */
        return PilotFX.toRawUnits(cancoderRots * Arm_Gearbox_Ratio);
    }
}
