import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.pilotlib.ctrwrappers.PilotCoder;
import frc.pilotlib.ctrwrappers.PilotFX;
import frc.pilotlib.ctrwrappers.PilotPigeon;
import org.junit.*;

public class ConstructionTests {
    PilotFX m_fx;
    PilotPigeon m_pigeon;
    PilotCoder m_coder;

    @Before
    public void constructDevices() {
        m_fx = new PilotFX(0);
        m_pigeon = new PilotPigeon(0);
        m_coder = new PilotCoder(0);
    }

    @After
    public void destroyDevices() {
        m_fx.close();
        m_pigeon.close();
        m_coder.close();
    }

    @Test
    public void testSendables() {
        SmartDashboard.putData("PilotFX", m_fx);
        SmartDashboard.putData("PilotPigeon", m_pigeon);
        SmartDashboard.putData("PilotCoder", m_coder);
    }
}
