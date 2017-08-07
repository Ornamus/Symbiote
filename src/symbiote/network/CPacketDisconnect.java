package symbiote.network;

import symbiote.server.Server;

public class CPacketDisconnect extends AbstractPacket {
    @Override
    public String toString() {
        return "CPacketDisconnect [player=" + name + "]";
    }
    
    String name;
    
    public CPacketDisconnect(String name) {
        this.name = name;
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Communicator.Type.SERVERSIDE) {
            Server.handlePlayerDisconnect(name, " has disconnected.");
        }
    }
}
