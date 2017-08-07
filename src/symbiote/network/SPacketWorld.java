package symbiote.network;

import java.lang.reflect.Constructor;
import symbiote.Main;
import symbiote.client.Client;
import symbiote.entity.AbstractEntity;
import symbiote.misc.Log;
import symbiote.network.Communicator.Type;
import symbiote.server.Server;
import symbiote.world.Floor;
import symbiote.world.Block;

public class SPacketWorld extends AbstractPacket {
    String worldData;
    
    public SPacketWorld(boolean createWorldData) {
        if (createWorldData) {
            worldData = "";
            boolean exists = false;
            for (AbstractEntity e : Server.entities.values()) {
                if (e instanceof Block) {
                    exists = true;
                    Block b = (Block) e;
                    worldData = worldData + "block:" + b.getStringData() + "]";
                } else if (e instanceof Floor) {
                    exists = true;
                    Floor b = (Floor) e;
                    worldData = worldData + "tile:" + b.id + ":" + b.x + ":" + b.y + ":" + b.imageName + "]";
                }
            }
            if (!exists) {
                Log.w("There is no world data!");
            }
        }
    }

    @Override
    public void handle(Communicator comm) {
        if (comm.getType() == Type.CLIENTSIDE) {
            for (String part : worldData.split("]")) {
                String[] array = part.split(":");
                if (array[0].equals("block")) {
                    int id = Integer.parseInt(array[1]);
                    Class c = Main.idToBlock.get(id);
                    if (c != null) {
                        Constructor con = c.getConstructors()[0];
                        Class[] paramTypes = con.getParameterTypes();
                        Object[] args = new Object[paramTypes.length];
                        for (int i=0;i<paramTypes.length;i++) {
                            Class type = paramTypes[i];
                            if (type == Integer.class || type == int.class) {
                                args[i] = Integer.parseInt(array[2 + i]);
                            } else if (type == Double.class || type == double.class) {
                                args[i] = Double.parseDouble(array[2 + i]);
                            } else if (type == String.class) {
                                args[i] = array[2 + i];
                            } else if (type == Boolean.class || type == boolean.class) {
                                args[i] = Boolean.parseBoolean(array[2] + i);
                            }
                        }
                        try {
                            Block b = (Block) con.newInstance(args);
                            Client.screen.thingMap.put(b.id, b);
                        } catch (Exception e) {
                            Log.e("Unable to create block type \"" + id + "\". Exception: " + e.getMessage());
                        }
                    } else {
                        Log.e("Invalid block id \"" + id + "\" given in packet!");
                    }                                                  
                } else {
                    Log.e("Invalid world object type \"" + array[0] + "\"!");
                }
            }
        }
    }
}