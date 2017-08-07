package symbiote.network;

import java.io.*;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Communicator extends Thread {
    public enum Type {
        SERVERSIDE,
        CLIENTSIDE
    }
    
    private Socket socket = null;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public String name = "";
    private boolean connected = false;
    
    private Queue<AbstractPacket> queuedPackets = new ConcurrentLinkedQueue<>();

    private Thread receiveThread;
    
    public Communicator(Socket socket) {
        this.socket = socket;
    }

    protected void sendPackets() throws Exception {
        AbstractPacket p;
        while ((p = queuedPackets.poll()) != null) {
            out.writeObject(p);
            out.flush();
        }
    }

    protected void receivePackets()  {
        try {
            Object o = in.readObject();
            AbstractPacket p = (AbstractPacket) o;
            p.handle(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            connected = true;
            
            onConnect();

            receiveThread = new Thread() {
                @Override
                public void run() {
                    while (true) receivePackets();
                }
            };
            receiveThread.start();

            while (true) {
                try {
                    sendPackets();
                } catch (Exception e) {
                    // TODO: non-side-effect handle error
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onClose(); //TODO: This doesn't seem to be being called
            
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(AbstractPacket p) {
       queuedPackets.add(p);
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public boolean isConnected() {
        return connected;
    }
    
    public abstract Type getType();
    public void onConnect() {}
    public void onClose() {}
}
