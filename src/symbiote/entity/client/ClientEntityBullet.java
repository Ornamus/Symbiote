package symbiote.entity.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityBullet;
import symbiote.entity.EntityPlayer;
import symbiote.entity.LivingEntity;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.resources.ImageHandler;

public class ClientEntityBullet extends EntityBullet implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public ClientEntityBullet(int id, double x, double y, double angle, String owner) {
        super(id, x, y, angle, owner);
        
        animation = AnimationFactory.start().addFrame("bullet.png").loop(true).finish();
    }
    
    @Override
    public void collide(AbstractEntity e) {
        boolean living = false;
        boolean hit = true;
        if (e instanceof LivingEntity) living = true;
        if (e instanceof EntityPlayer) {
            if (((EntityPlayer) e).name.equals(owner)) {
                hit = false;
            }
        }
        if (hit) {
            if (living) {
                ((LivingEntity) e).health -= 10;
            }
            destroy();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        
        image = animation.getCurrentFrame();
    }

    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            ImageHandler.drawRotated(image, x, y, angle, g);
        }
    }
}
