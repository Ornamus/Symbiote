package symbiote.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    
    public int index = 0;
    public int skips = 0;
    
    protected List<BufferedImage> frames = new ArrayList<>();  
    public boolean loop = true;
    public int speed = 4;
    
    private boolean finished = false;
    public AnimationHandler handler = null;

    public BufferedImage getCurrentFrame() {
        BufferedImage frame = frames.get(index);

        if (skips == speed) {
            index++;
            skips = 0;
        } else {
            skips++;
        }
        
        if (index == frames.size()) {
            if (loop) {
                index = 0;
            } else {
                index--;
                if (!finished) {
                    finished = true;
                    if (handler != null) {
                        handler.animationEnd(this);
                    }
                }
            }
        }
        return frame;
    }
    
    public BufferedImage getCurrentFrame(boolean increase) {
        if (increase) {
            return getCurrentFrame();
        } else {
            return frames.get(index);
        }
    }

    public int getLength() {
        return frames.size();
    }
    
    public List<Image> getFrames() {
        return new ArrayList<>(frames);
    }
}
