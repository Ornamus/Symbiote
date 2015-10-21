package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.LivingEntity;
import symbiote.entity.client.ClientLivingEntity;
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
        for (AbstractEntity t : Server.entities.values()) {
            if (t instanceof LivingEntity) {
                ((LivingEntity) t).symbioteControlled = false;
            }
        }
        LivingEntity controlled;
        
        if (name.equalsIgnoreCase("Symbiote")) {
            controlled = Server.getSymbiote();
        } else {
            controlled = Server.getPlayer(name);
        }
        if (controlled != null) {
            controlled.symbioteControlled = true;
            Server.getSymbiote().controlledEntity = controlled;
        }
    }

}
