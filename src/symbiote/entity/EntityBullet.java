package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketBullet;

public class EntityBullet extends AbstractEntity {
    public int ownerID = -1;
    
    public long shootTime;

    public EntityBullet(int id, double x, double y, double angle, int oID) {
        super(id, x, y);
        this.angle = angle;
        this.ownerID = oID;
        velocityDecrease = false;
        speed = 12;
        xVel = speed * Math.sin(Math.toRadians(angle));
        yVel = speed * -Math.cos(Math.toRadians(angle));
        
        angleRotate = true;
        
        this.width = 5;
        this.height = 10;
        
        shootTime = System.nanoTime();

    }
    
    @Override
    public void tick() {
        super.tick();
        if ((System.nanoTime() - shootTime) / 10e8 >= 5) {
            destroy();
        }   
    }
    
    @Override
    public void collide(AbstractEntity e) {
        boolean living = false;
        boolean hit = true;
        if (e instanceof LivingEntity) living = true;
        if (e.id == ownerID || e.renderType == RenderType.BACKGROUND) {
            hit = false;
        }
        if (hit) {
            if (living) {
                LivingEntity l = (LivingEntity) e;
                l.setHealth(l.getHealth() - 10);
            }
            destroy();
        }
    }
    
    @Override
    public boolean intersects(AbstractEntity t) {
        if (t.id == ownerID) {
            return false;
        }
        
        return super.intersects(t);
    }

    @Override
    public AbstractPacket getPacket() {
        return new SPacketBullet(id, x, y, angle, ownerID);
    }
}
