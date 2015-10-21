package symbiote.entity.client;

import java.awt.Color;
import java.awt.Graphics2D;

import symbiote.entity.LivingEntity;
import symbiote.misc.Util;

public abstract class ClientLivingEntity extends LivingEntity implements Drawable {
    
    public ClientLivingEntity(int id, double x, double y) {
        super(id, x, y);
    }
    
    @Override
    public void draw(Graphics2D g) {
        
    }
    
    
}
