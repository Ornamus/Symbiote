package symbiote.client;

import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import symbiote.client.screen.Screen;
import symbiote.entity.AbstractEntity;
import symbiote.network.CPacketDisconnect;
import symbiote.network.ClientCommunicator;
import symbiote.network.Communicator;

@SuppressWarnings("serial")
public class Client extends JFrame {
    public static Client self;
    public static Board board;
    public static Communicator communicator = null;
    public static String name = "";
    public static boolean symbiote = false;
    
    public static AbstractEntity focus = null;
    
    public static Screen screen;
    public Client() throws Exception {
        Client.screen = new Screen();
        
        setSize(800, 600);
        
        String ip = JOptionPane.showInputDialog("Please enter server address", "localhost");
        
        if (ip == null)
            throw new Exception("No IP provided");
        int port;
        
        // if entered matches a string with something like *:123 on the end, takes the port off the end.
        if (ip.matches(".*:\\d*$")) {
            port = Integer.parseInt(ip.substring(ip.lastIndexOf(':') + 1));
            ip = ip.substring(0, ip.lastIndexOf(':'));
        } else {
            port = 9001;
        }
        
        // create address, confirm is reachable
        InetAddress addr = InetAddress.getByName(ip);
        if (!addr.isReachable(5000))
            throw new Exception(addr + " is not reachable");

        communicator = new ClientCommunicator(addr, port);
        communicator.start();
        
        board = new Board(this);
        add(board);
        InputListener input = new InputListener();
        addMouseListener(input);
        addKeyListener(input);
        setTitle("Symbiote");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        self = this;
        setVisible(true);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                communicator.sendMessage(new CPacketDisconnect(name));
            }
        });
    }
}
