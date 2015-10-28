package symbiote.entity.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import symbiote.entity.EntitySymbiote;
import symbiote.misc.Util;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;

public class ClientEntitySymbiote extends EntitySymbiote implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public ClientEntitySymbiote(int id, String name, double x, double y) {
        super(id, name, x, y);
        animation = AnimationFactory.start().addFrame("symbiote.png").loop(true).finish();
        width = 16;
        height = 14;
    }

    @Override
    public void tick() {
        super.tick();
        
        image = animation.getCurrentFrame(true);
    }
    
    @Override
    public void draw(Graphics2D g) {
        if (image != null) {
            g.drawImage(image, Util.round(x), Util.round(y), Util.round(width), Util.round(height), null);
        }
        
        if (maxHealth > health) {
            drawHealth(x, y - 42, g);
        }
    }
}
