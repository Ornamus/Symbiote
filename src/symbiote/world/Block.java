package symbiote.world;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.client.ClientEntity;
import symbiote.entity.client.Drawable;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.resources.ImageHandler;

public class Block extends ClientEntity implements Drawable {

    public BufferedImage image;
    public String imageName;
    
    public Block(int id, double x, double y, String block) {
        super(id, x, y);
        image = ImageHandler.getImage(block + ".png");
        imageName = block;
        width = 32;
        height = 32;
        usePhysics = false;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
    }

    //TODO: Change so wall hitbox thickness is one block + one for every WallTop
    @Override
    public Shape getCollisionBox() {
        boolean above = false;
        boolean aboveIsTop = true;
        String topType = null;
        for (AbstractEntity a : Client.screen.thingMap.values()) {
            if (a instanceof Block) {
                Rectangle2D r = new Rectangle2D.Double(a.x, a.y, a.width, a.height);
                if (r.contains(x, y - 1)) {
                    above = true;
                    topType = ((Block)a).imageName;
                } else if (r.contains(x, y - 33)) {
                    aboveIsTop = false;
                }
                if (above && !aboveIsTop) break;              
            }
        }
        
        Rectangle fullBox = new Rectangle(Util.round(x), Util.round(y), Util.round(width), Util.round(height));
        Rectangle smallBox = new Rectangle(Util.round(x), Util.round(y) + 29, Util.round(width), 3);
        Rectangle noBox = new Rectangle(-1, -1, 0, 0);
        
        if (above) {
            if (aboveIsTop) {
                if (topType.equals("WallTop")) {
                    return fullBox;
                } else {
                    return smallBox;
                }
            } else {
                return fullBox;
            }
        } else {
            if (imageName.equals("WallTop")) {
                return smallBox;
            } else {
                return noBox;
            }
        }
        /*
        if (!above) {
            return new Rectangle(Util.round(x), Util.round(y) + 29, Util.round(width), 3);
        } else {
            return new Rectangle(Util.round(x), Util.round(y), Util.round(width), Util.round(height));
        }*/
    }

    @Override
    public AbstractPacket getPacket() {
        return null;
    }
    
}
