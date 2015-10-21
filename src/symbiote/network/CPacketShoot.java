package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityBullet;
import symbiote.server.Server;

public class CPacketShoot extends AbstractPacket {
    @Override
    public String toString() {
        return "CPacketShoot [x=" + x + ", y=" + y + ", angle=" + angle
                + ", owner=" + owner + "]";
    }
    
    double x;
    double y;
    double angle;
    String owner;
    
    public CPacketShoot() {
    }
    
    public CPacketShoot(double x, double y, double angle, String owner) {
        super();
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.owner = owner;
    }

    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(angle);
        out.writeUTF(owner);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.angle = in.readDouble();
        this.owner = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        EntityBullet b = new EntityBullet(AbstractEntity.getNextID(), x, y, angle, owner);
        
        Server.broadcast(b.getPacket());
        Server.entities.put(b.id, b);
    }

}
