package symbiote.entity;

import com.sun.glass.ui.Pixels;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import symbiote.misc.Util;
import symbiote.resources.ImageHandler;
import symbiote.resources.Pixel;

public class LivingEntity extends Entity {
    
    public double maxHealth = 100;
    public double health = 100;
    public String name = "";
    public boolean symbioteControlled = false;
    
    public LivingEntity(double x, double y) {
        super(x, y);
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (maxHealth > health) {
            drawHealth(x, y - 42, g);
        }
    }
    
    public void drawHealth(double dX, double dY, Graphics2D g) {
        int healthBarWidth = 32;
        
        int iX = Util.round(dX);
        int iY = Util.round(dY);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(iX - 1, iY - 12, 34, 8);
        g.setColor(Color.red);
        g.drawRect(iX, iY - 11, healthBarWidth, 6);
        g.fillRect(iX, iY - 11, healthBarWidth, 6);
        g.setColor(new Color(0, 190, 0));
        double greenBarWidth = ((health) / maxHealth) * healthBarWidth;
        g.drawRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.fillRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.setColor(new Color(0, 0, 0));
    }
    
    public Object[] getPacket() {
        return new Object[]{};
    }
}
