package symbiote.entity;

import symbiote.network.AbstractPacket;
import symbiote.network.SPacketPlayer;

public class EntityPlayer extends LivingEntity {
    public EntityPlayer(int id, String name, double x, double y) {
        super(id, x, y);
        this.name = name;
    }

    @Override
    public AbstractPacket getPacket() {
        return new SPacketPlayer(id, name, x, y, angle, false);
    }
}
