import static frc.robot.Constants.*;
import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.sensors.CANCoderSimCollection;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.simulation.DIOSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.pilotlib.ctrwrappers.PilotCoder;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.robot.commands.armcommands.ArmCommands;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.HopperSubsystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BallHandlingTests {
    static ArmSubsystem m_armSubsystem = new ArmSubsystem();
    static HopperSubsystem m_hopperSubsystem = new HopperSubsystem();
    PilotFX armFx;
    PilotFX intakeFx1;
    PilotFX intakeFx2;
    PilotFX lowerHopper;
    PilotFX upperHopper;
    PilotCoder armCoder;
    DigitalInput ballDetectInput;
    DIOSim ballDetectSim;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        armFx = m_armSubsystem.getArmMotor();
        intakeFx1 = m_armSubsystem.getIntakeMotor1();
        intakeFx2 = m_armSubsystem.getIntakeMotor2();
        lowerHopper = m_hopperSubsystem.getLowerHopperFx();
        upperHopper = m_hopperSubsystem.getUpperHopperFx();
        armCoder = m_armSubsystem.getArmCanCoder();
        ballDetectInput = m_armSubsystem.getBallDetectInput();
        ballDetectSim = new DIOSim(ballDetectInput);
    }

    @After
    public void destroyDevices() {}

    @Test
    public void testSendable() {
        SmartDashboard.putData("Arm Subsystem", m_armSubsystem);
        SmartDashboard.putData("Hopper Subsystem", m_hopperSubsystem);
    }

    @Test
    public void testManualMode() {
        double busV = 12;
        double dutyCycle = 0.1;
        m_armSubsystem.manualControl(dutyCycle);
        m_armSubsystem.periodic();
        var fxSim = armFx.getSimCollection();

        fxSim.setBusVoltage(busV);

        waitForUpdate(0.1);

        assertEquals(fxSim.getMotorOutputLeadVoltage(), busV * dutyCycle, 1);
    }

    @Test
    public void testAutomaticStow() {
        Command stowCommand = ArmCommands.getArmGoToStoreCommand(m_armSubsystem);
        stowCommand.initialize();
        stowCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate(0.1);

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticIndex() {
        Command indexCommand = ArmCommands.getArmGoToIndexCommand(m_armSubsystem);
        indexCommand.initialize();
        indexCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate(0.1);

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticCollect() {
        Command collectCommand =
                ArmCommands.getArmAutomaticCollectCommand(m_armSubsystem, m_hopperSubsystem);
        collectCommand.initialize();
        collectCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate(0.1);

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testBallDetect() {
        // Sensor is inverted, so test the inverse of the value
        ballDetectSim.setValue(false);
        assertEquals(m_armSubsystem.hasBall(), true);
        ballDetectSim.setValue(true);
        assertEquals(m_armSubsystem.hasBall(), false);
    }

    @Test
    public void testIndexPosition() {
        CANCoderSimCollection armSim = armCoder.getSimCollection();
        armSim.setRawPosition((int) (Stowed_Position * 4096.0 / 360.0));
        waitForUpdate(0.1);
        assertEquals(m_armSubsystem.isIndexed(), false);
        armSim.setRawPosition((int) (Indexing_Position * 4096.0 / 360.0));
        waitForUpdate(0.1);
        assertEquals(m_armSubsystem.isIndexed(), true);
    }

    @Test
    public void testAutomaticCollectSequence() {
        /* Create the command and initialize */
        Command automaticCollect =
                ArmCommands.getArmAutomaticCollectCommand(m_armSubsystem, m_hopperSubsystem);
        automaticCollect.initialize();
        /* Initialize sim objects to starting values */
        CANCoderSimCollection armSim = armCoder.getSimCollection();
        armSim.setRawPosition((int) (Stowed_Position * 4096.0 / 360.0)); // Start stow
        ballDetectSim.setValue(true); // Start no ball
        waitForUpdate(0.1, automaticCollect);
        /* Make sure arm is trying to move down to collect balls */
        assertEquals(armFx.getControlMode(), ControlMode.Position);
        assertEquals(armFx.getClosedLoopTarget(), ArmSubsystem.angleToNative(Collecting_Position), 10);
        assertEquals(intakeFx1.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx2.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx1.getMotorOutputPercent(), ArmSubsystem.Collect_Power, 0.1);
        assertEquals(intakeFx2.getMotorOutputPercent(), ArmSubsystem.Collect_Power, 0.1);
        assertEquals(lowerHopper.getMotorOutputPercent(), HopperSubsystem.Idle_Speed, 0.01);
        assertEquals(upperHopper.getMotorOutputPercent(), HopperSubsystem.Idle_Speed, 0.01);

        /* Now detect the ball and verify the arm stops running and moves to index */
        ballDetectSim.setValue(false); // We have a ball now

        waitForUpdate(0.1, automaticCollect);

        /* Make sure arm is moving to index without anything running */
        assertEquals(armFx.getControlMode(), ControlMode.Position);
        assertEquals(armFx.getClosedLoopTarget(), ArmSubsystem.angleToNative(Indexing_Position), 10);
        assertEquals(intakeFx1.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx2.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx1.getMotorOutputPercent(), ArmSubsystem.Idle_Power, 0.1);
        assertEquals(intakeFx2.getMotorOutputPercent(), ArmSubsystem.Idle_Power, 0.1);
        assertEquals(lowerHopper.getMotorOutputPercent(), HopperSubsystem.Idle_Speed, 0.01);
        assertEquals(upperHopper.getMotorOutputPercent(), HopperSubsystem.Idle_Speed, 0.01);

        /* Now set the intake to index position */
        armSim.setRawPosition((int) (Indexing_Position * 4096.0 / 360.0));

        waitForUpdate(0.1, automaticCollect);

        /* Verify arm is still holding index, but now the hoppers are running and intake is running to index */
        assertEquals(armFx.getControlMode(), ControlMode.Position);
        assertEquals(armFx.getClosedLoopTarget(), ArmSubsystem.angleToNative(Indexing_Position), 10);
        assertEquals(intakeFx1.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx2.getControlMode(), ControlMode.PercentOutput);
        assertEquals(intakeFx1.getMotorOutputPercent(), ArmSubsystem.Index_Power, 0.1);
        assertEquals(intakeFx2.getMotorOutputPercent(), ArmSubsystem.Index_Power, 0.1);
        assertEquals(lowerHopper.getMotorOutputPercent(), HopperSubsystem.Intake_Speed, 0.01);
        assertEquals(upperHopper.getMotorOutputPercent(), HopperSubsystem.Intake_Speed, 0.01);
        /* And we're done */
    }

    private static void waitForUpdate(double seconds, Command... commands) {
        try {
            for (int i = 0; i < 10; ++i) {
                com.ctre.phoenix.unmanaged.Unmanaged.feedEnable(100);
                for (Command c : commands) {
                    c.execute();
                }
                m_hopperSubsystem.periodic();
                m_armSubsystem.periodic();
                Thread.sleep((long) (seconds * 100));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
