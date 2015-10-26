package symbiote.client;

import java.awt.Point;
import java.awt.image.BufferedImage;
import symbiote.entity.AbstractEntity;
import symbiote.entity.LivingEntity;
import symbiote.entity.client.ClientEntityThisSymbiote;
import symbiote.misc.Util;
import symbiote.network.CPacketShoot;
import symbiote.network.CPacketSymbioteControl;

public abstract class Skill {
    
    //TODO: Skill icons and effects that make it obvious what skill is selected
    
    public static Skill SHOOT_BULLET = new Skill("Shoot"){
        @Override
        public void use(AbstractEntity e, Point p) {
            Point m = Util.getMouseInWorld();
            Client.communicator.sendMessage(new CPacketShoot(e.getCenterX(), e.getCenterY(), Util.angle(e.getCenterX(), e.getCenterY(), m.x, m.y), e.id));
        }
    };
    
    public static Skill SYMBIOTE_POSSESS = new Skill("Possess") {
        @Override
        public void use(AbstractEntity user, Point p) {
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
                            Client.communicator.sendMessage(new CPacketSymbioteControl(e.name));
                            break;
                            //}
                        }
                    }
                } else {
                    sim.controlledEntity.symbioteControlled = false;
                    sim.controlledEntity = sim;
                    useOnSelect = false;
                    Client.communicator.sendMessage(new CPacketSymbioteControl(sim.name));
                }
            } else {
                System.out.println("[ERROR] A non-symbiote entity attempted to use the possess skill!");
            }
        }
    };

    public static Skill NOTHING = new Skill("Do Nothing"){};
    
    
    public String name;
    public BufferedImage icon;
    public boolean useOnSelect = false;
    
    public Skill(String name) {
        this.name = name;
    }
    
    public void use(AbstractEntity user, Point p) {
        System.out.println("Skill '" + name + "' doesn't have any use code!");
    }
}
