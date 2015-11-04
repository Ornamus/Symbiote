package symbiote.world;

import symbiote.Main;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Log;
import symbiote.server.Server;

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
            Block b = new Block(AbstractEntity.getNextID(), x, botY - (progress * 32), block, (progress <= thickness));
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
                BackgroundTile t = new BackgroundTile(AbstractEntity.getNextID(), startX + (xi * 32), startY + (yi * 32), "Floor");
                if (Main.server) {
                    Server.entities.put(t.id, t);
                } else {
                    Client.screen.thingMap.put(t.id, t);
                    Log.w("Using server-side world generation code on the client!");
                }
            }
        }
    }
}
