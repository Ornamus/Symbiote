package symbiote.entity.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.client.Skill;
import symbiote.client.screen.SkillBar;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Util;
import symbiote.network.CPacketPosition;

public class ClientEntityThisPlayer extends ClientEntityPlayer implements Interactable {

    private final List<String> keysPressed = new ArrayList<>();
    public Point destination = null;
    
    /**
     * The distance the player can be from the destination point before pathing is stopped.
     */
    private static float DISTANCE_FROM_DESTINATION = 2;
    
    public ClientEntityThisPlayer(int id, String name, double x, double y) {
        super(id, name, x, y);
        destination = new Point((int) Math.round(x), (int) Math.round(y));
    }
    
    @Override
    public void keyPressed(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_W) {
            if (!keysPressed.contains("w"))
            keysPressed.add("w");
        }
        if (k.getKeyCode() == KeyEvent.VK_A) {
            if (!keysPressed.contains("a"))
            keysPressed.add("a");
        }
        if (k.getKeyCode() == KeyEvent.VK_S) {
            if (!keysPressed.contains("s"))
            keysPressed.add("s");
        }
        if (k.getKeyCode() == KeyEvent.VK_D) {
            if (!keysPressed.contains("d"))
            keysPressed.add("d");
        }
    }
    
    @Override
    public void keyReleased(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_W) {
            keysPressed.remove("w");
        }
        if (k.getKeyCode() == KeyEvent.VK_A) {
            keysPressed.remove("a");
        }
        if (k.getKeyCode() == KeyEvent.VK_S) {
            keysPressed.remove("s");
        }
        if (k.getKeyCode() == KeyEvent.VK_D) {
            keysPressed.remove("d");
        }
    }
    
    @Override public void mouseHeld(int x, int y, MouseEvent m) {
        if (m.getButton() == MouseEvent.BUTTON3) {       
            Point p = Util.getMouseInWorld();
            destination = new Point(p.x, p.y);            
        }
    }
    
    @Override
    public void mouseReleased(int x, int y, MouseEvent m) {
        if (((playing && !symbioteControlled) || (Client.symbiote && symbioteControlled)) && m.getButton() == MouseEvent.BUTTON1) {
            Skill k = SkillBar.getSelected();
            if (k != null) {
                k.use(this, Util.getMouseInWorld());
            }
        }
    }

    double lastX = 0;
    double lastY = 0;
    double lastAngle = 0;
    long lastSend = 0;

    @Override
    public void tick() {
        super.tick();
        
        if ((playing && !symbioteControlled) || (Client.symbiote && symbioteControlled)) {
            if (Main.client) {
                
                //TODO: Decide whether or not this style of movement (MOBA) should be used or not            
                //TODO: Am I crazy, or is going to the destination randomly inaccurate?
                double distanceToDestination = Math.sqrt(
                        Math.pow(getCollisionBoxCenterX() - destination.x, 2) + 
                        Math.pow(getCollisionBoxCenterY() - destination.y, 2));
                
                if (distanceToDestination > DISTANCE_FROM_DESTINATION) {
                    double moveAngle = Util.angle(getCollisionBoxCenterX(), getCollisionBoxCenterY(), destination.x, destination.y);

                    xVel = Math.min(speed, distanceToDestination) * Math.sin(Math.toRadians(moveAngle));
                    yVel = Math.min(speed, distanceToDestination) * -Math.cos(Math.toRadians(moveAngle));
                } else {
                    if (Math.abs(xVel) <= speed && Math.abs(yVel) <= speed) {
                        xVel = 0;
                        yVel = 0;
                    }
                }

                /*
                if (keysPressed.contains("w")) {
                    yVel = -speed;
                }
                if (keysPressed.contains("a")) {
                    xVel = -speed;
                }
                if (keysPressed.contains("s")) {
                    yVel = speed;
                }
                if (keysPressed.contains("d")) {
                    xVel = speed;
                }

                //Disabled due to the new sprite not needing rotation
                
                Point p = Util.getMouseOnScreen();
                if (p.x < Client.self.getWidth() && p.x > 0 && p.y < Client.self.getHeight() && p.y > 0) {
                    double oldAngle = angle;
                    angle = Util.angle(getCenterX(), getCenterY(), p.x + Board.offsetX, p.y + Board.offsetY);
                    for (AbstractEntity t : Client.screen.thingMap.values()) {
                        if (t instanceof Wall && intersects(t)) {
                            angle = oldAngle;
                        }
                    }
                }*/
                
                Point p = Util.getMouseOnScreen();
                angle = Util.angle(getCenterX(), getCenterY(), p.x + Board.offsetX, p.y + Board.offsetY);
                
                if (System.currentTimeMillis() - lastSend > Client.PACKET_SEND_RATE && (x != lastX || y != lastY || angle != lastAngle)) {
                    lastSend = System.currentTimeMillis();
                    Client.communicator.sendMessage(new CPacketPosition(this.id, x, y, angle));
                    lastX = x;
                    lastY = y;
                    lastAngle = angle;
                }
            }
        }
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void collide(AbstractEntity e) {}
}
