import static org.junit.Assert.assertEquals;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.robot.Constants;
import frc.robot.subsystems.DriveBaseSubsystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DriveSubsystemFunction {
    static DriveBaseSubsystem m_driveBaseSubsystem;
    PilotFX leftLeader, rightLeader;

    @Before
    public void constructDevices() {
        assert HAL.initialize(500, 0);

        m_driveBaseSubsystem = new DriveBaseSubsystem();
        leftLeader = m_driveBaseSubsystem.getLeftLeader();
        rightLeader = m_driveBaseSubsystem.getRightLeader();
    }

    @After
    public void destroyDevices() {}

    @Test
    public void testSendable() {
        SmartDashboard.putData("DriveBaseSubsystem", m_driveBaseSubsystem);
    }

    @Test
    public void testArcadeDrive() {
        double throt = 0.6;
        double turn = 0.4;
        double busV = 12;
        m_driveBaseSubsystem.arcadeDrive(throt, turn);
        var leftSim = leftLeader.getSimCollection();
        var rightSim = rightLeader.getSimCollection();

        leftSim.setBusVoltage(busV);
        rightSim.setBusVoltage(busV);

        waitForUpdate();

        double expectedLeft = busV * (throt - turn);
        double expectedRight = busV * (throt + turn);
        if (Constants.Left_Side_Inverted) expectedLeft = -expectedLeft;
        if (Constants.Right_Side_Inverted) expectedRight = -expectedRight;
        assertEquals(leftSim.getMotorOutputLeadVoltage(), expectedLeft, 0.1);
        assertEquals(rightSim.getMotorOutputLeadVoltage(), expectedRight, 0.1);
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
