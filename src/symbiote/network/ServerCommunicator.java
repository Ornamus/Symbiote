package symbiote.network;

import java.net.Socket;

public class ServerCommunicator extends Communicator {

    public ServerCommunicator(Socket socket) {
        super(socket);
    }
    
    @Override
    public Type getType() {
        return Type.SERVERSIDE;
    }

    @Override
    public void onConnect() {
        //Server.clients.add(this);
    }

    //TODO: This function isn't being called, so the stuff below is now being done in Server.handlePlayerDisconnect()
    @Override
    public void onClose() {
        //Server.clients.remove(this);
        
        //Server.gui.log(name + " has disconnected.");
        //Server.gui.refreshClients();
    }
}
