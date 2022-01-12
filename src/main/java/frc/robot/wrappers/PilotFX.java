package frc.robot.wrappers;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.util.sendable.SendableBuilder;

/** Wrapper class to provide reasonable values for TalonFX */
public class PilotFX extends WPI_TalonFX {

    public PilotFX(int deviceNumber) {
        super(deviceNumber);
    }

    public PilotFX(int deviceNumber, String canbusName) {
        super(deviceNumber, canbusName);
    }

    public static double toRotations(double rawUnits) {
        return rawUnits / 4096.0;
    }

    public static double toRawUnits(double rotations) {
        return rotations * 4096.0;
    }

    public static double toRPM(double rawVelUnits) {
        return rawVelUnits * 600.0 / 4096.0;
    }

    public static double toRawVelUnits(double rpm) {
        return rpm * 4096.0 / 600.0;
    }

    public double getRotations() {
        return toRotations(getSelectedSensorPosition());
    }

    public void setPosition(double rotations) {
        setSelectedSensorPosition(toRawUnits(rotations));
    }

    public double getRPM() {
        return toRPM(getSelectedSensorVelocity());
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        this.initSendable(builder);

        builder.addDoubleProperty("Position", this::getRotations, this::setPosition);
        builder.addDoubleProperty("Velocity", this::getRPM, null);
    }
}
