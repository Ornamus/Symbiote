package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.server.Server;

public class CPacketDisconnect extends AbstractPacket {
    @Override
    public String toString() {
        return "CPacketDisconnect [player=" + name + "]";
    }
    
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
            Server.handlePlayerDisconnect(name, " has disconnected.");
        }
    }
}
