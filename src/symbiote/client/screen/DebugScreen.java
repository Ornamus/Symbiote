package symbiote.client.screen;

import symbiote.entity.AbstractEntity;
import symbiote.world.WorldUtil;

public class DebugScreen extends Screen {
    
    public DebugScreen() {  
        //TODO: Server tells client what blocks should exist and what their IDs are
        
        for (AbstractEntity e : WorldUtil.createWall(20, 100, 16, 2, 3)) { //Top wall
            thingMap.put(e.id, e);
        }
        
        for (AbstractEntity e : WorldUtil.createWall(20 - 64, 292, 2, 2, 9)) { //Left wall
            thingMap.put(e.id, e);
        }
        
        for (AbstractEntity e : WorldUtil.createWall(20 + (16* 32), 292, 2, 2, 9)) { //Right wall
            thingMap.put(e.id, e);
        }
        
        for (AbstractEntity e : WorldUtil.createWall(20 + (5* 32), 100 + (32*5), 6, 2, 1)) { //Middle wall wall
            thingMap.put(e.id, e);
        }
               
        for (AbstractEntity e : WorldUtil.createFloor(-11 - 64, 100-(32*4), 22, 13)) { //Floor       
            thingMap.put(e.id, e);
        }
    }
}
