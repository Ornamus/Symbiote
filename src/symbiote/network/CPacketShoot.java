package symbiote.network;

import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityBullet;
import symbiote.server.Server;

public class CPacketShoot extends AbstractPacket {
    @Override
    public String toString() {
        return "CPacketShoot [x=" + x + ", y=" + y + ", angle=" + angle
                + ", ownerID=" + owner + "]";
    }
    
    double x;
    double y;
    double angle;
    int owner;
    
    public CPacketShoot(double x, double y, double angle, int owner) {
        super();
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.owner = owner;
    }

    @Override
    public void handle(Communicator comm) {
        EntityBullet b = new EntityBullet(AbstractEntity.getNextID(), x, y, angle, owner);
        
        Server.broadcast(b.getPacket());
        Server.entities.put(b.id, b);
    }
}
