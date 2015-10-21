package symbiote.entity.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import symbiote.Main;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.LivingEntity;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.network.CPacketPosition;
import symbiote.network.CPacketSymbioteControl;
import symbiote.network.SPacketSymbiote;
import symbiote.world.Wall;

public class ClientEntityThisSymbiote extends ClientEntitySymbiote implements Interactable {
    /**
     * how far away the symbiote can control people
     */
    private static final int CONTROLDISTANCE = 200;
    
    private final List<String> keysPressed = new ArrayList<>();
    
    public ClientEntityThisSymbiote(int id, double x, double y) {
        super(id, x, y);
    }
    
    @Override
    public AbstractPacket getPacket() {
        return new SPacketSymbiote(id, x, y, angle, false, true);
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
     
        if (Main.client && playing) {
            if (k.getKeyCode() == KeyEvent.VK_E) {
                if (controlledEntity == this) {
                    Point p = Util.getMouseInWorld();
                    for (AbstractEntity t : Client.screen.thingMap.values()) {
                        // TODO: complete controllable
                        if (t instanceof LivingEntity && t.getCollisionBox().contains(p)) {
                            // TODO: uncomment magnitude squared
                            //if ((this.x - t.x)*(this.x - t.x) + (this.y - t.y)*(this.y - t.y) < CONTROLDISTANCE*CONTROLDISTANCE) {
                                LivingEntity e = (LivingEntity) t;
                                e.symbioteControlled = true;
                                controlledEntity = e;
                                Client.communicator.sendMessage(new CPacketSymbioteControl(e.name));
                                break;
                            //}
                        }
                    }
                }
            }

            if (k.getKeyCode() == KeyEvent.VK_R) {
                if (controlledEntity != this) {
                    controlledEntity.symbioteControlled = false;
                    controlledEntity = this;
                    Client.communicator.sendMessage(new CPacketSymbioteControl(name));
                }
            }
        }
    }

    double lastX = 0;
    double lastY = 0;
    long lastSend;

    @Override
    public void tick() {
        super.tick();
        
        if (Main.client && controlledEntity == this && playing) {
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
            
            Point p = Util.getMouseOnScreen();
            if (p.x < Client.self.getWidth() && p.x > 0 && p.y < Client.self.getHeight() && p.y > 0) {
                double oldAngle = angle;
                angle = Util.angle(getCenterX(), getCenterY(), p.x + Board.offsetX, p.y + Board.offsetY);
                for (AbstractEntity t : Client.screen.thingMap.values()) {
                    if (t instanceof Wall && intersects(t)) {
                        angle = oldAngle;
                    }
                }
            }
            
            if (System.currentTimeMillis() - lastSend > 200 && (x != lastX || y != lastY)) {
                lastSend = System.currentTimeMillis();
                Client.communicator.sendMessage(new CPacketPosition(this.id, x, y, angle));
                lastX = x;
                lastY = y;
            }
        }
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y) {}
    @Override public void mouseReleased(int x, int y) {}
    @Override public void collide(AbstractEntity t) {}
}
