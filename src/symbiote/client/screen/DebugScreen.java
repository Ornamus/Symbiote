package symbiote.client.screen;

import symbiote.entity.AbstractEntity;
import symbiote.world.WorldUtil;

public class DebugScreen extends Screen {
    
    public DebugScreen() {  
        //TODO: Server tells client what blocks should exist and what their IDs are
        
        for (AbstractEntity e : WorldUtil.createWall(20, 100, 6, 3)) {
            thingMap.put(e.id, e);
        }
        for (AbstractEntity e : WorldUtil.createFloor(-12, 100-(32*4), 8, 8)) {            
            thingMap.put(e.id, e);
        }
    }
}
