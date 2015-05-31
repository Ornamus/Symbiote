package symbiote.screen;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import symbiote.Thing;

public class Screen {
    
    public List<Thing> addThings = new ArrayList<>();
    public List<Thing> things = new ArrayList<>();
    public List<Thing> removeThings = new ArrayList<>();
    
     /**
     * The looping code of this Screen. Generally used for game logic.
     */
    public void tick() {
        for (Thing t : new ArrayList<>(addThings)) {
            things.add(t);
        }
        for (Thing t : new ArrayList<>(removeThings)) {
            things.remove(t);
        }
        addThings.clear();
        removeThings.clear();
        for (Thing t : things) {
            t.tick();
        }
    }
    
    public void draw(Graphics2D g) {
        for (Thing t : things) {
            t.draw(g);
        }
    }
    
    public void mouseEnter() {}   
    public void mouseLeave() {}   
    
    public void mouseClicked(int x, int y) {
        for (Thing t : things) {
            t.mouseClicked(x, y);
        }
    }  
    
    public void mouseReleased(int x, int y) {
        for (Thing t : things) {
            t.mouseReleased(x, y);
        }
    }
    
    public void keyPressed(KeyEvent k) {
        for (Thing t : things) {
            t.keyPressed(k);
        }
    }
    
    public void keyReleased(KeyEvent k) {
        for (Thing t : things) {
            t.keyReleased(k);
        }
    }
}
