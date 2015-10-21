package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        // TODO: disconnect
        /*ClientEntityPlayer p = ClientEntityPlayer.get(name);
        if (p != null) {
            p.destroy();
        }*/
    }
}
