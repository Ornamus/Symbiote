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

    //TODO: Change skills to not be selectable, and instead have them be "press and point" abilities?
    //If we do that, we change left click to be a "basic attack", which is basically just shooting a bullet
    
    public static int selected = 0;
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
            if (i == selected) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
            g.drawRect(drawX, drawY, cooldownSize, cooldownSize);
        }
        g.setColor(oldColor);
    }
         
    @Override 
    public void keyPressed(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_1) {
            select(0);
        } else if (k.getKeyCode() == KeyEvent.VK_2) {
            select(1);
        } else if (k.getKeyCode() == KeyEvent.VK_3) {
            select(2);
        } else if (k.getKeyCode() == KeyEvent.VK_4) {
            select(3);
        }
        Skill s = getSelected();
        if (s != null && !s.isOnCooldown() && s.useOnSelect) {
            s.use(Client.focus, Util.getMouseInWorld());
            selected = -1;
        }
    }
           
    public static Skill getSelected() {
        if (selected == -1) {
            return null;
        }
        return skills[selected];
    }
    
    public static void select(int index) {
        if (index < skills.length && index > -2) {
            Skill k = skills[index];
            if (!(k.useOnSelect && k.isOnCooldown())) {
                selected = index;
            }
        } else {
            System.out.println("[ERROR] Invalid index '" + index + "' for skillbar!");
        }
    }
    
    public static void setSkills(Skill[] newSkills) {
        skills = newSkills;
        selected = -1;
    }
    
    public static void setSkills(Skill[] newSkills, int newSelected) {
        skills = newSkills;
        selected = newSelected;
    }
    
    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void mouseHeld(int x, int y, MouseEvent m) {}
    @Override public void mouseReleased(int x, int y, MouseEvent m) {}
    @Override public void keyReleased(KeyEvent k) {}
    
}
