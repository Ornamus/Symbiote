package symbiote.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import symbiote.misc.Util;

public class LivingEntity extends Entity {
    
    public double maxHealth = 100;
    public double health = 100;
    public String name = "";
    public boolean symbioteControlled = false;
    
    public LivingEntity(double x, double y) {
        super(x, y);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (health < 0) health = 0;
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
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
