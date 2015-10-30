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

public class ClientEntityPlayer extends EntityPlayer implements ComplexDrawable {
    BufferedImage image = null;
    Animation animation;
    
    public ClientEntityPlayer(int id, String name, double x, double y) {
        super(id, name, x, y);       
        //animation = AnimationFactory.start().addFrame("player_pyro.png").loop(true).finish();
        
        Util.randomInt(1, 4); //Not doing this seems to make the client start off as the same character EVERY TIME. Probably something with random seeds, I dunno
        
        //Fun code for using all the different player sprites. TODO: Server decides who gets which sprites
        switch (Util.randomInt(1, 4)) {
            case 1:
                animation = AnimationFactory.start().addFrame("player_pyro.png").loop(true).finish();          
                break;
            case 2:
                animation = AnimationFactory.start().addFrame("player_mechanic.png").loop(true).finish();
                width = 24;
                height = 48;
                break;
            case 3:
                animation = AnimationFactory.start().addFrame("player_tracker.png").loop(true).finish();
                width = 28;
                height = 48;
                break;
            case 4:
                animation = AnimationFactory.start().addFrame("player_medic.png").loop(true).finish();
                width = 28;
                height = 46;
                break;
                           
        } 
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
    
    double lastLerpX = 0, lastLerpY = 0;
    
    @Override
    public void draw(Graphics2D g) {
        double a = ((double) System.currentTimeMillis() - lastPacketUpdate) / (double) Client.PACKET_SEND_RATE;
        double lerpX = Util.lerp(this.lastX, this.x, a),
                lerpY = Util.lerp(this.lastY, this.y, a);
        
        lastLerpX = lerpX;
        lastLerpY = lerpY;
        
        if (image != null) {
            // first maps negative angles to 0-360, 'left' is >=180 in clockwise degrees. 
            boolean facingLeft = (angle + 360) % 360 >= 180;
            
            // if facing right, flip the image
            g.drawImage(image, 
                    Util.round(lerpX) + (int) (facingLeft ? 0 : width), 
                    Util.round(lerpY), 
                    Util.round(width) * (facingLeft ? 1 : -1), 
                    Util.round(height), 
                    null);
        }               
        
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
    
    @Override
    public void finalDraw(Graphics2D g) {
        if (maxHealth > getHealth()) {
            drawHealth(lastLerpX, lastLerpY - 42, g);
        }
        
        int textWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, Util.round((lastLerpX + (width / 2) - (textWidth / 2))), Util.round((lastLerpY - (height / 4))));
    }
}
