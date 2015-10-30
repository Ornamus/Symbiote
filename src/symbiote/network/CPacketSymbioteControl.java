package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntitySymbiote;
import symbiote.entity.EntityUtil;
import symbiote.entity.LivingEntity;
import symbiote.server.Server;

public class CPacketSymbioteControl extends AbstractPacket {
    String name;
    
    public CPacketSymbioteControl() {
    }
    
    public CPacketSymbioteControl(String name) {
        this.name = name;
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(this.name);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.name = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        for (AbstractEntity t : EntityUtil.getEntities()) {
            if (t instanceof LivingEntity) {
                ((LivingEntity) t).symbioteControlled = false;
            }
        }
        LivingEntity controlled;

        EntitySymbiote sim = EntityUtil.getSymbiote();
        if (name.equalsIgnoreCase(sim.name)) {
            controlled = sim;
        } else {
            controlled = EntityUtil.getPlayer(name);
        }
        if (controlled != null) {
            controlled.symbioteControlled = true;
            sim.controlled = controlled;
            Server.broadcastExcept(this, comm);
        }
    }
}
