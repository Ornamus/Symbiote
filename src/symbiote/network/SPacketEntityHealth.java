package symbiote.network;

import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.LivingEntity;
import symbiote.network.Communicator.Type;

public class SPacketEntityHealth extends AbstractPacket {
    @Override
    public String toString() {
        return "SPacketEntityHealth [id=" + id + ", health=" + health + "]";
    }
    
    int id;
    double health;

    
    public SPacketEntityHealth(int eID, double eHealth) {
        id = eID;
        health = eHealth;
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            AbstractEntity e = Client.screen.thingMap.get(id);
            if (e != null && e instanceof LivingEntity) {
                ((LivingEntity)e).setHealth(health);
            }
        }
    }
}