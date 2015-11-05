package symbiote.entity.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import symbiote.entity.EntityBullet;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.resources.ImageUtil;

public class ClientEntityBullet extends EntityBullet implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public ClientEntityBullet(int id, double x, double y, double angle, int owner) {
        super(id, x, y, angle, owner);
        
        animation = AnimationFactory.start().addFrame("bullet.png").loop(true).finish();
    }
    
    @Override
    public void tick() {
        super.tick();
        
        image = animation.getCurrentFrame();
    }

    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            ImageUtil.drawRotated(image, x, y, angle, g);
        }
    }
}
