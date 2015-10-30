package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketSymbiote;

public class EntitySymbiote extends LivingEntity {
    public LivingEntity controlled;
    
    public EntitySymbiote(int id, String name, double x, double y) {
        super(id, x, y);
        
        controlled = this;
        this.name = name;
    }
    
    @Override
    public AbstractPacket getPacket() {
        return new SPacketSymbiote(id, name, x, y, angle, false, false);
    }
}
