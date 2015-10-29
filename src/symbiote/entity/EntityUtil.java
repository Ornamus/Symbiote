package symbiote.entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import symbiote.misc.Util;

public class EntityUtil {
    
    public static HashMap<Point, List<AbstractEntity>> entityCoords = new HashMap<>();   
    public static HashMap<Class, List<AbstractEntity>> entityClasses = new HashMap<>();
    
    //TODO: This currently only finds entities who's x and y are the exact coordinates being searched for, so make this find any
    //entity who's bounding box contains the coordinates (keep in mind, this is called super often, so just iterating through all
    //the entities WILL cause bad lag)
    public static List<AbstractEntity> getEntitiesAt(Point p) {
        List<AbstractEntity> entities = entityCoords.get(p);
        if (entities == null) entities = new ArrayList<>();
        return entities;
    }
    
    public static List<AbstractEntity> getEntitiesAt(int x, int y) {return getEntitiesAt(new Point(x, y));}
    public static List<AbstractEntity> getEntitiesAt(double x, double y) {return getEntitiesAt(Util.round(x), Util.round(y));}
    
    public static List<AbstractEntity> getEntitiesIn(int rX, int rY, int width, int height) {
        List<AbstractEntity> entities = new ArrayList<>();
        for (int x = rX; x < rX + width + 1; x++) {
            for (int y = rY; y < rY + width + 1; y++) {
                 for (AbstractEntity e : getEntitiesAt(x, y)) {
                     entities.add(e);
                 }   
            }
        }
        return entities;
    }
    
    public static List<AbstractEntity> getEntitiesIn(Shape s) {
        Rectangle r = s.getBounds();
        return getEntitiesIn(r.x, r.y, r.width, r.height);
    }
    
    public static List<AbstractEntity> getEntitiesOfType(Class c) {
        List<AbstractEntity> entities = entityClasses.get(c);
        if (entities == null) entities = new ArrayList<>();
        return entities;
    }
    
    public static void updateEntity(AbstractEntity e) {
        List<AbstractEntity> entities = getEntitiesAt(e.x, e.y); //Coordinates
        if (!entities.contains(e)) entities.add(e);
        entityCoords.put(new Point(Util.round(e.x), Util.round(e.y)), entities);

        entities = getEntitiesOfType(e.getClass()); //Classes
        if (!entities.contains(e)) entities.add(e);
        entityClasses.put(e.getClass(), entities);
    }
}
