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

    static int selected = 0;
    static Skill[] skills;
    
    public SkillBar() {
        super(10, 10);
        InputListener.extras.add(this);
        if (Client.symbiote) {
            skills = new Skill[]{Skill.SHOOT_BULLET, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING};
        } else {
            skills = new Skill[]{Skill.SYMBIOTE_POSSESS, Skill.NOTHING, Skill.NOTHING, Skill.NOTHING};
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        for (int i=0; i<4; i++) { 
            Color oldColor = g.getColor();
            if (i == selected) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }
            //TODO: Draw skill icons instead of colorful boxes
            int drawX = Util.round(x) + (i * 36);
            int drawY = Util.round(y);
            g.fillRect(drawX, drawY, 32, 32);
            g.setColor(oldColor);
        }
    }
         
    @Override 
    public void keyPressed(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_1) {
            selected = 0;
        } else if (k.getKeyCode() == KeyEvent.VK_2) {
            selected = 1;
        } else if (k.getKeyCode() == KeyEvent.VK_3) {
            selected = 2;
        } else if (k.getKeyCode() == KeyEvent.VK_4) {
            selected = 3;
        }
        Skill s = getSelected();
        if (s.useOnSelect) {
            s.use(Client.focus, new Point(Util.round(Client.focus.x), Util.round(Client.focus.y)));
            selected = -1;
        }
    }
           
    public static Skill getSelected() {
        return skills[selected];
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
