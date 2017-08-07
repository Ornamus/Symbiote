// Each Packet is prefixed by who sends it, CPacketX is sent by client to server, SPacketY is sent by server to client.
package symbiote.network;

import java.io.*;

public abstract class AbstractPacket implements Serializable {

    public abstract void handle(Communicator comm);
}
