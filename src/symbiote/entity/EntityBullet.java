package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketBullet;

public class EntityBullet extends AbstractEntity {
    public int ownerID = -1;

    public EntityBullet(int id, double x, double y, double angle, int oID) {
        super(id, x, y);
        this.angle = angle;
        this.ownerID = oID;
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
        
        //TODO: figure out a better way of managing bullets instead of deleting them if off-screen
        if (x > 800 || x < 0 || y > 600 || y < 0) {
            destroy();
        }
    }
    
    /**
     * Don't hit the owner
     */
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
