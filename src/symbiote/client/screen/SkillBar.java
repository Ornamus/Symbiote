package symbiote.client.screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import symbiote.client.Client;
import symbiote.client.InputListener;
import symbiote.client.Skill;
import symbiote.entity.client.Interactable;
import symbiote.misc.Util;

public class SkillBar extends GUI implements Interactable {

    //TODO: Add "basic skills" that activate on left click
    
    static Map<Integer, Skill> skillKeys = new HashMap<>();
    
    static Skill[] skills;
    
    public SkillBar() {
        super(10, 10);
        InputListener.extras.add(this);
        setSkills(new Object[][]{ {KeyEvent.VK_1, Skill.NOTHING},  {KeyEvent.VK_2, Skill.NOTHING}, {KeyEvent.VK_3, Skill.NOTHING} , {KeyEvent.VK_4, Skill.NOTHING}});
    }
    
    @Override
    public void draw(Graphics2D g) {
        final int cooldownSize = 32;
        
        Color oldColor = g.getColor();
        List<Skill> visibleSkills = getVisibleSkills();
        for (int i = 0; i < visibleSkills.size(); i++) {            
            int drawX = Util.round(x) + i * (cooldownSize + 4);
            int drawY = Util.round(y);
            Skill k = visibleSkills.get(i);
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
    
    public List<Skill> getVisibleSkills() {
        List<Skill> vSkills = new ArrayList<>();
        for (Skill k : skills) {
            if (k.show) vSkills.add(k);
        }
        return vSkills;
    }
         
    @Override 
    public void keyPressed(KeyEvent k) {
        Skill s = skillKeys.get(k.getKeyCode());
        if (s != null && !s.isOnCooldown()) {
             s.use(Client.focus);
        }
    }
    
    public static void setSkills(Object[][] array) {
        for (Object[] o : array) {
            skillKeys.put((Integer)o[0], (Skill)o[1]);
        }
        refreshSkills();
    }
    
    public static void setSkill(Integer key, Skill s) {
        skillKeys.put(key, s);
        refreshSkills();
    }
    
    private static void refreshSkills() {
        Collection<Skill> values = skillKeys.values();
        skills = values.toArray(new Skill[values.size()]);
    }

    @Override public void mouseEnter() {}
    @Override public void mouseLeave() {}
    @Override public void mouseClicked(int x, int y, MouseEvent m) {}
    @Override public void mouseHeld(int x, int y, MouseEvent m) {}
    @Override public void mouseReleased(int x, int y, MouseEvent m) {}
    @Override public void keyReleased(KeyEvent k) {}    
}
