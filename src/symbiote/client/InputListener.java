package symbiote.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import symbiote.entity.client.Interactable;
import symbiote.misc.Util;

public class InputListener implements MouseListener, KeyListener {
    
    public static boolean mouseHeld = false;
    public static MouseEvent mouseHeldEvent = null;
    public static List<Interactable> extras = new ArrayList<>();
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Client.screen.mouseClicked(p.x, p.y, e);
        for (Interactable i : extras) {
            i.mouseClicked(p.x, p.y, e);
        }
        mouseHeld = true;
        mouseHeldEvent = e;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        Client.screen.mouseReleased(p.x, p.y, e);
        for (Interactable i : extras) {
            i.mouseReleased(p.x, p.y, e);
        }
        mouseHeld = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Client.screen.mouseEnter();
        for (Interactable i : extras) {
            i.mouseEnter();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Client.screen.mouseLeave();
        for (Interactable i : extras) {
            i.mouseLeave();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Client.screen.keyPressed(e);
        for (Interactable i : extras) {
            i.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Client.screen.keyReleased(e);
        for (Interactable i : extras) {
            i.keyReleased(e);
        }
    }  
}
