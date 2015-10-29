package symbiote.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityUtil;
import symbiote.entity.client.ClientEntity;
import symbiote.entity.client.Drawable;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.resources.ImageHandler;

public class BackgroundTile extends ClientEntity implements Drawable {

    public BufferedImage image;
    
    public BackgroundTile(int id, double x, double y, String imageName) {
        super(id, x, y);
        image = ImageHandler.getImage(imageName + ".png");
        
        width = 32;
        height = 32;
        renderType = RenderType.BACKGROUND;
        usePhysics = false;
    }
    
    @Override
    public void draw(Graphics2D g) {
        boolean above = false;
        for (AbstractEntity e : EntityUtil.getEntitiesAt(x, y-2)) {
            if (e instanceof Block) {
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

    @Override
    public AbstractPacket getPacket() {
        return null;
    }   
}
