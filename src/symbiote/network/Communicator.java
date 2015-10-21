package symbiote.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import symbiote.Main;

public abstract class Communicator extends Thread {
    public enum Type {
        SERVERSIDE,
        CLIENTSIDE
    }
    
    private Socket socket = null;
    private DataInputStream in;
    private DataOutputStream out;

    public String name = "";
    private boolean connected = false;
    
    private Queue<AbstractPacket> queuedPackets = new ConcurrentLinkedQueue<>();
    
    public Communicator(Socket socket) {
        this.socket = socket;
    }
    
    public void sendPackets() throws Exception {
        AbstractPacket s;
        while ((s = queuedPackets.poll()) != null) {
            int id = 0;
            try {
                id = Main.packetToID.get(s.getClass());
            } catch (NullPointerException e) {
                System.err.println("Packet " + s.getClass() + " is not defined in packetToID!");
                continue;
            }
            
            out.writeByte(id);
            
            byte[] b = s.toBytes();
            
            out.writeInt(b.length);
            out.write(b);
            
            System.out.println(this.getType() + " sending packet "+s+" id " + Main.packetToID.get(s.getClass()) + s.getClass() + " #" + b.length);
        }
    }
    
    public void receievePackets() throws Exception {
        if (in.available() > 0) {
            // get the length of the following message
            int type = in.readByte();
            int length = in.readInt();
            byte[] packetData = new byte[length];
            
            in.readFully(packetData);
            
            AbstractPacket packet = Main.idToPacket.get(type).newInstance();
            packet.fromBytes(packetData);
            System.out.println(this.getType() + " reading packet "+packet+" id " + type + Main.idToPacket.get(type) + " #" + length);
            packet.handle(this);
        } else {
            sleep(25);
        }
    }
    
    @Override
    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            connected = true;
            
            onConnect();
            
            while (true) {
                try {
                    sendPackets();
                    receievePackets();
                } catch (Exception e) {
                    // TODO: non-side-effect handle error
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onClose();
            
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

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public boolean isConnected() {
        return connected;
    }
    
    public abstract Type getType();
    public void onConnect() {}
    public void onClose() {}
}
