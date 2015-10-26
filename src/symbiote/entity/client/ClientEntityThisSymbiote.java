package symbiote.entity.client;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.client.screen.SkillBar;
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
    }
    
    @Override
    public void mouseReleased(int x, int y, MouseEvent m) {
        //TODO: Allow symbiote to use skills if it's not controlling itself?
        if (playing && controlledEntity == this && m.getButton() == MouseEvent.BUTTON1) {
            SkillBar.getSelected().use(this, Util.getMouseInWorld());
        }
    }

    double lastX = 0;
    double lastY = 0;
    double lastAngle = 0;
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
            
            if (System.currentTimeMillis() - lastSend > 200 && (x != lastX || y != lastY || angle != lastAngle)) {
                lastSend = System.currentTimeMillis();
                Client.communicator.sendMessage(new CPacketPosition(this.id, x, y, angle));
                lastX = x;
                lastY = y;
                lastAngle = angle;
            }
        }
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void mouseHeld(int x, int y, MouseEvent m) {}
    @Override public void collide(AbstractEntity t) {}
}
