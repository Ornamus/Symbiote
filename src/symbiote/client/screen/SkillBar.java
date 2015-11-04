package symbiote.client.screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import symbiote.client.Client;
import symbiote.client.InputListener;
import symbiote.client.Skill;
import symbiote.entity.client.Interactable;
import symbiote.misc.Util;

public class SkillBar extends GUI implements Interactable {

    //TODO: Add "basic skills" that activate on left click
    
    static Skill[] skills;
    
    public SkillBar() {
        super(10, 10);
        InputListener.extras.add(this);
        skills = new Skill[]{Skill.NOTHING, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING};
    }
    
    @Override
    public void draw(Graphics2D g) {
        final int cooldownSize = 32;
        
        Color oldColor = g.getColor();
        for (int i = 0; i < skills.length; i++) {            
            int drawX = Util.round(x) + i * (cooldownSize + 4);
            int drawY = Util.round(y);
            Skill k = skills[i];
            g.drawImage(k.icon, drawX, drawY, null);

            if (k.isOnCooldown()) {
                g.setColor(new Color(0, 0, 0, 128));
                g.clipRect(drawX, drawY, cooldownSize, cooldownSize);
                g.fillArc(
                        drawX - cooldownSize / 2, 
                        drawY - cooldownSize / 2,
                        cooldownSize * 2, 
                        cooldownSize * 2, 
                        90,
                        (int) (k.getSecondsLeft() / k.getCooldownTime() * 360));
                
                g.setClip(null);
            }
            g.setColor(Color.blue);
            g.drawRect(drawX, drawY, cooldownSize, cooldownSize);
        }
        g.setColor(oldColor);
    }
         
    @Override 
    public void keyPressed(KeyEvent k) {
        int selected = -1;
        if (k.getKeyCode() == KeyEvent.VK_1) {
            selected = 0;
        } else if (k.getKeyCode() == KeyEvent.VK_2) {
            selected = 1;
        } else if (k.getKeyCode() == KeyEvent.VK_3) {
            selected = 2;
        } else if (k.getKeyCode() == KeyEvent.VK_4) {
            selected = 3;
        }
        if (selected > -1) {
            Skill s = skills[selected];
            if (s != null && !s.isOnCooldown()) {
                s.use(Client.focus);
            }
        }
    }

    public static void setSkills(Skill[] newSkills) {
        skills = newSkills;
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void mouseHeld(int x, int y, MouseEvent m) {}
    @Override public void mouseReleased(int x, int y, MouseEvent m) {}
    @Override public void keyReleased(KeyEvent k) {}
    
}
