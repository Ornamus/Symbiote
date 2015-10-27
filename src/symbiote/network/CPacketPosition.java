package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.network.Communicator.Type;
import symbiote.server.Server;

public class CPacketPosition extends AbstractPacket {
    @Override
    public String toString() {
        return "CPacketPosition [id=" + id + ", x=" + x + ", y=" + y
                + ", angle=" + angle + "]";
    }

    int id;
    double x;
    double y;
    double angle;
    
    public CPacketPosition() {
    }
    
    public CPacketPosition(int id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeInt(id);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(angle);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.id = in.readInt();
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.angle = in.readDouble();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.SERVERSIDE) {
            // TODO: server should not be sending client CPackets
            Server.broadcastExcept(this, comm);
            System.out.println("BROADCASTEM");
        }
        
        AbstractEntity p;
        if (comm.getType() == Type.CLIENTSIDE) {
            p = Client.screen.thingMap.get(this.id);
        } else {
            p = Server.entities.get(this.id);
        }
        
        if (p != null) {
            p.setPositionAndAngle(this.x, this.y, this.angle);
        }
    }

}
