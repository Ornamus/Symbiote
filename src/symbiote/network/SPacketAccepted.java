package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import symbiote.client.Client;
import symbiote.client.Skill;
import symbiote.client.screen.SkillBar;
import symbiote.network.Communicator.Type;

public class SPacketAccepted extends AbstractPacket {
    String name;
    boolean isSymbiote;
    
    public SPacketAccepted() {
    }
    
    public SPacketAccepted(String name, boolean isSymbiote) {
        this.name = name;
        this.isSymbiote = isSymbiote;
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(this.name);
        out.writeBoolean(this.isSymbiote);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.name = in.readUTF();
        this.isSymbiote = in.readBoolean();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            Client.name = this.name;
            Client.symbiote = this.isSymbiote;
            
            comm.name = this.name;

            JOptionPane.showMessageDialog(null, "Name accepted! You are " + (Client.symbiote ? "" : "not ") + "the Symbiote!");
            if (Client.symbiote) {
                SkillBar.setSkills(new Skill[]{Skill.SYMBIOTE_POSSESS, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING});
            } else {
                SkillBar.setSkills(new Skill[]{Skill.SHOOT_BULLET, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING});
            }
        }
    }

}
