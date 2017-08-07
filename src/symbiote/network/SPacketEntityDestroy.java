package symbiote.network;

import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityUtil;
import symbiote.entity.LivingEntity;
import symbiote.misc.CrashError;

public class SPacketEntityDestroy extends AbstractPacket {
    @Override
    public String toString() {
        return "SPacketEntityDestroy [id=" + eID + "]";
    }
    
    int eID;
    
    public SPacketEntityDestroy(int eID) {
        this.eID = eID;
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Communicator.Type.CLIENTSIDE) {
            AbstractEntity e = EntityUtil.getEntityMap().get(eID);
            if (e instanceof LivingEntity && ((LivingEntity) e).name.equals(Client.name)) {
                new CrashError("You were kicked from the server, " + Client.name + "!");
            }
            if (e != null) {
                e.destroy();
            }
        }
    }
}
