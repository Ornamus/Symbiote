package symbiote.network;

import javax.swing.JOptionPane;
import symbiote.network.Communicator.Type;

public class SPacketDenied extends AbstractPacket {
    
    String reason;
    
    public SPacketDenied(String reason) {
        this.reason = reason;
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