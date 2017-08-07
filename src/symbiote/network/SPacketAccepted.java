package symbiote.network;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import symbiote.client.Client;
import symbiote.client.Skill;
import symbiote.client.screen.SkillBar;
import symbiote.network.Communicator.Type;

public class SPacketAccepted extends AbstractPacket {
    String name;
    boolean isSymbiote;

    public SPacketAccepted(String name, boolean isSymbiote) {
        this.name = name;
        this.isSymbiote = isSymbiote;
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            Client.name = this.name;
            Client.symbiote = this.isSymbiote;
            
            comm.name = this.name;

            JOptionPane.showMessageDialog(null, "Name accepted! You are " + (Client.symbiote ? "" : "not ") + "the Symbiote!");
            if (Client.symbiote) {
                SkillBar.setSkills(new Object[][]{ {KeyEvent.VK_1, Skill.SYMBIOTE_POSSESS},  {KeyEvent.VK_2, Skill.NOTHING}, {KeyEvent.VK_3, Skill.NOTHING} , {KeyEvent.VK_4, Skill.NOTHING}});
            } else {
                SkillBar.setSkills(new Object[][]{ {KeyEvent.VK_1, Skill.SHOOT_BULLET},  {KeyEvent.VK_2, Skill.NOTHING}, {KeyEvent.VK_3, Skill.NOTHING} , {KeyEvent.VK_4, Skill.NOTHING}});
            }
        }
    }

}
