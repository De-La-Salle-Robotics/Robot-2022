package frc.robot.configurations;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import frc.robot.subsystems.ArmSubsystem;

public class ArmConfiguration {
    public static void configure(TalonFX leader, CANCoder armCoder) {

        TalonFXConfiguration toConfigure = new TalonFXConfiguration();
        toConfigure.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

        /* TODO: Update velocity window to make them smoother */
        toConfigure.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_1Ms;
        toConfigure.velocityMeasurementWindow = 1;

        toConfigure.voltageCompSaturation = 12.0;

        leader.configAllSettings(toConfigure);

        leader.enableVoltageCompensation(true);

        CANCoderConfiguration coderConfig = new CANCoderConfiguration();
        coderConfig.magnetOffsetDegrees = Arm_Magnet_Offset;
        coderConfig.absoluteSensorRange = AbsoluteSensorRange.Signed_PlusMinus180;
        coderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        coderConfig.unitString = "deg";
        
        armCoder.configAllSettings(coderConfig);

        leader.setSelectedSensorPosition(ArmSubsystem.angleToNative(armCoder.getAbsolutePosition()));
    }
}
