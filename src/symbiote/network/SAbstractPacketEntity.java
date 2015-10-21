package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Directive from Server to create an entity, abstract
 */
public abstract class SAbstractPacketEntity extends AbstractPacket {
    private int id;
    private double x;
    private double y;
    private double angle;
    
    public SAbstractPacketEntity() {
    }
    
    public SAbstractPacketEntity(int id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeInt(id);
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.angle);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.id = in.readInt();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.angle = in.readDouble();
    }
    
    public int getId() {
        return this.id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }
}
