import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.pilotlib.ctrwrappers.PilotCoder;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.robot.commands.armcommands.ArmGoToCollectCommand;
import frc.robot.commands.armcommands.ArmGoToIndexCommand;
import frc.robot.commands.armcommands.ArmGoToStowCommand;
import frc.robot.subsystems.ArmSubsystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArmSubsystemFunction {
    static ArmSubsystem m_armSubsystem = new ArmSubsystem();
    PilotFX armFx;
    PilotFX intakeFx1;
    PilotFX intakeFx2;
    PilotCoder armCoder;
    DigitalInput ballDetectInput;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        armFx = m_armSubsystem.getArmMotor();
        intakeFx1 = m_armSubsystem.getIntakeMotor1();
        intakeFx2 = m_armSubsystem.getIntakeMotor2();
        armCoder = m_armSubsystem.getArmCanCoder();
        ballDetectInput = m_armSubsystem.getBallDetectInput();
    }

    @After
    public void destroyDevices() {}

    @Test
    public void testSendable() {
        SmartDashboard.putData("Arm Subsystem", m_armSubsystem);
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
        ArmGoToStowCommand stowCommand = new ArmGoToStowCommand(m_armSubsystem);
        stowCommand.initialize();
        stowCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticIndex() {
        ArmGoToIndexCommand indexCommand = new ArmGoToIndexCommand(m_armSubsystem);
        indexCommand.initialize();
        indexCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticCollect() {
        ArmGoToCollectCommand collectCommand = new ArmGoToCollectCommand(m_armSubsystem);
        collectCommand.initialize();
        collectCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
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
