package symbiote.world.rooms;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import symbiote.Thing;
import symbiote.misc.Util;
import symbiote.resources.ConfigFile;
import symbiote.resources.ImageHandler;
import symbiote.resources.Pixel;
import symbiote.world.Wall;

public class Room extends ThingCollection {
    
    public static int gridUnit = 16;
    public List<Exit> exits = new ArrayList<>();
    public BufferedImage image;
    private List<Pixel> borderPixels = new ArrayList<>();
    public ConfigFile config;
    
    public Room(double x, double y, String name) {
        super(x, y, new ArrayList<>());
        this.x = x;
        this.y = y;
        image = ImageHandler.getImage(name + ".png", getClass());
        config = new ConfigFile(name + ".ini", getClass()); 
        
        List<Pixel> exitPixels = new ArrayList<>();
        for (Pixel p : ImageHandler.getPixels(image)) {
            if (!Util.same(p.color, new Color(0, 0, 0))) { //If not pure black/transparancy
                if (Util.same(p.color, new Color(153, 217, 234))) { //Exit color
                    exitPixels.add(p);
                } else {
                    things.add(new Wall(p.x * gridUnit, p.y * gridUnit, "tile.png"));
                }
                borderPixels.add(p);
            }
        }
        while (!exitPixels.isEmpty()) {
            List<Pixel> usedParts = new ArrayList<>();
            Pixel part = exitPixels.get(0);
            usedParts.add(part);
            exitPixels.remove(part);
            boolean found = true;
            while (found) {
                found = false;
                for (Pixel p : exitPixels) {
                    for (Pixel p2 : new ArrayList<>(usedParts)) {
                        if (!usedParts.contains(p)) {
                            if ((p.x == p2.x || p.x - 1 == p2.x || p.x + 1 == p2.x) && (p.y == p2.y || p.y - 1 == p2.y || p.y + 1 == p2.y)) {
                                found = true;
                                usedParts.add(p);
                            }
                        }
                    }
                }
            }
            List<Thing> exitSpots = new ArrayList<>();
            for (Pixel p : usedParts) {
                exitSpots.add(new Thing(p.x * gridUnit, p.y * gridUnit, "exit.png"));
                exitPixels.remove(p);
            }
            //System.out.println("making an exit");
            exits.add(new Exit(exitSpots, this));

        }              
        add(x, y);
        
        //TODO Allign to grid, make it not laggy, draw behind everything else
        Area area = getArea(true);
        for (int dY = 0; dY < area.getBounds().height; dY += gridUnit) {
            for (int dX = 0; dX < area.getBounds().width; dX += gridUnit) {
                if (area.contains(dX, dY) && area.contains(dX + gridUnit, dY + gridUnit)) {
                    //System.out.println("Made back at " + dX + ", " + dY);
                    //things.add(new Wall(dX, dY, "back.png"));
                }
            }
        }
    }
    
    public boolean atPoint(int x, int y, List<Pixel> pixels) {
        for (Pixel p : pixels) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }
    
    public boolean intersects(Room r, boolean useBorder) {
        Area a = getArea(useBorder);
        a.intersect(r.getArea(useBorder));
        return !a.isEmpty();
    }
    
    public Area getArea(boolean useBorder) {
        boolean excludeBorder = !useBorder;
        int decreaseAmount = (excludeBorder ? gridUnit : 0);
        List<Shape> shapes = new ArrayList<>();
        for (int pY = excludeBorder ? 1 : 0; pY < image.getHeight() + (excludeBorder ? -1 : 0); pY++) {
            int startX = -1, endX = -1;
            for (int pX = 0; pX < image.getWidth(); pX++) {
                if (atPoint(pX, pY, borderPixels)) {
                    if (startX == -1) {
                        startX = pX;
                    } else {
                        
                            endX = pX;
                    }
                    
                }
            }
            //System.out.println(startX + ", " + endX);
            Rectangle r = new Rectangle((startX * gridUnit) + Util.round(x) + decreaseAmount, (pY * gridUnit) + Util.round(y), (endX - startX + 1) * gridUnit - (decreaseAmount * 2), gridUnit);
            //System.out.println(r.getX() + ", " + r.getY() + ", " + r.getWidth() + ", " + r.getHeight());
            shapes.add(r);
        }
        Area a = new Area(new Rectangle(Util.round(x), Util.round(y), 0, 0));
        for (Shape s : shapes) {
            a.add(new Area(s));
        }  
        return a;
    }
    
    @Override
    public void add(double x, double y) {
        super.add(x, y);
        for (Exit e : exits) {
            e.add(x, y);
        }
    }
}
