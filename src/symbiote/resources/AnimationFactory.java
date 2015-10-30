package symbiote.resources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnimationFactory { //TODO add a way to make certain frames last longer
    
    public List<BufferedImage> frames = new ArrayList<>();   
    public Boolean loop = true;
    public int speed = 4;
    
    public static AnimationFactory start() { return new AnimationFactory(); }
    
    public AnimationFactory addFrame(String imageName) {
        frames.add(ImageUtil.getImage(imageName));
        return this;
    }
    
    public AnimationFactory addFrames(String[] imageNames) {
        for (String s : imageNames) {
            addFrame(s);
        }
        return this;
    }
    
    public AnimationFactory loop(boolean b) {
        loop = b;
        return this;
    }
    
    public AnimationFactory speed(int i) {
        speed = i;
        return this;
    }
    
    public Animation finish() {
        Animation a = new Animation();
        a.frames = new ArrayList<>(frames);
        a.loop = loop;
        a.speed = speed;
        return a;
    }
}
