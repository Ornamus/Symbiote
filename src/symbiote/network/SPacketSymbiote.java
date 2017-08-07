package symbiote.network;

import symbiote.client.Client;
import symbiote.entity.client.ClientEntitySymbiote;
import symbiote.entity.client.ClientEntityThisSymbiote;
import symbiote.network.Communicator.Type;

public class SPacketSymbiote extends SAbstractPacketEntity {
    boolean dontUse;
    private boolean you;
    String name;
    
    public SPacketSymbiote(int id, String name, double x, double y, double angle, boolean dontUse, boolean you) {
        super(id, x, y, angle);
        
        this.name = name;
        this.dontUse = dontUse;
        this.you = you;
    }
    
    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            ClientEntitySymbiote sim;
            if (this.you) {
                sim = new ClientEntityThisSymbiote(this.getId(), name, 0, 0);
                Client.focus = sim;
            } else {
                sim = new ClientEntitySymbiote(this.getId(), name, 0, 0);
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
