package symbiote.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import symbiote.network.AbstractPacket;
import symbiote.network.SPacketBullet;

public class EntityBullet extends AbstractEntity {
    public int ownerID = -1;
    Timer deathTimer;

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
        
        deathTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destroy();
            }        
        });
        
        deathTimer.setRepeats(false);
        deathTimer.start();

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
