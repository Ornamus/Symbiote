package symbiote.entity.client;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Util;
import symbiote.network.CPacketPosition;
import symbiote.resources.ImageUtil;

public class ClientEntityThisPlayer extends ClientEntityPlayer implements Interactable {

    private final List<String> keysPressed = new ArrayList<>();
    public Point destination = null;
    public BufferedImage target = ImageUtil.getImage("target.png");
    
    /**
     * The distance the player can be from the destination point before pathing is stopped.
     */
    private static float DISTANCE_FROM_DESTINATION = 2;
    
    public ClientEntityThisPlayer(int id, String name, double x, double y) {
        super(id, name, x, y);
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
        if (!symbioteControlled && m.getButton() == MouseEvent.BUTTON1) {
            /*Skill k = SkillBar.getSelected();
            if (k != null) {
                k.use(this, Util.getMouseInWorld());
            }*/
        }
    }

    double lastSentX = 0;
    double lastSentY = 0;
    double lastSentAngle = 0;
    long lastSend = 0;

    boolean wasControlled = false;
    
    @Override
    public void tick() {
        super.tick();
        
        if (!symbioteControlled) {
            if (wasControlled) {
                wasControlled = false;
                destination = null;
            }
            //TODO: Decide whether to use this style of movement (MOBA) or Hammerwatch style   
            if (destination != null) {
                double distanceToDestination = Math.sqrt(
                        Math.pow(getCollisionBoxCenterX() - destination.x, 2)
                        + Math.pow(getCollisionBoxCenterY() - destination.y, 2));

                if (distanceToDestination > DISTANCE_FROM_DESTINATION) {
                    double moveAngle = Util.angle(getCollisionBoxCenterX(), getCollisionBoxCenterY(), destination.x, destination.y);

                    xVel = Math.min(speed, distanceToDestination) * Math.sin(Math.toRadians(moveAngle));
                    yVel = Math.min(speed, distanceToDestination) * -Math.cos(Math.toRadians(moveAngle));
                } else {
                    if (Math.abs(xVel) <= speed && Math.abs(yVel) <= speed) {
                        xVel = 0;
                        yVel = 0;
                    }
                    destination = null;
                }
            }

            Point p = Util.getMouseOnScreen();
            angle = Util.angle(getCenterX(), getCenterY(), p.x + Board.offsetX, p.y + Board.offsetY);

            if (System.currentTimeMillis() - lastSend > Client.PACKET_SEND_RATE && (x != lastSentX || y != lastSentY || angle != lastSentAngle)) {
                lastSend = System.currentTimeMillis();
                Client.communicator.sendMessage(new CPacketPosition(this.id, x, y, angle));
                lastSentX = x;
                lastSentY = y;
                lastSentAngle = angle;
            }
        } else  {
            wasControlled = true;
        }
    }
    
    @Override
    public void finalDraw(Graphics2D g) {
        super.finalDraw(g);
        
        if (destination != null) g.drawImage(target, destination.x - 5, destination.y - 5, null);
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void collide(AbstractEntity e) {}
}