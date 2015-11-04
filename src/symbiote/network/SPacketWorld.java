package symbiote.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Log;
import symbiote.network.Communicator.Type;
import symbiote.server.Server;
import symbiote.world.BackgroundTile;
import symbiote.world.Block;

public class SPacketWorld extends AbstractPacket {
    String worldData;
    
    public SPacketWorld() {
    }
    
    public SPacketWorld(boolean createWorldData) {
        if (createWorldData) {
            worldData = "";
            boolean exists = false;
            for (AbstractEntity e : Server.entities.values()) {
                if (e instanceof Block) {
                    exists = true;
                    Block b = (Block) e;
                    worldData = worldData + "block:" + b.id + ":" + b.x + ":" + b.y + ":" + b.imageName + ":" + b.hitbox + "]";
                } else if (e instanceof BackgroundTile) {
                    exists = true;
                    BackgroundTile b = (BackgroundTile) e;
                    worldData = worldData + "tile:" + b.id + ":" + b.x + ":" + b.y + ":" + b.imageName + "]";
                }
            }
            if (!exists) {
                Log.w("There is no world data!");
            }
        }
    }
    
    @Override
    public void write(ObjectOutputStream out) throws Exception {
        out.writeUTF(worldData);
    }

    @Override
    public void read(ObjectInputStream in) throws Exception {
        this.worldData = in.readUTF();
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            for (String part : worldData.split("]")) {
                String[] array = part.split(":");
                if (array[0].equals("block")) {
                    Block b = new Block(Integer.parseInt(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]), array[4], Boolean.parseBoolean(array[5]));
                    Client.screen.thingMap.put(b.id, b);
                } else if (array[0].equals("tile")) {
                    BackgroundTile b = new BackgroundTile(Integer.parseInt(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]), array[4]);
                    Client.screen.thingMap.put(b.id, b);
                } else {
                    Log.e("Invalid world object type \"" + array[0] + "\"!");
                }
            }
        }
    }
}