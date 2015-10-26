package symbiote.client.screen;

import symbiote.entity.AbstractEntity;
import symbiote.world.DungeonGenerator;

public class DebugScreen extends Screen {
    
    public DebugScreen() {  
        for (AbstractEntity t : DungeonGenerator.generate()) {
            // TODO: add to client
//            t.id = AbstractEntity.getNextID();            
//            Client.screen.thingMap.put(t.id, t);
        }
        
//        if (Main.client) {
//            if (Client.symbiote) {
//                things.add(new EntitySymbiote(320, 320));
//            } else {
//                EntityPlayer p = new EntityPlayer(300, 300);
//                p.name = Client.name;
//                things.add(p);
//            }
//            
//            EntityPlayer p = new EntityPlayer(300, 300);
//            p.controlling = false;
//            p.name = "Test";
//            things.add(p);
//            things.add(new EntitySymbiote(320, 320));
//        }
    }
}
