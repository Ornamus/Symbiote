package symbiote.world;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.Thing;
import symbiote.world.rooms.Room;

public class Wall extends Solid {
    
    public BufferedImage[][] images;
    
    public Wall(double x, double y, String imageName) {
        super(x, y, imageName);
        List<BufferedImage> parts = new ArrayList<>();
        int rowsHeight = 0;
        int rowLength = 0;
        for (int lY=0;lY<image.getHeight() - Room.gridUnit;lY += Room.gridUnit + 1) {
            rowsHeight++;
            rowLength = 0;
            for (int lX=0;lX<image.getWidth() - Room.gridUnit;lX += Room.gridUnit + 1) {
                rowLength++;
                parts.add(image.getSubimage(lX, lY, Room.gridUnit, Room.gridUnit + 1));
            }
        }
        images = new BufferedImage[rowsHeight][];
        int row = 0;
        while (!parts.isEmpty()) {
            int index = 0;
            BufferedImage[] currentRow = new BufferedImage[rowLength];
            for (BufferedImage i : new ArrayList<>(parts)) {
                if (index < rowLength) {
                    currentRow[index] = i;
                    parts.remove(i);
                    index++;
                } else {
                    break;
                }
            }
            images[row] = currentRow;
            row++;

        }
        System.out.println(rowsHeight + " " + rowLength);
        image = images[2][1];
    }    

    @Override
    public void tick() {
        super.tick();
        
        //TODO fix the magic gaps and the certain upward collums
        boolean up = false;
        boolean left = false;
        boolean right = false;
        boolean down = false;

        for (Thing t : Main.screen.things) {
            if (t instanceof Wall && t != this) {
                Shape s = t.getCollisionBox();
                if (s.contains(x, y - 1)) {
                    up = true;
                } else if (s.contains(x - 1, y)) {
                    left = true;
                } else if (s.contains(x + Room.gridUnit + 1, y)) {
                    right = true;
                } else if (s.contains(x, y + Room.gridUnit + 1)) {
                    down = true;
                }
            }
        }

        if (!up && !left && right && down) {
            image = images[0][0];
        } else if (!up && !left && !right && down) {
            image = images[0][1];
        } else if (!up && left && !right && down) {
            image = images[0][2];
        } else if (!up && left && right && !down) {
            image = images[0][3];
        } else if (!up && !left && right && !down) {
            image = images[1][0];
        } else if (!up && !left && !right && !down) {
            image = images[1][1];
        } else if (!up && left && !right && !down) {
            image = images[1][2];
        } else if (up && !left && !right && down) {
            image = images[1][3];
        } else if (up && !left && right && !down) {
            image = images[2][0];
        } else if (up && !left && !right && !down) {
            image = images[2][1];
        } else if (up && left && !right && !down) {
            image = images[2][2];
        } else {
            image = images[2][3]; //Blank
        }
    }
}
