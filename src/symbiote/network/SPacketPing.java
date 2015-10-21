package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SPacketPing extends AbstractPacket {

    String msg;
    
    public SPacketPing() {
    }
    
    public SPacketPing(String msg) {
        this.msg = msg;
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(msg);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.msg = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        System.out.println(this.msg);
    }

}
