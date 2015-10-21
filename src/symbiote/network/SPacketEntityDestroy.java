package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.client.Client;

public class SPacketEntityDestroy extends AbstractPacket {
    int eID;
    
    public SPacketEntityDestroy() {
    }
    
    public SPacketEntityDestroy(int eID) {
        this.eID = eID;
    }

    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeInt(eID);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.eID = in.readInt();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Communicator.Type.CLIENTSIDE) {
            Client.screen.thingMap.get(eID).destroy();
        }
    }
}
