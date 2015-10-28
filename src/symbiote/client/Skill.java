package symbiote.client;

import java.awt.Point;
import java.awt.image.BufferedImage;
import symbiote.client.screen.SkillBar;
import symbiote.entity.AbstractEntity;
import symbiote.entity.LivingEntity;
import symbiote.entity.client.ClientEntityThisSymbiote;
import symbiote.misc.Util;
import symbiote.network.CPacketShoot;
import symbiote.network.CPacketSymbioteControl;
import symbiote.resources.ImageHandler;

public abstract class Skill {
    
    //TODO: Effects that make it obvious what skill is selected
    
    public static Skill SHOOT_BULLET = new Skill("Shoot", 0.4, "skill_gun"){
        @Override
        public void code(AbstractEntity e, Point p) {
            Point m = Util.getMouseInWorld();
            Client.communicator.sendMessage(new CPacketShoot(e.getCenterX(), e.getCenterY(), Util.angle(e.getCenterX(), e.getCenterY(), m.x, m.y), e.id));
        }
    };
    
    //TODO: Symbiote is not able to control possessed player, and possessed player can still control their character. Fix this
    public static Skill SYMBIOTE_POSSESS = new Skill("Possess", 5, "skill_possess") {
        
        BufferedImage possess = ImageHandler.getImage("skill_possess.png");
        BufferedImage stop_possess = ImageHandler.getImage("skill_possess_stop.png");
        
        @Override
        public void code(AbstractEntity user, Point p) {
            if (user instanceof ClientEntityThisSymbiote) {
                ClientEntityThisSymbiote sim = (ClientEntityThisSymbiote) user;
                if (sim.controlledEntity == sim) {
                    for (AbstractEntity t : Client.screen.thingMap.values()) {
                        // TODO: complete controllable
                        if (t instanceof LivingEntity && t.getBounds().contains(p)) {
                        // TODO: uncomment magnitude squared
                            //if ((this.x - t.x)*(this.x - t.x) + (this.y - t.y)*(this.y - t.y) < CONTROLDISTANCE*CONTROLDISTANCE) {
                            LivingEntity e = (LivingEntity) t;
                            e.symbioteControlled = true;
                            sim.controlledEntity = e;
                            useOnSelect = true;
                            SkillBar.selected = -1;
                            icon = stop_possess;
                            Client.communicator.sendMessage(new CPacketSymbioteControl(e.name));
                            break;
                            //}
                        }
                    }
                } else {
                    sim.controlledEntity.symbioteControlled = false;
                    sim.controlledEntity = sim;
                    useOnSelect = false;
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
    public boolean useOnSelect = false;
    
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
        this.icon = ImageHandler.getImage(icon + ".png");
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
    
    public void use(AbstractEntity user, Point p) {
        if (!this.isOnCooldown()) {
            this.lastUseTime = System.nanoTime();
            
            code(user, p);
        }
    }
    
    public void code(AbstractEntity user, Point p) {
        System.out.println("Skill '" + name + "' doesn't have any code!");
    }
}
