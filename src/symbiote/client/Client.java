package symbiote.client;

import javax.swing.JFrame;
import symbiote.Communicator;

public class Client extends JFrame {
    
    public static Client self;
    public static Board board;
    public static Communicator communicator = null;
    public static String name = "";
    public static boolean symbiote = false;
    
    public Client() {
        setSize(800, 600);
        communicator = new Communicator();
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
    }
}

