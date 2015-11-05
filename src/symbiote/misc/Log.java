package symbiote.misc;

import javax.swing.JOptionPane;
import symbiote.Main;
import symbiote.server.Server;

public class Log {
    
    /**
     * Logs an info message.
     */
    public static void i(String m) {
        systemAndGUI("[INFO] " + m);
    }
    
    /**
     * Logs a debug message.
     */
    public static void d(String m) {
        systemAndGUI("[DEBUG] " + m);
    }
    
    /**
     * Logs an error message.
     */
    public static void e(String m) {
        systemAndGUI("[ERROR] " + m);
        if (Main.client) JOptionPane.showMessageDialog(null, "ERROR\n" + m);
    }
    
    /**
     * Logs a warning message.
     */
    public static void w(String m) {
        systemAndGUI("[WARNING] " + m);
    }
    
    private static void systemAndGUI(String m) {
        System.out.println(m);
        if (Main.server) Server.gui.log(m);
    }    
}
