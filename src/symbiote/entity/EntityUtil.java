package symbiote.entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import symbiote.client.Client;
import symbiote.misc.Util;

public class EntityUtil {
    public static List<AbstractEntity> getEntitiesAt(Point p) {
        List<AbstractEntity> list = new ArrayList<>();
        for (AbstractEntity e : Client.screen.thingMap.values()) {
            if (e.intersects(p)) {
                list.add(e);
            }
        }
        
        return list;
    }
    
    public static List<AbstractEntity> getEntitiesAt(int x, int y) {return getEntitiesAt(new Point(x, y));}
    public static List<AbstractEntity> getEntitiesAt(double x, double y) {return getEntitiesAt(Util.round(x), Util.round(y));}
    
    public static List<AbstractEntity> getEntitiesIn(int rX, int rY, int width, int height) {
        return getEntitiesIn(new Rectangle(rX, rY, width, height));
    }
    
    public static List<AbstractEntity> getEntitiesIn(Shape s) {
        List<AbstractEntity> entities = new ArrayList<>();
        for (AbstractEntity e : Client.screen.thingMap.values()) {
            if (s.contains(e.getCollisionBoxCenterX(), e.getCollisionBoxCenterY())) {
                entities.add(e);
            }
        }
        return entities;
    }
    
    public static List<AbstractEntity> getEntitiesOfType(Class<? extends AbstractEntity> c) {
        List<AbstractEntity> list = new ArrayList<>();
        for (AbstractEntity e : Client.screen.thingMap.values()) {
            if (c.isInstance(e)) {
                list.add(e);
            }
        }
        return list;
    }
}
