package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.entity.EntityPlayer;
import symbiote.server.Server;

public class CPacketDisconnect extends AbstractPacket {
    String name;
    
    public CPacketDisconnect() {
    }
    
    public CPacketDisconnect(String name) {
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
        if (comm.getType() == Communicator.Type.SERVERSIDE) {
            EntityPlayer p = Server.getPlayer(name);
            if (p != null) {
                Server.broadcast(new SPacketEntityDestroy(p.id));
                p.destroy();
            }
            Server.gui.refreshClients();
            Server.gui.log(name + " has disconnected.");
        }
    }
}
