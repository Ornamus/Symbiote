package symbiote.world;

import java.util.ArrayList;
import java.util.List;
import symbiote.entity.AbstractEntity;

public class WorldUtil {
    
    public static List<AbstractEntity> createWallSegment(double x, double botY, int height) {
        List<AbstractEntity> entities = new ArrayList<>();
        int progress = 0;
        for (int i=height-1;i>-1;i--) {
            String block = "WallFront";
            if (i == 0) {
                block = "WallTransition";
            }
            entities.add(new Block(999 + AbstractEntity.getNextID(), x, botY - (progress * 32), block));
            progress++; 
        }
        return entities;
    }
    
    public static List<AbstractEntity> createWall(double startX, double botY, int width, int height) {
        List<AbstractEntity> entities = new ArrayList<>();
        for (int i=0;i<width;i++) {
            for (AbstractEntity e : createWallSegment(startX + (i * 32), botY, height)) {
                entities.add(e);
            }
        }
        return entities;
    }
    
    public static List<AbstractEntity> createFloor(double startX, double startY, double width, double height) {
        List<AbstractEntity> entities = new ArrayList<>();
        for (int xi=0;xi<width;xi++) {
            for (int yi=0;yi<height;yi++) {
                entities.add(new BackgroundTile(999 + AbstractEntity.getNextID(), startX + (xi*32), startY + (yi*32), "Floor"));
            }
        }
        return entities;
    }
}
