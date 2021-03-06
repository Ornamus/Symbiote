package symbiote.network;

import symbiote.client.Client;
import symbiote.entity.client.ClientEntityPlayer;
import symbiote.entity.client.ClientEntityThisPlayer;
import symbiote.network.Communicator.Type;

/**
 * Directive from Server to create a player
 */
public class SPacketPlayer extends SAbstractPacketEntity {
    @Override
    public String toString() {
        return "SPacketPlayer [name=" + name + ", you=" + you + ", getId()="
                + getId() + ", getX()=" + getX() + ", getY()=" + getY()
                + ", getAngle()=" + getAngle() + "]";
    }

    private String name;
    private boolean you;
    
    public SPacketPlayer(int id, String name, double x, double y, double angle, boolean you) {
        super(id, x, y, angle);
        
        this.name = name;
        this.you = you;
    }
    
    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            ClientEntityPlayer player;
            if (this.you) {
                player = new ClientEntityThisPlayer(this.getId(), this.name, this.getX(), this.getY());
                Client.focus = player;
            } else {
                player = new ClientEntityPlayer(this.getId(), this.name, this.getX(), this.getY());
            }
            
            player.angle = this.getAngle();
            
            Client.screen.thingMap.put(this.getId(), player);
        }
    }
    
    public boolean isYou() {
        // TODO: determine based on recipient?
        return you;
    }

    public void setYou(boolean you) {
        this.you = you;
    }
}
