package symbiote.world;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import symbiote.entity.client.ClientEntity;
import symbiote.entity.client.Drawable;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.resources.ImageUtil;

public class Block extends ClientEntity implements Drawable {

    public BufferedImage image;
    public String imageName;
    public boolean hitbox;
    
    public Block(int id, double x, double y, String block, boolean hitbox) {
        super(id, x, y);
        image = ImageUtil.getImage(block + ".png");
        imageName = block;
        width = 32;
        height = 32;
        this.hitbox = hitbox;
        usePhysics = false;
        if (!hitbox) {
            renderType = RenderType.HIGHEST;
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
    }

    @Override
    public Shape getCollisionBox() {
        if (hitbox) {
            return new Rectangle(Util.round(x), Util.round(y + (height / 2)), Util.round(width), Util.round(height / 2));
        } else {
            return new Rectangle(-1, -1, 0, 0);
        }
    }

    @Override
    public AbstractPacket getPacket() {
        return null;
    }
    
}
