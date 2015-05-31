package symbiote.world.rooms;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import symbiote.Thing;
import symbiote.world.Wall;

public class Hallway extends ThingCollection {
    
    public Area area;
    public Exit exit;
    
    public Hallway(Rectangle r, boolean horizontal) { //TODO make an exit at the other end of the hallway
        super(r.x, r.y, new ArrayList<>());
        if (horizontal) {
            for (int cX = r.x; cX < r.getWidth() + r.x; cX += Room.gridUnit) {things.add(new Wall(cX, r.y, "wallSheet.png"));}
            for (int cX = r.x; cX < r.getWidth() + r.x; cX += Room.gridUnit) {things.add(new Wall(cX, r.y + r.height - Room.gridUnit, "wallSheet.png"));}
        } else {
            for (int cY=r.y; cY < r.getHeight() + r.y; cY += Room.gridUnit) {things.add(new Wall(r.x, cY, "wallSheet.png"));}
            for (int cY=r.y; cY < r.getHeight() + r.y; cY += Room.gridUnit) {things.add(new Wall(r.x + r.width - Room.gridUnit, cY, "wallSheet.png"));}
        }
        area = new Area(r);
    }
    
    public boolean intersects(Room r, boolean useBorder) {
        Area a = (Area) area.clone();
        a.intersect(r.getArea(useBorder));
        return !a.isEmpty();
    }
    
    //TODO make this not terrible
    public void merge(Hallway h) {
        List<Thing> newThings = new ArrayList<>();
        for (Thing t : things) {
            newThings.add(t);
        }
        for (Thing t : h.things) {
            newThings.add(t);
        }
        Area intersect = ((Area) area.clone());
        intersect.add((Area) h.area.clone());
        if (!intersect.isEmpty()) {
            Rectangle r = intersect.getBounds();
            for (int cY = r.x; cY < r.getHeight() + r.y; cY += Room.gridUnit) {
                for (int cX = r.x; cX < r.getWidth() + r.x; cX += Room.gridUnit) {
                    if (intersect.contains(cX, cY)) {
                        for (Thing t : new ArrayList<>(newThings)) {
                            if (t.getCollisionBox().contains(cX, cY)) {
                                newThings.remove(t);
                            }
                        }
                    }
                }
            }
        }
        area.add(h.area);
        things = newThings;
    }
}
