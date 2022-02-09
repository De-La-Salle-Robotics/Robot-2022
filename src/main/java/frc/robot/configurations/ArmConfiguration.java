package frc.robot.configurations;

import static frc.robot.Constants.*;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;

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

        leader.setSelectedSensorPosition(getArmAngle(armCoder.getPosition()));
    }

    private static double getArmAngle(double canCoderAngleDegrees) {
        /* Convert from degrees to rotations */
        double cancoderRots = canCoderAngleDegrees / 360.0;
        /* From rotations to native units */
        double nativeUnits = cancoderRots * 2048;
        /* Apply gear ratio to native units */
        return nativeUnits * Arm_Gearbox_Ratio;
    }
}
