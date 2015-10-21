package symbiote.client.screen;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import symbiote.entity.AbstractEntity;
import symbiote.entity.client.Drawable;
import symbiote.entity.client.Interactable;

public class Screen {
    public Map<Integer, AbstractEntity> thingMap = new ConcurrentHashMap<>();
    
     /**
     * The looping code of this Screen. Generally used for game logic.
     */
    public void tick() {
        for (AbstractEntity t : thingMap.values()) {
            t.tick();
        }
    }
    
    public void draw(Graphics2D g) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Drawable)
                ((Drawable) t).draw(g);
        }
    }
    
    public void mouseEnter(MouseEvent e) {}   
    public void mouseLeave(MouseEvent e) {}   
    
    public void mouseClicked(int x, int y) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).mouseClicked(x, y);
        }
    }  
    
    public void mouseReleased(int x, int y) {
        for (AbstractEntity t : thingMap.values()) {
            if (t instanceof Interactable)
                ((Interactable) t).mouseReleased(x, y);
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
