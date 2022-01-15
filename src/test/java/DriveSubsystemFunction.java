import static org.junit.Assert.assertEquals;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.wrappers.PilotFX;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DriveSubsystemFunction {
    DriveBaseSubsystem m_driveBaseSubsystem;
    PilotFX leftLeader, rightLeader;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        m_driveBaseSubsystem = new DriveBaseSubsystem();
        leftLeader = new PilotFX(Constants.Left_Leader_ID);
        rightLeader = new PilotFX(Constants.Right_Leader_ID);
    }

    @After
    public void destroyDevices() {
        leftLeader.close();
        rightLeader.close();
        m_driveBaseSubsystem.close();
    }

    @Test
    public void testSendable() {
        SmartDashboard.putData("DriveBaseSubsystem", m_driveBaseSubsystem);
    }

    @Test
    public void testArcadeDrive() {
        double throt = 0.4;
        double turn = 0.6;
        double busV = 12;
        m_driveBaseSubsystem.arcadeDrive(throt, turn);
        var leftSim = leftLeader.getSimCollection();
        var rightSim = rightLeader.getSimCollection();

        leftSim.setBusVoltage(busV);
        rightSim.setBusVoltage(busV);

        waitForUpdate();

        assertEquals(leftSim.getMotorOutputLeadVoltage(), busV * (throt - turn), 1);
        assertEquals(rightSim.getMotorOutputLeadVoltage(), busV * (throt + turn), 1);
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
