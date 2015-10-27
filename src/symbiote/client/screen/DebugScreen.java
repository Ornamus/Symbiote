package symbiote.client.screen;

import symbiote.world.Block;

public class DebugScreen extends Screen {
    
    public DebugScreen() {  
        //TODO: Coordinate with the server on IDs so that the blocks don't get overridden
        String[] blocks = {"WallTop", "WallTop", "WallTransition", "WallFront", "WallFront"};
        for (int i=0;i<5;i++) {
            Block b = new Block(999 + i, 20, 50 + (i * 32), blocks[i]);
            thingMap.put(999 + i, b);
        }
    }
}
