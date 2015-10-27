package symbiote.entity.client;

import symbiote.entity.AbstractEntity;
import symbiote.network.AbstractPacket;

public abstract class ClientEntity extends AbstractEntity { //An entity is an object that moves or has physics.
    
    public ClientEntity(int id, double x, double y) {
        super(id, x, y);
    }    
    
    public abstract AbstractPacket getPacket();
    
}
