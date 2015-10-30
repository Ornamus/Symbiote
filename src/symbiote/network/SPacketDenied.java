package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import symbiote.network.Communicator.Type;

public class SPacketDenied extends AbstractPacket {
    
    String reason;
    
    public SPacketDenied() {
    }
    
    public SPacketDenied(String reason) {
        this.reason = reason;
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(reason);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.reason = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            JOptionPane.showMessageDialog(null, "Name is already taken.");
            
            String submittedName = JOptionPane.showInputDialog("Please type your name.");
            if (submittedName == null || submittedName.equals("null")) {
                System.exit(0);
            }
            comm.sendMessage(new CPacketJoin(submittedName));
        }
    }
}