package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketSymbiote;

public class EntitySymbiote extends LivingEntity {
    public LivingEntity controlledEntity;
    public boolean playing = true;
    
    public EntitySymbiote(int id, double x, double y) {
        super(id, x, y);
        
        controlledEntity = this;
        name = "Symbiote";
    }
    
    @Override
    public AbstractPacket getPacket() {
        return new SPacketSymbiote(id, x, y, angle, false, false);
    }
}
