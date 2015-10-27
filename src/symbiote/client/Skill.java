package symbiote.client;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
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
    
    //TODO: Figure out why bullets are spawning out of different positions around the feet instead of the chest area
    //TODO: Bullets aren't spawning at all now? What?
    public static Skill SHOOT_BULLET = new Skill("Shoot", 2, "skill_gun"){
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
                        if (t instanceof LivingEntity && t.getCollisionBox().contains(p)) {
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

    public static Skill NOTHING = new Skill("Do Nothing", 0.1, "skill_nothing"){};
    
    
    public String name;
    public BufferedImage icon;
    public boolean useOnSelect = false;
    
    public double cooldownTime = 0;
    public double currentCooldown = 0;
    public boolean onCooldown = false;
    public Timer cooldownTimer;
    
    public Skill(String name, double cooldown, String icon) {
        this.name = name;
        this.cooldownTime = cooldown;
        this.icon = ImageHandler.getImage(icon + ".png");
    }    
    
    public void cooldownTimer(int time) {
        cooldownTimer = new Timer(time, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                currentCooldown += time / 1000;
                if (currentCooldown >= cooldownTime) {
                    cooldownTimer.stop();
                    onCooldown = false;
                    currentCooldown = 0;
                } else {
                    double timeLeft = cooldownTime - currentCooldown;
                    cooldownTimer.stop();
                    cooldownTimer(timeLeft >= 1 ? 1000 : Util.round(timeLeft * 1000));
                }
            }
        });
        cooldownTimer.start();
    }
    
    public void use(AbstractEntity user, Point p) {
        if (!onCooldown) {
            code(user, p);
            onCooldown = true;
            cooldownTimer(cooldownTime >= 1 ? 1000 : Util.round(cooldownTime * 1000));
        }
    }
    
    public void code(AbstractEntity user, Point p) {
        System.out.println("Skill '" + name + "' doesn't have any code!");
    }
}
