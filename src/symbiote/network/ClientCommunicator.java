package symbiote.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import symbiote.client.Client;
import symbiote.client.screen.DebugScreen;

public class ClientCommunicator extends Communicator {

    public ClientCommunicator(InetAddress ip, int port) throws IOException {
        super(new Socket(ip, port));
        System.out.println("Connecting to server " + ip + ":" + port);
    }

    @Override
    public Type getType() {
        return Type.CLIENTSIDE;
    }

    @Override
    public void onConnect() {
        if (Client.screen == null)
            Client.screen = new DebugScreen();
        
        String name = JOptionPane.showInputDialog("Please type your name.");
        if (name == null || name.equals("null")) {
            System.exit(0);
        } else {
            this.sendMessage(new CPacketJoin(name));
        }
    }
}
