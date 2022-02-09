package frc.pilotlib.controllerwrappers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.function.DoubleSupplier;

/** 7762's Operator Controller is a logitech F310 in D-input mode. */
public class OperatorController extends GenericHID {

    /** Represents a digital button. */
    public enum Button {
        kLeftBumper(5),
        kRightBumper(6),
        kLeftStick(9),
        kRightStick(10),
        kA(1),
        kB(2),
        kX(3),
        kY(4),
        kBack(7),
        kStart(8);

        @SuppressWarnings("MemberName")
        public final int value;

        Button(int value) {
            this.value = value;
        }
    }

    /** Represents an axis. */
    public enum Axis {
        kLeftX(0),
        kRightX(4),
        kLeftY(1),
        kRightY(5),
        kLeftTrigger(2),
        kRightTrigger(3);

        @SuppressWarnings("MemberName")
        public final int value;

        Axis(int value) {
            this.value = value;
        }
    }

    public OperatorController(int port) {
        super(port);
        /* Verify this matches the logitech controller */
    }

    public DoubleSupplier getAxis(Axis axis) {
        return () -> getRawAxis(axis.value);
    }

    public JoystickButton getButton(Button button) {
        return new JoystickButton(this, button.value);
    }
}
