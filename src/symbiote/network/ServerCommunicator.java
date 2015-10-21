package symbiote.network;

import java.net.Socket;

import symbiote.server.Server;

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

    @Override
    public void onClose() {
        Server.clients.remove(this);
        
        Server.gui.log(name + " has disconnected.");
        Server.gui.refreshClients();
    }
}
