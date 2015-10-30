package symbiote.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import symbiote.Main;
import symbiote.misc.Util;
import symbiote.network.SPacketEntityHealth;
import symbiote.server.Server;

public abstract class LivingEntity extends AbstractEntity {
    public double maxHealth = 100;
    private double health = 100;
    public String name = "";
    public boolean symbioteControlled = false;
    
    //TODO: Make sure health is always synced up between the server and clients
    
    public LivingEntity(int id, double x, double y) {
        super(id, x, y);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (health < 0) {
            health = 0;
            
            // TODO: create dead body when health == 0
        }
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double newHealth) {
        if (Main.server && health != newHealth) {
            Server.broadcast(new SPacketEntityHealth(this.id, newHealth));
        }
        health = newHealth;
    }

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
