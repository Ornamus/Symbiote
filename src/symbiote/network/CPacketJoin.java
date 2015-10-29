package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import symbiote.entity.AbstractEntity;
import symbiote.entity.EntityPlayer;
import symbiote.entity.EntitySymbiote;
import symbiote.network.Communicator.Type;
import symbiote.server.Server;

public class CPacketJoin extends AbstractPacket {
    String name;
    
    public CPacketJoin() {
    }
    
    public CPacketJoin(String name) {
        this.name = name;
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(this.name);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.name = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.SERVERSIDE) {
            boolean nameTaken = false;
            for (Communicator c : Server.clients) {
                if (c.name.equals(this.name)) {
                    nameTaken = true;
                    break;
                }
            }
            
            if (!nameTaken) {
                boolean symb = Server.symbioteNeeded;
                comm.sendMessage(new SPacketAccepted(this.name, symb));
                comm.name = this.name;
                Server.symbioteNeeded = false;
                
                Server.gui.log(this.name + " has joined.");
                Server.clients.add(comm);
                Server.gui.refreshClients();
                
                int id = AbstractEntity.getNextID();
                if (symb) { // TODO: this whole block could be done smarter
                    EntitySymbiote entity = new EntitySymbiote(id, comm.name, 320, 320);
                    Server.broadcastExcept(entity.getPacket(), comm);
                    
                    SPacketSymbiote yourself = ((SPacketSymbiote) entity.getPacket());
                    yourself.setYou(true);
                    comm.sendMessage(yourself);
                    
                    Server.entities.put(id, entity);
                } else {
                    EntityPlayer entity = new EntityPlayer(id, comm.name, 320, 320);
                    Server.broadcastExcept(entity.getPacket(), comm);
                    
                    SPacketPlayer yourself = ((SPacketPlayer) entity.getPacket());
                    yourself.setYou(true);
                    comm.sendMessage(yourself);
                    
                    Server.entities.put(id, entity);
                }
                
                // inform the new client of all existing things
                //TODO: Bullets that have long since been shot and despawned are appearing for new clients
                for (AbstractEntity thing : Server.entities.values()) {
                    if (thing.id != id) {
                        AbstractPacket p = ((AbstractEntity) thing).getPacket();
                        if (p != null) {
                            comm.sendMessage(p);
                            System.out.println("Informing of " + thing);
                        }
                    }
                }
            } else {
                comm.sendMessage(new SPacketDenied("Name taken"));
                Server.gui.log("New user submitted invalid name \"" + this.name + "\".");
            }
        }
    }

}
