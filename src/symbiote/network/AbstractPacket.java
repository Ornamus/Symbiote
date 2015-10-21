// Each Packet is prefixed by who sends it, CPacketX is sent by client to server, SPacketY is sent by server to client.
package symbiote.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class AbstractPacket {
    public byte[] toBytes() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream p = new ObjectOutputStream(out);
        
        write(p);
        
        p.close();
        return out.toByteArray();
    }
    public void fromBytes(byte[] bytes) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        
        read(in);
        
        in.close();
    }
    
    public abstract void write(ObjectOutputStream out) throws Exception;
    public abstract void read(ObjectInputStream in) throws Exception;
    public abstract void handle(Communicator comm);
}
