package symbiote.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import symbiote.Main;
import symbiote.client.Client;
import symbiote.entity.client.ClientEntity;
import symbiote.entity.client.Drawable;
import symbiote.misc.Log;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.resources.ImageUtil;

public class Block extends ClientEntity implements Drawable {

    public BufferedImage image;
    public String imageName;
    public Integer blockID;
    
    public Block(int id, double x, double y, String block) {
        super(id, x, y);
        image = ImageUtil.getImage(block + ".png");
        imageName = block;
        width = 32;
        height = 32;
        usePhysics = false;
        blockID = Main.blockToID.get(this.getClass());
        if (blockID == null) {
            Log.e("\"" + this.getClass().getSimpleName() + "\" does not have a block ID! Add it with Main.addBlock()!");
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
        if (Client.DEBUG) {
            Color oldColor = g.getColor();
            g.setColor(Color.red);
            Rectangle r = getCollisionBox().getBounds();
            g.drawRect(r.x, r.y, r.width, r.height);
            g.setColor(oldColor);
        }
    }
    
        
    public String getStringData() {
        return blockID + ":" + id + ":" + x + ":" + y + ":" + imageName;
    }

    @Override
    public AbstractPacket getPacket() {
        return null;
    }
}
