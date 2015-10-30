package symbiote.entity.client;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import symbiote.client.Client;
import symbiote.client.Skill;
import symbiote.client.screen.SkillBar;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Util;
import symbiote.network.AbstractPacket;
import symbiote.network.CPacketPosition;
import symbiote.network.SPacketSymbiote;

public class ClientEntityThisSymbiote extends ClientEntitySymbiote implements Interactable {
    /**
     * how far away the symbiote can control people
     */
    private static final int CONTROLDISTANCE = 200;
    
    private final List<String> keysPressed = new ArrayList<>();
    
    public ClientEntityThisSymbiote(int id, String name, double x, double y) {
        super(id, name, x, y);
    }
    
    @Override
    public AbstractPacket getPacket() {
        return new SPacketSymbiote(id, name, x, y, angle, false, true);
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
        if (m.getButton() == MouseEvent.BUTTON1) {
            Skill k = SkillBar.getSelected();
            if (k != null) {
                k.use(this, Util.getMouseInWorld());
            }
        }
    }

    double lastX = 0;
    double lastY = 0;
    double lastAngle = 0;
    long lastSend;

    @Override
    public void tick() {
        super.tick();
        
        if (keysPressed.contains("w")) {
            controlled.yVel = -speed;
        }
        if (keysPressed.contains("a")) {
            controlled.xVel = -speed;
        }
        if (keysPressed.contains("s")) {
            controlled.yVel = speed;
        }
        if (keysPressed.contains("d")) {
            controlled.xVel = speed;
        }

        if (System.currentTimeMillis() - lastSend > 200 && (controlled.x != lastX || controlled.y != lastY || controlled.angle != lastAngle)) {
            lastSend = System.currentTimeMillis();
            Client.communicator.sendMessage(new CPacketPosition(controlled.id, controlled.x, controlled.y, controlled.angle));
            lastX = controlled.x;
            lastY = controlled.y;
            lastAngle = controlled.angle;
        }
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void mouseHeld(int x, int y, MouseEvent m) {}
    @Override public void collide(AbstractEntity t) {}
}
