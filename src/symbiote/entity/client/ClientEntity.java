package symbiote.entity.client;

import symbiote.entity.AbstractEntity;
import symbiote.network.AbstractPacket;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;

public abstract class ClientEntity extends AbstractEntity { //An entity is an object that moves or has physics.
    public Animation animation = AnimationFactory.start().addFrame("back.png").finish();
    
    public ClientEntity(int id, double x, double y) {
        super(id, x, y);
    }    
    
    public abstract AbstractPacket getPacket();
    
}
