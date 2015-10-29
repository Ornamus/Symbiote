package symbiote.resources;

import java.awt.Graphics;
import symbiote.misc.Util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

    public static BufferedImage copy(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage flipImage(BufferedImage bi) {
        BufferedImage flipped = new BufferedImage(
                bi.getWidth(),
                bi.getHeight(),
                bi.getType());
        AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
        AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
        tran.concatenate(flip);

        Graphics2D g = flipped.createGraphics();
        g.setTransform(tran);
        g.drawImage(bi, 0, 0, null);
        g.dispose();

        return flipped;
    }

    public static void drawRotated(BufferedImage i, double x, double y, double angle, Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), x + (i.getWidth() / 2), y + (i.getHeight() / 2));
        AffineTransform old = g.getTransform();
        g.transform(transform);

        g.drawImage(i, Util.round(x), Util.round(y), null);
        
        g.setTransform(old);
    }
    
    /*
    int r = // red component 0...255
    int g = // green component 0...255
    int b = // blue component 0...255
    int col = (r << 16) | (g << 8) | b;
    img.setRGB(x, y, col);
    */
}
