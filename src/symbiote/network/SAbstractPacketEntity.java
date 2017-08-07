package symbiote.network;

/**
 * Directive from Server to create an entity, abstract
 */
public abstract class SAbstractPacketEntity extends AbstractPacket {
    private int id;
    private double x;
    private double y;
    private double angle;
    
    public SAbstractPacketEntity(int id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
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
