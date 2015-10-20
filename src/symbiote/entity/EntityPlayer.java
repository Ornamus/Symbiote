package symbiote.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.Thing;
import symbiote.client.Board;
import symbiote.client.Client;
import symbiote.resources.AnimationFactory;
import symbiote.misc.Util;
import symbiote.world.Wall;

public class EntityPlayer extends LivingEntity {
    
    private final List<String> keysPressed = new ArrayList<>();
    public boolean playing = true;
    
    public EntityPlayer(int x, int y) {
        super(x, y);
        animation = AnimationFactory.start().addFrame("body.png").finish();
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
    public void mouseReleased(int x, int y) {
        if ((playing && !symbioteControlled) || (Client.symbiote && symbioteControlled)) {
            Point p = Util.getMouseInWorld(); //TODO check if this makes bullets work properly
            Client.communicator.sendMessage(new Object[]{"entity", "bullet", getCenterX(), getCenterY(), Util.angle(getCenterX(), getCenterY(), p.x, p.y), Client.name});
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if ((playing && !symbioteControlled) || (Client.symbiote && symbioteControlled)) {
            if (Main.client) {
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
                if (!name.equals("")) {
                    Client.communicator.sendMessage(getPacket());
                }
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (!playing || symbioteControlled) {
            BufferedImage frame = animation.getCurrentFrame(false);
            int textWidth = g.getFontMetrics().stringWidth(name);
            g.drawString(name, Util.round((x + (frame.getWidth() / 2) - (textWidth / 2))), Util.round((y - (frame.getHeight() / 2))));
        }
    }

    @Override
    public Object[] getPacket() {
        return new Object[]{"player", name, x, y, angle};
    }

    public static EntityPlayer get(String name) {
        for (Thing t : Main.screen.things) {
            if (t instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) t;
                if (player.name.equals(name)) {
                    return player;
                }
            }
        }
        return null;
    }
}
