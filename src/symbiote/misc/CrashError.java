package symbiote.misc;

import javax.swing.JOptionPane;

public class CrashError  {
    
    public CrashError(String message) {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }    
}
