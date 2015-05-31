package symbiote.screen;

import symbiote.Main;
import symbiote.Thing;
import symbiote.client.Client;
import symbiote.entity.EntityPlayer;
import symbiote.entity.EntitySymbiote;
import symbiote.world.DungeonGenerator;

public class DebugScreen extends Screen {
    
    public DebugScreen() {        
        for (Thing t : DungeonGenerator.generate()) {
            things.add(t);
        }
        if (Main.client) {
            if (Client.symbiote) {
                things.add(new EntitySymbiote(320, 320));
            } else {
                EntityPlayer p = new EntityPlayer(300, 300);
                p.name = Client.name;
                things.add(p);
            }
//            EntityPlayer p = new EntityPlayer(300, 300);
//            p.controlling = false;
//            p.name = "Test";
//            things.add(p);
//            things.add(new EntitySymbiote(320, 320));
        }
    }
}
