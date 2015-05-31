package symbiote;

import symbiote.client.Client;
import javax.swing.JOptionPane;
import symbiote.screen.Screen;

public class Main {

    public static Screen screen;
    Server s = null;
    public static boolean server = false;
    
    public Main() {
        int result = JOptionPane.showConfirmDialog(null, "Boot as client?");
        screen = new Screen();
        if (result == 0) {
            new Client();
        } else if (result == 1) {
            s = new Server();
            s.start();
            server = true;
        }
    }
    
    public static void main(String[] args) {      
        new Main();
    }   
}
