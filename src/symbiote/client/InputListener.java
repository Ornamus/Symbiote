package symbiote.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import symbiote.misc.Util;

public class InputListener implements MouseListener, KeyListener {
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Client.screen.mouseClicked(p.x, p.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Client.screen.mouseReleased(p.x, p.y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Client.screen.mouseEnter(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Client.screen.mouseLeave(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Client.screen.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Client.screen.keyReleased(e);
    }  
}
