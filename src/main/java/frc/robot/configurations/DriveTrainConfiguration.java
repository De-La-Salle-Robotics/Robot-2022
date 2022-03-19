package frc.robot.configurations;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;

public class DriveTrainConfiguration {
    public static void configure(TalonFX leader, IMotorController follower) {

        follower.follow(leader);
        follower.setInverted(InvertType.FollowMaster);

        TalonFXConfiguration toConfigure = new TalonFXConfiguration();
        toConfigure.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

        /* TODO: Update velocity window to make them smoother */
        toConfigure.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_1Ms;
        toConfigure.velocityMeasurementWindow = 1;

        toConfigure.slot0.kP = 0.1;
        toConfigure.slot0.kI = 0.0;
        toConfigure.slot0.kD = 0.0;
        toConfigure.slot0.kF = 0.0;
        toConfigure.openloopRamp = 0.375;

        toConfigure.voltageCompSaturation = 12.0;

        leader.configAllSettings(toConfigure);

        leader.enableVoltageCompensation(true);
    }
}
