package symbiote.world;

import symbiote.Main;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityUtil;
import symbiote.misc.Log;
import symbiote.server.Server;
import symbiote.world.Furniture.FurnitureType;
import symbiote.world.Wall.WallHitbox;

public class WorldUtil {
    
    public static void createWallSegment(double x, double botY, int height, int thickness) {
        int progress = 0;
        thickness--;
        for (int i=height+thickness-1;i>-1;i--) {
            String block = "WallFront";
            if (i == thickness) {
                block = "WallTransition";
            } else if (i < thickness) {
                block = "WallTop";
            }
            int hitboxType = WallHitbox.FULL;
            if (i == 0) {
                hitboxType = WallHitbox.NONE;
            } else if (i == 1) {
                hitboxType = WallHitbox.THIN;
            }
            Block b = new Wall(AbstractEntity.getNextID(), x, botY - (progress * 32), block, hitboxType);
            if (Main.server) {
                Server.entities.put(b.id, b);
            } else {
                Client.screen.thingMap.put(b.id, b);
                Log.w("Using server-side world generation code on the client!");
            }
            progress++; 
        }
    }
    
    public static void createWall(double startX, double botY, int width, int height, int thickness) {
        for (int i=0;i<width;i++) {
            createWallSegment(startX + (i * 32), botY, height, thickness);
        }
    }
    
    public static void createFloor(double startX, double startY, double width, double height) {
        for (int xi = 0; xi < width; xi++) {
            for (int yi = 0; yi < height; yi++) {
                Floor t = new Floor(AbstractEntity.getNextID(), startX + (xi * 32), startY + (yi * 32), "Floor");
                if (Main.server) {
                    Server.entities.put(t.id, t);
                } else {
                    Client.screen.thingMap.put(t.id, t);
                    Log.w("Using server-side world generation code on the client!");
                }
            }
        }
    }
    
    public static void createTable(double tX, double tY, boolean lChair, boolean rChair) {
        Block t = new Furniture(AbstractEntity.getNextID(), tX, tY, FurnitureType.TABLE);
        EntityUtil.getEntityMap().put(t.id, t);
        if (lChair) {
            t = new Furniture(AbstractEntity.getNextID(), tX - 32, tY, FurnitureType.RIGHT_CHAIR);
            EntityUtil.getEntityMap().put(t.id, t);
        }
        if (rChair) {
            t = new Furniture(AbstractEntity.getNextID(), tX + 32, tY, FurnitureType.LEFT_CHAIR);
            EntityUtil.getEntityMap().put(t.id, t);
        }
        if (Main.client) Log.w("Using server-side world generation code on the client!");
    }
}
