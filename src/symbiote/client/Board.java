package symbiote.client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import symbiote.Main;
import symbiote.entity.EntityPlayer;
import symbiote.entity.EntitySymbiote;

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
        
        if (Client.symbiote) {
            EntitySymbiote player = EntitySymbiote.get();
            if (player != null) {
                offsetX = player.controlledEntity.getCenterX() - (Client.self.getWidth() / 2);
                offsetY = player.controlledEntity.getCenterY() - (Client.self.getHeight() / 2);
                g.translate(-offsetX, -offsetY);
            }
        } else {
            EntityPlayer player = EntityPlayer.get(Client.name);
            if (player != null) {
                offsetX = player.getCenterX() - (Client.self.getWidth() / 2);
                offsetY = player.getCenterY() - (Client.self.getHeight() / 2);
                g.translate(-offsetX, -offsetY);
            }
        }
        
        Main.screen.tick();
        Main.screen.draw(g);
        
        graphics.dispose();
        
    }
    
    @Override
    public void actionPerformed(ActionEvent a) {
        repaint();
    }
}

