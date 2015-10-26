package symbiote.entity.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import symbiote.entity.EntityPlayer;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.misc.Util;

public class ClientEntityPlayer extends EntityPlayer implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public boolean playing = false;
    
    public ClientEntityPlayer(int id, String name, double x, double y) {
        super(id, name, x, y);
        animation = AnimationFactory.start().addFrame("body.png").loop(true).finish();
        size = 2;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.image = animation.getCurrentFrame(true);
    }
    
    @Override
    public Shape getCollisionBox() {
        //This hitbox is just the shadow at the person's feet, to give a correct feel of depth
        return new Rectangle2D.Double(x, y + (21 * size), width * size, 4 * size);
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            g.drawImage(image, Util.round(x), Util.round(y), Util.round(image.getWidth()*size), Util.round(image.getHeight()*size), null);
            //ImageHandler.drawRotated(image, x, y, angle, g);
        }
        
        if (maxHealth > health) {
            drawHealth(x, y - 42, g);
        }
        
        BufferedImage frame = animation.getCurrentFrame(false);
        int textWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, Util.round((x + (frame.getWidth() / 2) - (textWidth / 2))), Util.round((y - (frame.getHeight() / 2))));
    }
    
    /**
     * Draws this LivingEntity's health bar at dX and dY.
     */
    public void drawHealth(double dX, double dY, Graphics2D g) {
        int healthBarWidth = 32;
        
        int iX = Util.round(dX);
        int iY = Util.round(dY);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(iX - 1, iY - 12, healthBarWidth + 2, 8);
        g.setColor(Color.red);
        g.drawRect(iX, iY - 11, healthBarWidth, 6);
        g.fillRect(iX, iY - 11, healthBarWidth, 6);
        g.setColor(new Color(0, 190, 0));
        double greenBarWidth = ((health) / maxHealth) * healthBarWidth;
        g.drawRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.fillRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.setColor(new Color(0, 0, 0));
    }
}
