package symbiote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread { 
    
    public static ServerGUI gui;
    public static int port = 9001;
    public static List<Communicator> clients = new ArrayList<>();   
    public static boolean symbioteNeeded = true;
    public Thread tickThread;

    public Server() {
        // TODO: de-static
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
        
        if (cmd.startsWith("/echo ")) {
            String msg = cmd.substring(cmd.indexOf(' ') + 1);
            gui.log(msg);
            System.out.println(msg);
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
                    
                    Main.screen.tick();
                    try {
                        sleep(25);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        if (!(Main.client && Main.server)) tickThread.start();
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
                    Communicator c = new Communicator(listener.accept());
                    c.start();
                    gui.log("Opened a connection with " + c.socket.getInetAddress().getHostAddress() + ".");
                } catch (IOException ex) {
                    
                }
                
                try {
                    sleep(25);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            try {
                listener.close();               
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Sends a message to every client.
     */
    public static void broadcast(String message) {
        for (Communicator c : clients) {
            c.out.println(message);
        }
    }
    
    /**
     * Sends a message to every client except for c.
     */
    public static void broadcast(String message, Communicator c) {
        for (Communicator c2 : clients) {
            if (c != c2) {
                c2.out.println(message);
            }
        }
    }
    
    /**
     * Sends data to every client.
     */
    public static void broadcast(Object[] array) {
        String message = "";
        for (Object o : array) {
            message = message + o + (o.equals(array[array.length - 1]) ? "" : ":");
        }
        broadcast(message);
    }   
    
    /**
     * Sends data to every client except for c.
     */
    public static void broadcast(Object[] array, Communicator c) {
        String message = "";
        for (Object o : array) {
            message = message + o + (o.equals(array[array.length - 1]) ? "" : ":");
        }
        broadcast(message, c);
    }   
}