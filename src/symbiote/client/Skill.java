package symbiote.client;

import java.awt.Point;
import java.awt.image.BufferedImage;
import symbiote.client.screen.SkillBar;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityUtil;
import symbiote.entity.LivingEntity;
import symbiote.entity.client.ClientEntityThisSymbiote;
import symbiote.misc.Util;
import symbiote.network.CPacketShoot;
import symbiote.network.CPacketSymbioteControl;
import symbiote.resources.ImageUtil;

public abstract class Skill {
    
    //TODO: Effects that make it obvious what skill is selected
    
    public static Skill SHOOT_BULLET = new Skill("Shoot", 0.4, "skill_gun"){
        @Override
        public void code(AbstractEntity e) {
            Point m = Util.getMouseInWorld();
            Client.communicator.sendMessage(new CPacketShoot(e.getCenterX(), e.getCenterY(), Util.angle(e.getCenterX(), e.getCenterY(), m.x, m.y), e.id));
        }
    };
    
    public static Skill SYMBIOTE_POSSESS = new Skill("Possess", 5, "skill_possess") {
        
        BufferedImage possess = ImageUtil.getImage("skill_possess.png");
        BufferedImage stop_possess = ImageUtil.getImage("skill_possess_stop.png");
        
        @Override
        public void code(AbstractEntity user) {
            if (user instanceof ClientEntityThisSymbiote) {
                ClientEntityThisSymbiote sim = (ClientEntityThisSymbiote) user;
                if (sim.controlled == sim) {
                    Point p = Util.getMouseInWorld();
                    for (AbstractEntity t : EntityUtil.getEntities()) {
                        // TODO: complete controllable
                        if (t instanceof LivingEntity && t.getBounds().contains(p)) {
                        // TODO: uncomment magnitude squared
                            //if ((this.x - t.x)*(this.x - t.x) + (this.y - t.y)*(this.y - t.y) < CONTROLDISTANCE*CONTROLDISTANCE) {
                            LivingEntity e = (LivingEntity) t;
                            e.symbioteControlled = true;
                            sim.controlled = e;
                            Client.focus = e;
                            icon = stop_possess;                           
                            Client.communicator.sendMessage(new CPacketSymbioteControl(e.name));
                            break;
                            //}
                        }
                    }
                } else {
                    sim.controlled.symbioteControlled = false;
                    sim.controlled = sim;
                    Client.focus = sim;
                    icon = possess;
                    Client.communicator.sendMessage(new CPacketSymbioteControl(sim.name));
                }
            } else {
                System.out.println("[ERROR] A non-symbiote entity attempted to use the possess skill!");
            }
        }
    };

    public static Skill NOTHING = new Skill("Do Nothing", 0, "skill_nothing"){};
    
    
    public String name;
    public BufferedImage icon;
    
    /**
     * How many seconds the skill takes to cool down
     */
    private double cooldownTime = 0;
    
    /**
     * System.nanoTime of the last use
     */
    private long lastUseTime;
    
    public Skill(String name, double cooldown, String icon) {
        this.name = name;
        this.icon = ImageUtil.getImage(icon + ".png");
        this.lastUseTime = System.nanoTime() - (int) (cooldown * 10e8);
        setCooldownTime(cooldown);
    }
    
    /**
     * @return Whether this skill is in a state of cooldown, where it cannot be used.
     */
    public boolean isOnCooldown() {
        return (System.nanoTime() - lastUseTime) / 10e8 < getCooldownTime();
    }
    
    /**
     * @return The time, in seconds, that this skill takes to cool down after use.
     */
    public double getCooldownTime() {
        return cooldownTime;
    }
    
    /**
     * Sets how long the skill takes to cool down after use.
     * @param cooldownTime The cooldown time, in seconds
     */
    public void setCooldownTime(double cooldownTime) {
        this.cooldownTime = cooldownTime;
    }
    
    /**
     * @return The time, in seconds, that this skill has left until it is off cooldown, or 0 if cooldown is not in effect.
     */
    public double getSecondsLeft() {
        if (isOnCooldown()) {
            return getCooldownTime() - ((System.nanoTime() - lastUseTime) / 10e8);
        } else {
            return 0;
        }
    }
    
    public void use(AbstractEntity user) {
        if (!this.isOnCooldown()) {
            this.lastUseTime = System.nanoTime();
            
            code(user);
        }
    }
    
    public void code(AbstractEntity user) {
        System.out.println("Skill '" + name + "' doesn't have any code!");
    }
}
