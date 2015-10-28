package symbiote.world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.client.ClientEntity;
import symbiote.entity.client.Drawable;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.resources.ImageHandler;

public class BackgroundTile extends ClientEntity implements Drawable {

    public BufferedImage normal;
    public BufferedImage shadow;
    
    public BackgroundTile(int id, double x, double y, String imageName) {
        super(id, x, y);
        normal = ImageHandler.getImage(imageName + ".png");
        shadow = ImageHandler.getImage(imageName + "Shadow.png");
        
        width = 32;
        height = 32;
        foreground = false;
        usePhysics = false;
    }
    
    @Override
    public void draw(Graphics2D g) {
        boolean above = false;
        for (AbstractEntity e : Client.screen.thingMap.values()) {
            if (e instanceof Block && e.getBounds().contains(x, y-1)) {
                above = true;
                break;
            }
        }
        g.drawImage(above ? shadow : normal, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
    }

    @Override
    public AbstractPacket getPacket() {
        return null;
    }   
}
