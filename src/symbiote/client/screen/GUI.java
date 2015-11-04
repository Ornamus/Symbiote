package symbiote.client.screen;

import java.awt.Graphics2D;
import symbiote.entity.client.Drawable;
import symbiote.misc.Log;

public class GUI implements Drawable {
    
    public double x, y;
    
    public GUI(double x, double y) {
        this.x = x;
        this.y = y;       
    }
    
    @Override
    public void draw(Graphics2D g) {
        Log.w("A GUI at (" + x + ", " + y + ") doesn't have any draw code!");
    }   
}
