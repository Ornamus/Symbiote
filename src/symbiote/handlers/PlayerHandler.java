package symbiote.handlers;

import java.util.ArrayList;
import symbiote.Main;
import symbiote.Server;
import symbiote.Thing;
import symbiote.client.Client;
import symbiote.entity.EntityPlayer;

public class PlayerHandler extends PacketHandler {

    public PlayerHandler() {
        super("player");
    }

    @Override
    public void onPacket(String message, String[] array) {
        if (!array[1].equals("disconnect")) { //Normal player update packet
            if (Main.server) {
                Server.broadcast(message, Client.communicator);
            }
            EntityPlayer player = null;
            for (Thing t : new ArrayList<>(Main.screen.things)) {
                if (t instanceof EntityPlayer) {
                    EntityPlayer possiblePlayer = (EntityPlayer) t;
                    if (possiblePlayer.name.equals(array[1])) {
                        if (player == null) {
                            player = possiblePlayer;
                        } else {
                            possiblePlayer.destroy();
                        }
                    }
                }
            }
            if (player == null) {
                player = (EntityPlayer) Thing.create(new EntityPlayer(0, 0));
                player.name = array[1];
                player.playing = false;
            }
            player.x = Double.parseDouble(array[2]);
            player.y = Double.parseDouble(array[3]);
            player.angle = Double.parseDouble(array[4]);
        } else { //Disconnect packet (player:disconnect:Ryan)
            EntityPlayer p = EntityPlayer.get(array[2]);
            if (p != null) {
                p.destroy();
            }
        }
    }
}
