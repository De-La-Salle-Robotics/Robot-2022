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
    PilotCoder armCoder;
    DigitalInput ballDetectInput;
    DIOSim ballDetectSim;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        armFx = m_armSubsystem.getArmMotor();
        intakeFx1 = m_armSubsystem.getIntakeMotor1();
        intakeFx2 = m_armSubsystem.getIntakeMotor2();
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
        double dutyCycle = 0.4;
        m_armSubsystem.manualControl(dutyCycle);
        m_armSubsystem.periodic();
        var fxSim = armFx.getSimCollection();

        fxSim.setBusVoltage(busV);

        waitForUpdate();

        assertEquals(fxSim.getMotorOutputLeadVoltage(), busV * dutyCycle, 1);
    }

    @Test
    public void testAutomaticStow() {
        Command stowCommand = ArmCommands.getArmGoToStoreCommand(m_armSubsystem);
        stowCommand.initialize();
        stowCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticIndex() {
        Command indexCommand = ArmCommands.getArmGoToIndexCommand(m_armSubsystem);
        indexCommand.initialize();
        indexCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticCollect() {
        Command collectCommand = ArmCommands.getArmAutomaticCollectCommand(m_armSubsystem, m_hopperSubsystem);
        collectCommand.initialize();
        collectCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

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
        waitForUpdate();
        assertEquals(m_armSubsystem.isIndexed(), false);
        armSim.setRawPosition((int) (Indexing_Position * 4096.0 / 360.0));
        waitForUpdate();
        assertEquals(m_armSubsystem.isIndexed(), true);
    }

    private static void waitForUpdate() {
        try {
            com.ctre.phoenix.unmanaged.Unmanaged.feedEnable(500);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
