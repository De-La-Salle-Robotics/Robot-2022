import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.wrappers.PilotFX;
import frc.robot.wrappers.PilotPigeon;
import org.junit.*;

public class ConstructionTests {
    PilotFX m_fx;
    PilotPigeon m_pigeon;

    @Before
    public void constructDevices() {
        m_fx = new PilotFX(0);
        m_pigeon = new PilotPigeon(0);
    }

    @After
    public void destroyDevices() {
        m_fx.close();
        m_pigeon.close();
    }

    @Test
    public void testSendables() {
        SmartDashboard.putData("PilotFX", m_fx);
        SmartDashboard.putData("PilotPigeon", m_pigeon);
    }
}
