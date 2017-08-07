package symbiote.network;

import symbiote.client.Client;
import symbiote.entity.client.ClientEntityBullet;
import symbiote.network.Communicator.Type;

public class SPacketBullet extends SAbstractPacketEntity {
    @Override
    public String toString() {
        return "SPacketBullet [owner=" + owner + ", getId()=" + getId()
                + ", getX()=" + getX() + ", getY()=" + getY() + ", getAngle()="
                + getAngle() + "]";
    }

    int owner;
    
    public SPacketBullet(int id, double x, double y, double angle, int owner) {
        super(id, x, y, angle);
        this.owner = owner;
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            ClientEntityBullet bullet;
            bullet = new ClientEntityBullet(this.getId(), this.getX(), this.getY(), this.getAngle(), owner);
            
            if (Client.screen.thingMap.containsKey(this.getId())) {
                System.out.println("Already has id!");
            }
            Client.screen.thingMap.put(this.getId(), bullet);
            
            //Keep this god-awful sound commented out until we get a new sound that isn't COMPLETELY HORRIFIC
            //AudioHandler.playSound("gun.wav");
            
        }
    }

}
