package symbiote.world.rooms;

import java.util.ArrayList;
import java.util.List;
import symbiote.Thing;

public class ThingCollection {
    
    public double x,y;
    public List<Thing> things = new ArrayList<>();
    
    public ThingCollection(double x, double y, List<Thing> things) {
        this.x = x;
        this.y = y;
        this.things = new ArrayList<>(things);
    }
    
    /**
     * Adds x and y to the ThingCollection's coordinates and to the coordinates of all of it's Things.
     */
    public void add(double x, double y) {
        this.x = x;
        this.y = y;
        for (Thing t : things) {
            t.x += x;
            t.y += y;
        }
    }
}
