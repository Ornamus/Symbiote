package symbiote.entity.client;

import java.awt.Graphics2D;
import symbiote.entity.LivingEntity;

public abstract class ClientLivingEntity extends LivingEntity implements Drawable {
    
    public ClientLivingEntity(int id, double x, double y) {
        super(id, x, y);
    }
    
    @Override
    public void draw(Graphics2D g) {       
    }      
}
