package symbiote.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityUtil;
import symbiote.misc.Util;

public class Floor extends Block {
    
    public Floor(int id, double x, double y, String imageName) {
        super(id, x, y, imageName);
        
        renderType = RenderType.BACKGROUND;  
    }
    
    @Override
    protected Shape getNewCollisionBox() {
        return new Rectangle(-1, -1, 0, 0);
    }
    
    @Override
    public void draw(Graphics2D g) {
        boolean above = false;
        for (AbstractEntity e : EntityUtil.getEntitiesAt(x, y-2)) {
            if (e instanceof Wall) {
                above = true;
                break;
            }
        }
        g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
        if (above) { //Darken the image
            Color oldC = g.getColor();
            g.setColor(new Color(0, 0, 0, 70));
            g.fillRect(Util.round(x), Util.round(y), Util.round(width), Util.round(height));
            g.setColor(oldC);          
        }
    } 
}
