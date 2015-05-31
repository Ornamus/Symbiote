package symbiote.world;

import java.awt.geom.Rectangle2D;
import symbiote.Thing;

public class Wall extends Thing {
    
    public Wall(double x, double y, String imageName) {
        super(x, y, imageName);
    }    
    
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, image.getWidth(), image.getHeight());
    }
}
