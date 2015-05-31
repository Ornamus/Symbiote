package symbiote.resources;

import java.awt.Color;

public class Pixel {
    
    public final int x, y;
    public final Color color;
    
    public Pixel(int x, int y, int r, int g, int b) {
        this.x = x;
        this.y = y;
        color = new Color(r, g, b);
    }   
}
