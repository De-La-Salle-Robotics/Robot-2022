// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.pilotlib.utils.PlayableSubsystem;
import frc.robot.configurations.ClimbConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClimbSubsystem extends SubsystemBase implements PlayableSubsystem {
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
    private boolean m_isPlaying = false;

    @Override
    public Collection<TalonFX> getPlayableDevices() {
        List<TalonFX> talons = new ArrayList<TalonFX>();
        talons.add(m_winchMotor);
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
        if(m_isPlaying) return;
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
