package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.client.Client;
import symbiote.misc.CrashError;

public class SPacketEntityDestroy extends AbstractPacket {
    @Override
    public String toString() {
        return "SPacketEntityDestroy [id=" + eID + "]";
    }
    
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
            if (Client.focus.id == eID) { //TODO: If you're the symbiote, focus isn't always your entity...right?
                new CrashError("You were kicked from the server, " + Client.name + "!");
            }
            Client.screen.thingMap.get(eID).destroy();
        }
    }
}
