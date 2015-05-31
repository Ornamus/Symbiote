package symbiote.entity;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.Thing;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.misc.Util;
import symbiote.resources.AnimationFactory;
import symbiote.world.Wall;

public class EntitySymbiote extends LivingEntity {
    
    private final List<String> keysPressed = new ArrayList<>();
    public LivingEntity controlledEntity;
    public boolean playing = true;
    
    public EntitySymbiote(double x, double y) {
        super(x, y);
        animation = AnimationFactory.start().addFrame("symbiote.png").finish();
        controlledEntity = this;
        name = "Symbiote";
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
                    for (Thing t : Main.screen.things) {
                        if (t instanceof LivingEntity && t.getCollisionBox().contains(p)) { //TODO add a distance limit
                            LivingEntity e = (LivingEntity) t;
                            e.symbioteControlled = true;
                            controlledEntity = e;
                            Client.communicator.sendMessage(new Object[]{"symbioteControl", e.name});
                            break;
                        }
                    }
                }
            }

            if (k.getKeyCode() == KeyEvent.VK_R) {
                if (controlledEntity != this) {
                    controlledEntity.symbioteControlled = false;
                    controlledEntity = this;
                    Client.communicator.sendMessage(new Object[]{"symbioteControl", name});
                }
            }
        }
    }

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
                for (Thing t : Main.screen.things) {
                    if (t instanceof Wall && intersects(t)) {
                        angle = oldAngle;
                    }
                }
            }
        }
        if (playing) Client.communicator.sendMessage(getPacket());
    }
    
    @Override
    public Object[] getPacket() {
        return new Object[]{"symbiote", x, y, angle, (controlledEntity != this)};
    }
    
    public static EntitySymbiote get() {
        for (Thing t : Main.screen.things) {
            if (t instanceof EntitySymbiote) {
                return (EntitySymbiote) t;
            }
        }
        return null;
    }
}
