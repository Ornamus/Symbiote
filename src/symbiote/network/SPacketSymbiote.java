package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import symbiote.client.Client;
import symbiote.entity.client.ClientEntitySymbiote;
import symbiote.entity.client.ClientEntityThisSymbiote;
import symbiote.network.Communicator.Type;

public class SPacketSymbiote extends SAbstractPacketEntity {
    boolean dontUse;
    private boolean you;

    public SPacketSymbiote() {
    }
    
    public SPacketSymbiote(int id, double x, double y, double angle, boolean dontUse, boolean you) {
        super(id, x, y, angle);
        
        this.dontUse = dontUse;
        this.you = you;
    }

    @Override
    public void write(ObjectOutputStream out) throws Exception {
        super.write(out);
        
        out.writeBoolean(dontUse);
        out.writeBoolean(this.you);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        super.read(in);
        
        this.dontUse = in.readBoolean();
        this.you = in.readBoolean();
    }
    
    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            ClientEntitySymbiote sim;
            if (this.you) {
                sim = new ClientEntityThisSymbiote(this.getId(), 0, 0);
                sim.playing = true;
                Client.focus = sim;
            } else {
                sim = new ClientEntitySymbiote(this.getId(), 0, 0);
                sim.playing = false;
            }
            //sim.id = this.getId();
            Client.screen.thingMap.put(this.getId(), sim);
            
            sim.x = this.getX();
            sim.y = this.getY();
            sim.angle = this.getAngle();
            //sim.dontUse = this.dontUse;
        }
    }
    
    public boolean isYou() {
        return you;
    }

    public void setYou(boolean you) {
        this.you = you;
    }
}
