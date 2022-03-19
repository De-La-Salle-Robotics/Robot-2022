// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.robot.configurations.ClimbConfiguration;

public class ClimbSubsystem extends SubsystemBase {
    public static final double Winch_Power = 1;
    public static final double Climb_Power = 1.0;
    public static final double Unwinch_Power = -1;
    public static final double Idle_Power = 0;

    private final PilotFX m_winchMotor = new PilotFX(Winch_ID);
    private final WPI_VictorSPX m_climbMotor = new WPI_VictorSPX(Climb_ID);

    public PilotFX getWinchMotor() {
        return m_winchMotor;
    }

    public WPI_VictorSPX getClimbMotor() {
        return m_climbMotor;
    }

    private ClimbState m_currentState = ClimbState.Idle;

    public enum ClimbState {
        Winching,
        Unwinching,
        Climbing,
        Idle,
    }

    /** Creates a new ExampleSubsystem. */
    public ClimbSubsystem() {
        addChild("Winch Motor", m_winchMotor);
        addChild("Climb Motor", m_climbMotor);

        ClimbConfiguration.configure(m_winchMotor, m_climbMotor);
    }

    public void setClimbState(ClimbState state) {
        m_currentState = state;
    }

    public void close() {
        m_winchMotor.close();
        m_climbMotor.close();
    }

    @Override
    public void periodic() {
        switch (m_currentState) {
            case Idle:
                m_climbMotor.set(ControlMode.PercentOutput, Idle_Power);
                m_winchMotor.set(ControlMode.PercentOutput, Idle_Power);
                break;
            case Winching:
                m_climbMotor.set(ControlMode.PercentOutput, Idle_Power);
                m_winchMotor.set(ControlMode.PercentOutput, Winch_Power);
                break;
            case Unwinching:
                m_climbMotor.set(ControlMode.PercentOutput, Idle_Power);
                m_winchMotor.set(ControlMode.PercentOutput, Unwinch_Power);
                break;
            case Climbing:
                m_climbMotor.set(ControlMode.PercentOutput, Climb_Power);
                m_winchMotor.set(ControlMode.PercentOutput, Idle_Power);
                break;
        }
    }

    @Override
    public void simulationPeriodic() {}
}
