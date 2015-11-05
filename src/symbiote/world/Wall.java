package symbiote.world;

import java.awt.Rectangle;
import java.awt.Shape;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Log;
import symbiote.misc.Util;

public class Wall extends Block {

    public int hitboxType;
    
    public Wall(int id, double x, double y, String block, int hitboxType) {
        super(id, x, y, block);
        this.hitboxType = hitboxType;
        if (hitboxType == 0) {
            renderType = AbstractEntity.RenderType.HIGHEST;
        }
    }
    
    @Override
    protected Shape getNewCollisionBox() {
        if (hitboxType == WallHitbox.NONE) {
            return new Rectangle(-1, -1, 0, 0);
        } else if (hitboxType == WallHitbox.THIN) {
            return new Rectangle(Util.round(x), Util.round(y + Util.round(height / 2)), Util.round(width), Util.round(height / 2));
        } else if (hitboxType == WallHitbox.FULL) {
            return new Rectangle(Util.round(x), Util.round(y), Util.round(width), Util.round(height));
        } else {
            Log.e("Wall has an invalid hitbox type of \"" + hitboxType + "\"!");
            return null;
        }
    }
    
    @Override
    public String getStringData() {
        return super.getStringData() + ":" + hitboxType;
    }
    
    public class WallHitbox {
        public final static int NONE = 0;
        public final static int THIN = 1;
        public final static int FULL = 2;
    }   
}
