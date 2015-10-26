package symbiote.client.screen;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import symbiote.client.InputListener;
import symbiote.entity.AbstractEntity;
import symbiote.entity.client.Drawable;
import symbiote.entity.client.Interactable;
import symbiote.misc.Util;

public class Screen {
    public Map<Integer, AbstractEntity> thingMap = new ConcurrentHashMap<>();
    
     /**
     * The looping code of this Screen. Generally used for game logic.
     */
    public void tick() {
        for (AbstractEntity t : thingMap.values()) {
            t.tick();
        }
        for (AbstractEntity t : thingMap.values()) {
            if (InputListener.mouseHeld && t instanceof Interactable) {
                Point p = Util.getMouseOnScreen();
                ((Interactable) t).mouseHeld(p.x, p.y, InputListener.mouseHeldEvent);
            }
        }
    }
    
    public void draw(Graphics2D g) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Drawable)
                ((Drawable) t).draw(g);
        }
    }
    
    //TODO: IMPLEMENT
    public void mouseEnter() {}   
    public void mouseLeave() {}   
    
    public void mouseClicked(int x, int y, MouseEvent m) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).mouseClicked(x, y, m);
        }
    }  
    
    public void mouseHeld(int x, int y, MouseEvent m) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).mouseHeld(x, y, m);
        }
    }  
    
    public void mouseReleased(int x, int y, MouseEvent m) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).mouseReleased(x, y, m);
        }
    }
    
    public void keyPressed(KeyEvent k) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).keyPressed(k);
        }
    }
    
    public void keyReleased(KeyEvent k) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).keyReleased(k);
        }
    }
}
