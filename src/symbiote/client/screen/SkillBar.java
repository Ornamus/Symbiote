package symbiote.client.screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import symbiote.client.Client;
import symbiote.client.InputListener;
import symbiote.client.Skill;
import symbiote.entity.client.Interactable;
import symbiote.misc.Util;

public class SkillBar extends GUI implements Interactable {

    public static int selected = 0;
    static Skill[] skills;
    
    public SkillBar() {
        super(10, 10);
        InputListener.extras.add(this);
        skills = new Skill[]{Skill.NOTHING, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING};
    }
    
    @Override
    public void draw(Graphics2D g) {
        for (int i=0; i<4; i++) {            
            int drawX = Util.round(x) + (i * 36);
            int drawY = Util.round(y);
            Skill k = skills[i];
            g.drawImage(k.icon, drawX, drawY, null);

            Color oldColor = g.getColor();
            if (k.onCooldown) {
                g.setColor(new Color(0, 0, 0, 128));
                g.fillRect(drawX, drawY, 32, 32); //TODO: be fancy and have the gray overlay get shorter as the cooldown finishes
                g.setColor(Color.white);
                g.drawString((k.cooldownTime - k.currentCooldown) + "", drawX + 5, drawY + 15);
            }
            if (i == selected) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
            g.drawRect(drawX, drawY, 32, 32);           
            
            g.setColor(oldColor);
        }
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
        if (s != null && s.useOnSelect) {
            s.use(Client.focus, new Point(Util.round(Client.focus.x), Util.round(Client.focus.y)));
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
        if (index < 4 && index > -2) {
            if (index == -1 || !skills[index].onCooldown) {
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
