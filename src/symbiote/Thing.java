package symbiote;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import symbiote.entity.Entity;
import symbiote.resources.ImageHandler;
import symbiote.world.Solid;

public class Thing { //A Thing is any object that exists in the world that will be drawn and/or have logic.
    
    public double x = 0;
    public double y = 0;
    public double angle = 0;
    public BufferedImage image = null;
    public boolean dontUse = false;
    
    public boolean destroyed = false;
    
    public Thing (double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Thing(double x, double y, String imageName) {
        this.x = x;
        this.y = y;
        image = ImageHandler.getImage(imageName);
    }
    
    /**
     * The looping code of this Thing. Generally used for game logic.
     */
    public void tick() {}
    
    /**
     * Checks whether this thing intersects a shape.
     * @return Whether this thing intersects a shape.
     */
    public boolean intersects(Shape shape) {
        Area areaA = new Area(getCollisionBox());
        areaA.intersect(new Area(shape));
        return !areaA.isEmpty();
    }
    
    /**
     * Checks whether this thing intersects another Thing.
     * @return Whether this thing intersects another Thing.
     */
    public boolean intersects(Thing t) {
        return intersects(t.getCollisionBox());
    }
    
    /**
     * Gets the collision box of this Thing.
     * @return The collision box of this Thing.
     */
    public Shape getCollisionBox() {
        Shape shape = new Rectangle2D.Double(x, y, image.getWidth(), image.getHeight());
        if (!dontUse) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), x + image.getWidth() / 2, y + image.getHeight() / 2);
            shape = transform.createTransformedShape(shape);
        } else {
            shape = new Rectangle2D.Double(-1, -1, 0, 0);
        }
        return shape;
    }

    public double getCenterX() {
        return x + (image.getWidth() / 2);
    }
    
    public double getCenterY() {
        return y + (image.getHeight() / 2);
    }
    
    public double getWidth() {
        return image.getWidth();
    }
    
    public double getHeight() {
        return image.getHeight();
    }

    //TODO call these somewhere
    public void mouseEnter() {}
    public void mouseLeave() {}

    public void mouseClicked(int x, int y) {}
    public void mouseReleased(int x, int y) {}
    public void keyPressed(KeyEvent k) { }
    public void keyReleased(KeyEvent k) {}
    
    public void collide(Thing t) {}
    public void collide(Entity e) {}
    public void collide(Solid s) {}

    public void draw(Graphics2D g) {
        if (image != null && !dontUse) {
            ImageHandler.drawRotated(image, x, y, angle, g);
        }
    }
    
    /**
     * Adds this Thing to the removal queue and marks it as destroyed.
     */
    public void destroy() {
        Main.screen.removeThings.add(this);
        destroyed = true;
    }
    
    /**
     * Adds a Thing to the add queue and returns it.
     * @return The Thing you just added to the add queue.
     */
    public static Thing create(Thing t) {
        Main.screen.addThings.add(t);
        return t;
    }
}
