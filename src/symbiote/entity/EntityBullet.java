package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketBullet;

public class EntityBullet extends AbstractEntity {
    public String owner = null;

    public EntityBullet(int id, double x, double y, double angle, String owner) {
        super(id, x, y);
        this.angle = angle;
        this.owner = owner;
        velocityDecrease = false;
        speed = 8;
        xVel = speed * Math.sin(Math.toRadians(angle));
        yVel = speed * -Math.cos(Math.toRadians(angle));
        
        this.width = 5;
        this.height = 10;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (x > 800 || x < 0 || y > 600 || y < 0) {
            destroy();
        }
    }
    
    /**
     * Don't hit the owner
     */
    @Override
    public boolean intersects(AbstractEntity t) {
        if (t instanceof EntityPlayer) {
            if (((EntityPlayer) t).name.equals(owner))
                return false;
        }
        
        return super.intersects(t);
    }

    @Override
    public AbstractPacket getPacket() {
        return new SPacketBullet(id, x, y, angle, owner);
    }
}
