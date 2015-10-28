package symbiote.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityPlayer;
import symbiote.entity.EntitySymbiote;
import symbiote.entity.LivingEntity;
import symbiote.network.AbstractPacket;
import symbiote.network.Communicator;
import symbiote.network.SPacketEntityDestroy;
import symbiote.network.SPacketPing;
import symbiote.network.ServerCommunicator;

public class Server extends Thread { 
    
    public static ServerGUI gui;
    public static int port = 9001;
    public static List<Communicator> clients = new ArrayList<>();   
    public static boolean symbioteNeeded = true;
    public Thread tickThread;

    public static Map<Integer, AbstractEntity> entities = new ConcurrentHashMap<>();
    
    public Server() {
        this(Server.port);
    }
    
    public Server (int port) {
        Server.port = port;
        gui = new ServerGUI(this);
        gui.setVisible(true);
    }
    
    private AbstractQueue<String> commandQueue = new ConcurrentLinkedQueue<String>();

    public void queueCommand(String command) {
        commandQueue.add(command);
    }
    
    /**
     * Executes a user's command on the server.
     * @param cmd The command given by the user
     */
    public void execute(String cmd) {
        gui.log("> " + cmd);

        if (cmd.startsWith("/kick ")) {
            String name = cmd.substring(cmd.indexOf(' ') + 1);
            if (!handlePlayerDisconnect(name, " was kicked.")) gui.log("\"" + name + "\" does not exist!");           
        } else if (cmd.startsWith("/echo ")) {
            String msg = cmd.substring(cmd.indexOf(' ') + 1);
            gui.log(msg);
            System.out.println(msg);
        } else if (cmd.startsWith("/pos")) {
            for (AbstractEntity e : Server.entities.values()) {
                gui.log(e.id + "@" +  e.x + "," + e.y);
            }
        } else if (cmd.equals("/pingall")) {
            Server.broadcast(new SPacketPing("All broadcast"));
        } else if (cmd.startsWith("/ping ")) {
            try {
                int index = Integer.parseInt(cmd.substring(cmd.indexOf(' ') + 1));
                Server.clients.get(index).sendMessage(new SPacketPing("Broadcast at " + index));
            } catch (Exception e) {
                gui.log("Bad argument #1");
            }
        } else if (cmd.startsWith("/pingbut ")) {
            try {
                int index = Integer.parseInt(cmd.substring(cmd.indexOf(' ') + 1));
                Server.broadcastExcept(new SPacketPing("Broadcast but not to " + index), Server.clients.get(index));
            } catch (Exception e) {
                gui.log("Bad argument #1");
            }
        }
    }
    
    @Override
    public void run() {
        tickThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    String cmd;
                    while ((cmd = commandQueue.poll()) != null) {
                        Server.this.execute(cmd);
                    }
                    
                    // Main.screen.tick();
                    try {
                        sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        tickThread.start();
        
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(port);
            gui.refreshClients();
            gui.log("Server started on " + InetAddress.getLocalHost().getHostAddress() + ":" + port + ".");
        } catch (IOException ex) {
        }
        try {
            while (true) {
                try {
                    Communicator c = new ServerCommunicator(listener.accept());
                    c.start();
                    gui.log("Opened a connection with " + c.getSocket().getInetAddress().getHostAddress() + ".");
                    gui.log("Port: " + c.getSocket().getLocalPort());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (listener != null)
                    listener.close();               
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Sends a message to every client.
     */
    public static void broadcast(AbstractPacket message) {
        for (Communicator c : clients) {
            System.out.println(c.getSocket().getLocalPort());
            try {
                c.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Sends a message to every client except for 'excluded'.
     */
    public static void broadcastExcept(AbstractPacket message, Communicator excluded) {
        for (Communicator client : clients) {
            if (excluded != client) {
                try {
                    client.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static EntitySymbiote getSymbiote() {
        for (AbstractEntity e : entities.values()) {
            if (e instanceof EntitySymbiote) {
                return (EntitySymbiote) e;
            }
        }
        return null;
    }

    public static EntityPlayer getPlayer(String name) {
        for (AbstractEntity e : entities.values()) {
            if (e instanceof EntityPlayer) {
                if (((EntityPlayer) e).name.equals(name)) {
                    return (EntityPlayer) e;
                }
            }
        }
        return null;
    }

    public static boolean handlePlayerDisconnect(String name, String message) {
        Communicator client = null;
        for (Communicator c : clients) {
            if (c.name.equalsIgnoreCase(name)) {
                client = c;
                break;
            }
        }
        if (client != null) {
            LivingEntity e = Server.getPlayer(name);
            if (e == null) {
                e = Server.getSymbiote();
            }
            if (e.name.equalsIgnoreCase(name)) {
                Server.broadcast(new SPacketEntityDestroy(e.id));
                e.destroy();
            }

            Server.clients.remove(client);
            Server.gui.log(name + message);
            Server.gui.refreshClients();
            return true;
        } else {
            System.out.println("[ERROR] Told to disconnect client \"" + name + "\" but they don't exist!");
            return false;
        }
    }
}
