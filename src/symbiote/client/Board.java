package symbiote.client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
    
    public Timer timer;
    public static double offsetX = 0;
    public static double offsetY = 0;
    
    public Board(Client c) {
        timer = new Timer(25, this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        
        Graphics2D g = (Graphics2D) graphics;
        
        if (Client.focus != null) {
            offsetX = Client.focus.getCenterX() - (Client.self.getWidth() / 2);
            offsetY = Client.focus.getCenterY() - (Client.self.getHeight() / 2);
        }
        
        g.translate(-offsetX, -offsetY);
        g.fillRect(320, 320, 10, 10);
        Client.screen.tick();
        Client.screen.draw(g);
        
        graphics.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent a) {
        repaint();
    }
}

