package symbiote;

import symbiote.handlers.PacketHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static java.lang.Thread.sleep;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import static symbiote.Server.clients;
import symbiote.client.Client;
import symbiote.entity.EntityBullet;
import symbiote.entity.EntityPlayer;
import symbiote.entity.EntitySymbiote;
import symbiote.entity.LivingEntity;
import symbiote.resources.AudioHandler;
import symbiote.screen.DebugScreen;

public class Communicator extends Thread {
    
    public Socket socket = null;
    public BufferedReader in;
    public PrintWriter out;

    private InetAddress serverIp;
    private int serverPort;
    
    public String name = "";
    public boolean server;
    public boolean connected = false;
    
    List<String> addMessages = new ArrayList<>();
    List<String> messages = new ArrayList<>();

    public Communicator(InetAddress ip, int port) {
        server = false;
        
        this.serverIp = ip;
        this.serverPort = port;
    }
    
    public Communicator(Socket socket) {
        this.socket = socket;
        server = true;
    }

    @Override
    public void run() {
        try {
            if (!server) {              
                System.out.println("Connecting to server " + this.serverIp + ":" + this.serverPort);
                socket = new Socket(this.serverIp, this.serverPort);
            }
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (!server) {
                while (!clientInit()) {sleep(25);}
            } else {
                while (!serverInit()) {sleep(25);}
            }
            connected = true;
            Main.screen = new DebugScreen();
            while (true) {
                for (String s : new ArrayList<>(addMessages)) {
                    messages.add(s);
                }
                addMessages.clear();
                for (String s : messages) {
                    out.println(s);
                }
                messages.clear();
                boolean stop = false;
                while (in.ready()) {
                    String message = in.readLine();
                    String[] array = message.split(":");
                    PacketHandler ph = Main.packetHandlers.get(array[0]);
                    if (ph != null) {
                        ph.onPacket(message, array);
                    } else {
                        System.out.println("Got a '" + array[0] + "' packet that doesn't have a handler!");
                        if (array[0].equals("symbiote")) {
                            if (server) {
                                Server.broadcast(message, this);
                            }
                            EntitySymbiote sim = null;
                            for (Thing t : new ArrayList<>(Main.screen.things)) {
                                if (t instanceof EntitySymbiote) {
                                    EntitySymbiote foundSim = (EntitySymbiote) t;
                                    if (sim == null) {
                                        sim = foundSim;
                                    } else {
                                        foundSim.destroy();
                                    }
                                }
                            }
                            if (sim == null) {
                                sim = (EntitySymbiote) Thing.create(new EntitySymbiote(0, 0));
                                sim.playing = false;
                            }
                            sim.x = Double.parseDouble(array[1]);
                            sim.y = Double.parseDouble(array[2]);
                            sim.angle = Double.parseDouble(array[3]);
                            sim.dontUse = Boolean.parseBoolean(array[4]);
                        } else if (array[0].equals("symbioteControl")) {
                            if (server) {
                                Server.broadcast(message, this);
                            }
                            for (Thing t : Main.screen.things) {
                                if (t instanceof LivingEntity) {
                                    ((LivingEntity) t).symbioteControlled = false;
                                }
                            }
                            LivingEntity controlled;
                            if (array[1].equalsIgnoreCase("Symbiote")) {
                                controlled = EntitySymbiote.get();
                            } else {
                                controlled = EntityPlayer.get(array[1]);
                            }
                            controlled.symbioteControlled = true;
                            EntitySymbiote.get().controlledEntity = controlled;
                        } else if (array[0].equals("entity")) {
                            if (server) {
                                Server.broadcast(message);
                            }
                            if (array[1].equals("bullet")) {
                                Thing.create(new EntityBullet(Double.parseDouble(array[2]), Double.parseDouble(array[3]), Double.parseDouble(array[4]), array[5]));
                                if (!server) {
                                    AudioHandler.playSound("gun.wav");
                                }
                            }
                        }
                    }
                }
                if (stop) break;
                sleep(25);
            }
        } catch (Exception e) {
        } finally {
            if (server) {
                Server.gui.log(name + " has disconnected.");
                clients.remove(this);
                Server.gui.refreshClients();
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean clientInit() throws IOException {
        String submittedName = JOptionPane.showInputDialog("Please type your name.");
        if (submittedName == null || submittedName.equals("null")) {
            System.exit(0);
        }
        out.println(submittedName);
        waitForMessage();
        while (true) {
            waitForMessage();
            String reply = in.readLine();
            if (reply.contains("accepted")) {
                Client.name = submittedName;
                Client.symbiote = Boolean.parseBoolean(reply.split(":")[1]);
                JOptionPane.showMessageDialog(null, "Name accepted! You are " + (Client.symbiote ? "" : "not ") + "the Symbiote!");
                return true;
            } else if (reply.contains("denied")) {
                JOptionPane.showMessageDialog(null, "Name is already taken.");
                return false;
            }
        }
    }
    
    public boolean serverInit() throws IOException {
        waitForMessage();
        String sentName = in.readLine();
        boolean nameTaken = false;
        for (Communicator c : Server.clients) {
            if (c.name.equals(sentName)) {
                nameTaken = true;
                break;
            }
        }
        if (!nameTaken) {
            out.println("accepted:" + Server.symbioteNeeded);
            if (Server.symbioteNeeded) Server.symbioteNeeded = false;
            name = sentName;
            Server.gui.log(sentName + " has joined.");
            Server.clients.add(this);
            Server.gui.refreshClients();
            return true;
        } else {
            out.println("denied:Name taken");
            Server.gui.log("New user submitted invalid name \"" + sentName + "\".");
            return false;
        }
    }

    /**
     * Loops at 25ms until a message appears in the BufferedReader.
     */
    public void waitForMessage() {
        try {
            while (!in.ready()) {
                try {
                    sleep(25);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendMessage(String s) {
       addMessages.add(s);
    }
    
    public void sendMessage(Object[] array) {
        String message = "";
        for (Object o : array) {
            message = message + o + (o.equals(array[array.length - 1]) ? "" : ":");
        }
        sendMessage(message);
    }
    
}
