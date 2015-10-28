package symbiote.entity.client;

import symbiote.entity.AbstractEntity;
import symbiote.network.AbstractPacket;

public abstract class ClientEntity extends AbstractEntity {
    
    public ClientEntity(int id, double x, double y) {
        super(id, x, y);
    }    
    
    public abstract AbstractPacket getPacket();
    
}
