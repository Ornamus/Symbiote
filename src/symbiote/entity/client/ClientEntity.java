package symbiote.entity.client;

import symbiote.entity.AbstractEntity;
import symbiote.network.AbstractPacket;

public abstract class ClientEntity extends AbstractEntity implements ComplexDrawable {

    //TODO: Find a way to get a standard hierarchy for all client entities while still letting them extend their serverside counterparts


    public ClientEntity(int id, double x, double y) {
        super(id, x, y);
    }    
    
    public abstract AbstractPacket getPacket();
    
}
