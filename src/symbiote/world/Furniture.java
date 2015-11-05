package symbiote.world;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import symbiote.misc.Log;
import symbiote.resources.ImageUtil;

public class Furniture extends Block {
    
    public int type;
    
    public Furniture(int id, double sX, double sY, int t) {
        super(id, sX, sY, "chair");
        type = t;
        
        if (type == FurnitureType.LEFT_CHAIR || type == FurnitureType.RIGHT_CHAIR) {
            y -= 16;
            width = 32;
            height = 64;
            if (type == FurnitureType.LEFT_CHAIR) {
                image = ImageUtil.flipImage(image);
            }
        } else if (type == FurnitureType.TABLE) {
            width = 32;
            height = 32;
            image = ImageUtil.getImage("table.png");
            imageName = "table";
        }
    }
    
    @Override
    protected Shape getNewCollisionBox() {
        if (type == FurnitureType.RIGHT_CHAIR || type == FurnitureType.LEFT_CHAIR) {
            return new Rectangle2D.Double(x, y + 54, width, 10); //The shadow and down
        } else if (type == FurnitureType.TABLE) {
            return new Rectangle2D.Double(x, y + 22, width, 10); //The shadow and down
        } else {
            Log.e("Furniture has invalid furniture type \"" + type + "\"!");
            return null;
        }
    }
    
    @Override
    public String getStringData() {
        return blockID + ":" + id + ":" + x + ":" + y + ":" + type;
    }
    
    public class FurnitureType {
        public final static int RIGHT_CHAIR = 0;
        public final static int LEFT_CHAIR = 1;
        public final static int TABLE = 2;
    }
}
