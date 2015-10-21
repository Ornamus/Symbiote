package symbiote;

import java.util.HashMap;
import symbiote.client.Client;
import javax.swing.JOptionPane;
import symbiote.handlers.PacketHandler;
import symbiote.handlers.PlayerHandler;
import symbiote.screen.Screen;

public class Main {

    public static Screen screen;
    Server s = null;
    public static boolean client = false;
    public static boolean server = false;
    
    public static HashMap<String, PacketHandler> packetHandlers = new HashMap<>();
    
    public Main() {
        int result = JOptionPane.showOptionDialog(
                null, 
                "What would you like to start?", 
                "Symbiote Startup",
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                new String[]{"Client", "Server", "Close"}, 
                "Client");
        screen = new Screen();
        
        
        //Packet Handler
        addHandler(new PlayerHandler());

        if (result == 0) {
            try {
                new Client();
                client = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Could not create client.\n" + e.getMessage());
                e.printStackTrace();
            }
        } else if (result == 1) {
            s = new Server();
            s.start();
            server = true;
        }
    }
    
    public static void main(String[] args) {      
        new Main();
    }
    
    public static void addHandler(PacketHandler p) {
        packetHandlers.put(p.packetType, p);
    }
}