package symbiote.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import symbiote.Main;
import symbiote.misc.Util;

public class InputListener implements MouseListener, KeyListener {
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Main.screen.mouseClicked(p.x, p.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Main.screen.mouseReleased(p.x, p.y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //TODO implement
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //TODO implement
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Main.screen.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Main.screen.keyReleased(e);
    }  
}
