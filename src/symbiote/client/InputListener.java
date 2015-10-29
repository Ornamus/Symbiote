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
    
    public static boolean leftMouseHeld = false;
    public static boolean middleMouseHeld = false;
    public static boolean rightMouseHeld = false;
    
    public static MouseEvent leftMouseEvent = null;
    public static MouseEvent middleMouseEvent = null;
    public static MouseEvent rightMouseEvent = null;

    public static List<Interactable> extras = new ArrayList<>();
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        if (Client.screen != null) Client.screen.mouseClicked(p.x, p.y, e);
        for (Interactable i : extras) {
            i.mouseClicked(p.x, p.y, e);
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMouseHeld = true;
            leftMouseEvent = e;
        }
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleMouseHeld = true;
            middleMouseEvent = e;
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseHeld = true;
            rightMouseEvent = e;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = Util.getMouseOnScreen();
        if (Client.screen != null) Client.screen.mouseReleased(p.x, p.y, e);
        for (Interactable i : extras) {
            i.mouseReleased(p.x, p.y, e);
        }
        if (e.getButton() == MouseEvent.BUTTON1) leftMouseHeld = false;
        if (e.getButton() == MouseEvent.BUTTON2) middleMouseHeld = false;
        if (e.getButton() == MouseEvent.BUTTON3) rightMouseHeld = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (Client.screen != null) Client.screen.mouseEnter();
        for (Interactable i : extras) {
            i.mouseEnter();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (Client.screen != null) Client.screen.mouseLeave();
        for (Interactable i : extras) {
            i.mouseLeave();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (Client.screen != null) Client.screen.keyPressed(e);
        for (Interactable i : extras) {
            i.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (Client.screen != null) Client.screen.keyReleased(e);
        for (Interactable i : extras) {
            i.keyReleased(e);
        }
    }  
}
