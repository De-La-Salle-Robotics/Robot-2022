// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotCoder;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.pilotlib.utils.RangeUtils;
import frc.pilotlib.wpiwrappers.PilotDigitalInput;
import frc.robot.configurations.ArmConfiguration;

public class ArmSubsystem extends SubsystemBase {
    public static final double Collect_Power = 0.675;
    public static final double Spit_Power = -0.5;
    public static final double Idle_Power = 0;
    public static final double Angle_Threshold = 4;

    private final PilotFX m_armMotor = new PilotFX(Arm_Pivot_ID);
    private final PilotFX m_intakeMotor1 = new PilotFX(Arm_Intake1_ID);
    private final PilotFX m_intakeMotor2 = new PilotFX(Arm_Intake2_ID);
    private final PilotCoder m_armCanCoder = new PilotCoder(Arm_Cancoder_ID);

    private final PilotDigitalInput m_ballDetectInput =
            new PilotDigitalInput(Ball_Detect_Input_ID, true);

    public PilotFX getArmMotor() {
        return m_armMotor;
    }

    public PilotFX getIntakeMotor1() {
        return m_intakeMotor1;
    }

    public PilotFX getIntakeMotor2() {
        return m_intakeMotor2;
    }

    public PilotCoder getArmCanCoder() {
        return m_armCanCoder;
    }

    public PilotDigitalInput getBallDetectInput() {
        return m_ballDetectInput;
    }

    private ArmState m_currentState;
    private ArmPosition m_currentPosition;
    private double m_manualPower;
    private IntakeState m_intakeState;

    private enum ArmState {
        Manual, // The arm is controlled manually through manualControl
        Automatic, // The arm is controlled through automaticControl
    }

    public enum ArmPosition {
        Stowed,
        Indexing,
        Collecting,
    }

    public enum IntakeState {
        Collect,
        Spit,
        Idle,
    }

    /** Creates a new ExampleSubsystem. */
    public ArmSubsystem() {
        addChild("Arm Pivot", m_armMotor);
        addChild("Intake Motor 1", m_intakeMotor1);
        addChild("Intake Motor 2", m_intakeMotor2);
        addChild("Arm Cancoder", m_armCanCoder);
        addChild("Intake Ball Detector", m_ballDetectInput);

        ArmConfiguration.configure(m_armMotor, m_intakeMotor1, m_intakeMotor2, m_armCanCoder);

        m_currentState = ArmState.Manual;
        m_intakeState = IntakeState.Idle;
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

    public void runIntake(IntakeState intakeState) {
        m_intakeState = intakeState;
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
        switch (m_intakeState) {
            case Collect:
                m_intakeMotor1.set(ControlMode.PercentOutput, Collect_Power);
                m_intakeMotor2.set(ControlMode.PercentOutput, Collect_Power);
                break;
            case Spit:
                m_intakeMotor1.set(ControlMode.PercentOutput, Spit_Power);
                m_intakeMotor2.set(ControlMode.PercentOutput, Spit_Power);
                break;
            case Idle:
                m_intakeMotor1.set(ControlMode.PercentOutput, Idle_Power);
                m_intakeMotor2.set(ControlMode.PercentOutput, Idle_Power);
                break;
        }
    }

    public boolean hasBall() {
        return m_ballDetectInput.get() == true;
    }

    public boolean isIndexed() {
        return RangeUtils.isInRange(m_armCanCoder.getPosition(), Indexing_Position, Angle_Threshold);
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
