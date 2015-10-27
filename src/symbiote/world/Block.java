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
    
    public Block(int id, double x, double y, String block) {
        super(id, x, y);
        image = ImageHandler.getImage(block + ".png");
        width = 32;
        height = 32;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
    }
    
    @Override
    public Shape getCollisionBox() {
        boolean above = false;
        for (AbstractEntity a : Client.screen.thingMap.values()) {
            if (a instanceof Block) {
                Rectangle2D r = new Rectangle2D.Double(a.x, a.y, a.width, a.height);
                if (r.contains(x, y - 1)) {
                    above = true;
                    break;
                }
            }
        }
        if (!above) {
            return new Rectangle(Util.round(x), Util.round(y) + 29, Util.round(width), 3);
        } else {
            return new Rectangle(Util.round(x), Util.round(y), Util.round(width), Util.round(height));
        }
    }

    @Override
    public AbstractPacket getPacket() {
        return null;
    }
    
}
