package symbiote.entity.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import symbiote.entity.EntitySymbiote;
import symbiote.misc.Util;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.resources.ImageHandler;

public class ClientEntitySymbiote extends EntitySymbiote implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public ClientEntitySymbiote(int id, double x, double y) {
        super(id, x, y);
        animation = AnimationFactory.start().addFrame("symbiote.png").loop(true).finish();
    }

    @Override
    public void tick() {
        super.tick();
        
        image = animation.getCurrentFrame(true);
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            ImageHandler.drawRotated(image, x, y, angle, g);
        }
        
        if (maxHealth > health) {
            drawHealth(x, y - 42, g);
        }
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
