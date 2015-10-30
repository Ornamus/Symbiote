package symbiote.entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import symbiote.Main;
import symbiote.client.Client;
import symbiote.misc.Util;
import symbiote.server.Server;

public class EntityUtil {
    
    public static List<AbstractEntity> getEntitiesAt(Point p) {
        List<AbstractEntity> list = new ArrayList<>();
        for (AbstractEntity e : Main.server ? Server.entities.values() : Client.screen.thingMap.values()) {
            if (e.intersects(p)) {
                list.add(e);
            }
        }       
        return list;
    }
    
    public static List<AbstractEntity> getEntitiesAt(int x, int y) {return getEntitiesAt(new Point(x, y));}
    public static List<AbstractEntity> getEntitiesAt(double x, double y) {return getEntitiesAt(Util.round(x), Util.round(y));}
    
    public static List<AbstractEntity> getEntitiesOfType(Class<? extends AbstractEntity> c) {
        List<AbstractEntity> list = new ArrayList<>();
        for (AbstractEntity e : getEntities()) {
            if (c.isInstance(e)) {
                list.add(e);
            }
        }
        return list;
    }
    
    public static EntitySymbiote getSymbiote() {
        for (AbstractEntity e : getEntities()) {
            if (e instanceof EntitySymbiote) {
                return (EntitySymbiote) e;
            }
        }
        return null;
    }

    public static EntityPlayer getPlayer(String name) {
        for (AbstractEntity e : getEntities()) {
            if (e instanceof EntityPlayer) {
                if (((EntityPlayer) e).name.equals(name)) {
                    return (EntityPlayer) e;
                }
            }
        }
        return null;
    }
    
    public static Map<Integer, AbstractEntity> getEntityMap() {
        return Main.server ? Server.entities : Client.screen.thingMap;
    }
    
    public static Collection<AbstractEntity> getEntities() {
        return getEntityMap().values();
    }
}
