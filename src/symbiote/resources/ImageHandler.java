package symbiote.resources;

import symbiote.misc.Util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class ImageHandler {
    
    public static BufferedImage getImage(String name) {
        return toBufferedImage(new ImageIcon(ImageHandler.class.getResource(name)).getImage());       
    }    
    
    public static BufferedImage getImage(String name, Class c) {
        return toBufferedImage(new ImageIcon(c.getResource(name)).getImage());       
    }   
    
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    public static List<Pixel> getPixels(BufferedImage i) {
        List<Pixel> pixels = new ArrayList<>();
        for (int y = 0; y < i.getHeight(); y++) {
            for (int x = 0; x < i.getWidth(); x++) {
                int clr = i.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;              
                pixels.add(new Pixel(x, y, red, green, blue));
            }
        }
        return pixels;
    }
    
    public static Pixel getPixel(BufferedImage i, int x, int y) {
        for (Pixel p : getPixels(i)) {
            if (p.x == x && p.y == y) {
                return p;
            }
        }
        return null;
    }
    
    public static void drawRotated(BufferedImage i, double x, double y, double angle, Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), x + (i.getWidth() / 2), y + (i.getHeight() / 2));
        AffineTransform old = g.getTransform();
        g.transform(transform);

        g.drawImage(i, Util.round(x), Util.round(y), null);
        
        g.setTransform(old);
    }
}
