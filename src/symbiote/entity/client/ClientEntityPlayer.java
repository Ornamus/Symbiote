package symbiote.entity.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import symbiote.client.Client;
import symbiote.entity.EntityPlayer;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.misc.Util;

public class ClientEntityPlayer extends EntityPlayer implements Drawable {
    BufferedImage image = null;
    Animation animation;
    
    public boolean playing = false;
    
    public ClientEntityPlayer(int id, String name, double x, double y) {
        super(id, name, x, y);
        animation = AnimationFactory.start().addFrame("body.png").loop(true).finish();
    }
    
    double lastX = 0;
    double lastY = 0;
    long lastPacketUpdate;
    
    @Override
    public void setPositionAndAngle(double x, double y, double angle) {
        // inb4 update
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastPacketUpdate = System.currentTimeMillis();
        
        super.setPositionAndAngle(x, y, angle);
    }
    
    @Override
    public void tick() {
        super.tick();
        this.image = animation.getCurrentFrame(true);
    }
    
    @Override
    public Shape getCollisionBox() {
        // This hitbox is just the shadow at the person's feet (1/3rd the person's height), to give a correct feel of depth
        return new Rectangle2D.Double(x, y + height * (2/3d), width, height / 3d);
    }
    
    @Override
    public void draw(Graphics2D g) {
        double a = ((double) System.currentTimeMillis() - lastPacketUpdate) / (double) Client.PACKET_SEND_RATE;
        double lerpX = Util.lerp(this.lastX, this.x, a),
                lerpY = Util.lerp(this.lastY, this.y, a);
        
        if (image != null) {
            // first maps negative angles to 0-360, 'left' is >=180 in clockwise degrees. 
            boolean facingLeft = (angle + 360) % 360 >= 180;
            
            // if facing right, flip the image
            g.drawImage(image, 
                    Util.round(lerpX) + (int) (facingLeft ? 0 : image.getWidth() * size), 
                    Util.round(lerpY), 
                    Util.round(image.getWidth() * size) * (facingLeft ? 1 : -1), 
                    Util.round(image.getHeight() * size), 
                    null);
        }
        
        if (maxHealth > health) {
            drawHealth(lerpX, lerpY - 42, g);
        }
        
        BufferedImage frame = animation.getCurrentFrame(false);
        int textWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, Util.round((lerpX + (frame.getWidth() / 2) - (textWidth / 2))), Util.round((lerpY - (frame.getHeight() / 2))));
        
        if (Client.DEBUG) {
            Rectangle collisionBox = this.getCollisionBox().getBounds();
            g.setColor(Color.RED);
            g.drawRect(collisionBox.x, collisionBox.y, collisionBox.width, collisionBox.height);
            
            g.setColor(Color.BLUE);
            g.drawLine(
                    (int) (collisionBox.getCenterX()),
                    (int) (collisionBox.getCenterY()), 
                    (int) (collisionBox.getCenterX() + 30 * Math.sin(Math.toRadians(angle))), 
                    (int) (collisionBox.getCenterY() + -30 * Math.cos(Math.toRadians(angle))));
        }
    }
    
    /**
     * Draws this LivingEntity's health bar at dX and dY.
     */
    public void drawHealth(double dX, double dY, Graphics2D g) {
        int healthBarWidth = 32;
        
        int iX = Util.round(dX);
        int iY = Util.round(dY);
        g.setColor(new Color(0, 0, 0));
        g.drawRect(iX - 1, iY - 12, healthBarWidth + 2, 8);
        g.setColor(Color.red);
        g.drawRect(iX, iY - 11, healthBarWidth, 6);
        g.fillRect(iX, iY - 11, healthBarWidth, 6);
        g.setColor(new Color(0, 190, 0));
        double greenBarWidth = ((health) / maxHealth) * healthBarWidth;
        g.drawRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.fillRect(iX, iY - 11, (int) greenBarWidth, 6);
        g.setColor(new Color(0, 0, 0));
    }
}
