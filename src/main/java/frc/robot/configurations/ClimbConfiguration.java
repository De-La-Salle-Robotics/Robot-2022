package frc.robot.configurations;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class ClimbConfiguration {
    public static void configure(TalonFX winchMotor, VictorSPX climbMotor) {
        TalonFXConfiguration winchConfig = new TalonFXConfiguration();

        winchConfig.voltageCompSaturation = 12.0;
        winchConfig.peakOutputForward = 0.2;
        winchConfig.statorCurrLimit.currentLimit = 10;
        winchConfig.statorCurrLimit.triggerThresholdCurrent = 10;
        winchConfig.statorCurrLimit.triggerThresholdTime = 0;
        winchConfig.statorCurrLimit.enable = true;

        winchMotor.configAllSettings(winchConfig);
    }
}
