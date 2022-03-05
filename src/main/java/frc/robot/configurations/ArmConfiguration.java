package frc.robot.configurations;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import frc.robot.subsystems.ArmSubsystem;

public class ArmConfiguration {
    public static void configure(
            TalonFX armMotor, TalonFX intake1Motor, TalonFX intake2Motor, CANCoder armCoder) {

        /**
        * CANcoder is config'd first so it has time to send its position frame which is used at the end
        * of the configure method
        */
        CANCoderConfiguration coderConfig = new CANCoderConfiguration();
        coderConfig.magnetOffsetDegrees = Arm_Magnet_Offset;
        coderConfig.absoluteSensorRange = AbsoluteSensorRange.Signed_PlusMinus180;
        coderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        coderConfig.unitString = "deg";

        armCoder.configAllSettings(coderConfig);
        armCoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 10);
        armCoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 250);

        TalonFXConfiguration armConfig = new TalonFXConfiguration();
        armConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

        armConfig.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_1Ms;
        armConfig.velocityMeasurementWindow = 1;

        armConfig.voltageCompSaturation = 12.0;
        armConfig.supplyCurrLimit.currentLimit = 30;
        armConfig.supplyCurrLimit.triggerThresholdCurrent = 30;
        armConfig.supplyCurrLimit.triggerThresholdTime = 0;

        armConfig.slot0.kP = 0.1;
        armConfig.slot0.closedLoopPeakOutput = 0.3;

        armMotor.configAllSettings(armConfig);

        TalonFXConfiguration intake1Config = new TalonFXConfiguration();
        intake1Config.supplyCurrLimit.currentLimit = 30;
        intake1Config.supplyCurrLimit.triggerThresholdCurrent = 30;
        intake1Config.supplyCurrLimit.triggerThresholdTime = 0;
        intake1Motor.configAllSettings(intake1Config);

        TalonFXConfiguration intake2Config = new TalonFXConfiguration();
        intake2Config.supplyCurrLimit.currentLimit = 30;
        intake2Config.supplyCurrLimit.triggerThresholdCurrent = 30;
        intake2Config.supplyCurrLimit.triggerThresholdTime = 0;
        intake2Motor.configAllSettings(intake2Config);

        armMotor.enableVoltageCompensation(true);
        armMotor.setSelectedSensorPosition(
                ArmSubsystem.angleToNative(armCoder.getAbsolutePosition()), 0, 100);
    }
}
