package symbiote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import symbiote.client.Client;
import javax.swing.JOptionPane;
import symbiote.network.*;
import symbiote.server.Server;
import symbiote.world.Floor;
import symbiote.world.Block;
import symbiote.world.Furniture;
import symbiote.world.Wall;

public class Main {
    
    Server s = null;
    public static boolean client = false;
    public static boolean server = false;
    
    public static Map<Integer, Class<? extends AbstractPacket>> idToPacket = new HashMap<>();
    public static Map<Class<? extends AbstractPacket>, Integer> packetToID = new HashMap<>();
    
    public static Map<Integer, Class<? extends Block>> idToBlock = new HashMap<>();
    public static Map<Class<? extends Block>, Integer> blockToID = new HashMap<>();
    
    public Main(int result) {
        addPacket(1, CPacketJoin.class);
        addPacket(2, SPacketAccepted.class);
        addPacket(3, SPacketDenied.class);
        addPacket(4, CPacketDisconnect.class);
        addPacket(5, CPacketShoot.class);
        addPacket(6, SPacketPlayer.class);
        addPacket(7, SPacketSymbiote.class);
        addPacket(8, CPacketSymbioteControl.class);
        addPacket(9, CPacketPosition.class);
        addPacket(10, SPacketPing.class);
        addPacket(11, SPacketBullet.class);
        addPacket(12, SPacketEntityDestroy.class);
        addPacket(13, SPacketWorld.class);
        addPacket(14, SPacketEntityHealth.class);
        
        addBlock(1, Block.class);
        addBlock(2, Wall.class);
        addBlock(3, Floor.class);
        addBlock(4, Furniture.class);
        
        if (result == -1)
            result = JOptionPane.showOptionDialog(
                    null,
                    "What would you like to start?", 
                    "Symbiote Startup",
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    new String[]{"Client", "Server", "Close"}, 
                    "Client");
        
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
        List<String> arguments = Arrays.asList(args);
        
        int type = -1;
        if (arguments.contains("client"))
            type = 0;
        else if (arguments.contains("server"))
            type = 1;
        
        if (arguments.contains("debug")) {
            Client.DEBUG = true;
        }
        
        new Main(type);
    }
    
    public static void addPacket(int id, Class<? extends AbstractPacket> p) {
        idToPacket.put(id, p);
        packetToID.put(p, id);
    }
    
    public static void addBlock(int id, Class<? extends Block> b) {
        idToBlock.put(id, b);
        blockToID.put(b, id);
    }
}
