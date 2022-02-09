import static org.junit.Assert.assertEquals;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ArmCommands.ArmCollectCommand;
import frc.robot.commands.ArmCommands.ArmIndexCommand;
import frc.robot.commands.ArmCommands.ArmStowCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.wrappers.PilotFX;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArmSubsystemFunction {
    ArmSubsystem m_armSubsystem;
    PilotFX armFx;
    WPI_CANCoder armCoder;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        m_armSubsystem = new ArmSubsystem();
        armFx = m_armSubsystem.getArmMotor();
        armCoder = m_armSubsystem.getArmCanCoder();
    }

    @After
    public void destroyDevices() {
        armFx.close();
    }

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
        ArmStowCommand stowCommand = new ArmStowCommand(m_armSubsystem);
        stowCommand.initialize();
        stowCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticIndex() {
        ArmIndexCommand indexCommand = new ArmIndexCommand(m_armSubsystem);
        indexCommand.initialize();
        indexCommand.execute();
        m_armSubsystem.periodic();
        waitForUpdate();

        assertEquals(armFx.getControlMode(), ControlMode.Position);
    }

    @Test
    public void testAutomaticCollect() {
        ArmCollectCommand collectCommand = new ArmCollectCommand(m_armSubsystem);
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
