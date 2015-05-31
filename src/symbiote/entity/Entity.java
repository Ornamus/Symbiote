package symbiote.entity;

import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.Thing;
import symbiote.resources.Animation;
import symbiote.resources.AnimationFactory;
import symbiote.world.Solid;

public class Entity extends Thing { //An entity is an object that moves or has physics.
    
    public double xVel = 0;
    public double yVel = 0;
    public double speed = 3;
    public Animation animation = AnimationFactory.start().addFrame("back.png").finish();
    public List<Thing> collisions = new ArrayList<>();
    
    public boolean velocity = true;
    public boolean velocityDecrease = true;
    
    public Entity(double x, double y) {
        super(x, y);
        image = animation.getCurrentFrame();
    }    
    
    @Override
    public void tick() {
        image = animation.getCurrentFrame();
        if (velocity) {
            x += xVel;
            handleCollisions(true);
            y += yVel;
            handleCollisions(false);

            if (velocityDecrease) {
                if (xVel > 0) {
                    xVel--;
                } else if (xVel < 0) {
                    xVel++;
                }
                if (yVel > 0) {
                    yVel--;
                } else if (yVel < 0) {
                    yVel++;
                }
            }
            collisions.clear();
        }
    }

    /**
     * Fixes the coordinates of this Entity in the case that it is intersecting a Solid and calls all relevant collide() functions.
     * @param adjustX Whether the Entity needs to be fixed on the X plane or Y plane in the case of it colliding with a Solid.
     */
    public void handleCollisions(boolean adjustX) {
        for (Thing t : Main.screen.things) {
            if (t != this && !t.destroyed && !collisions.contains(t)) {
                if (intersects(t)) {
                    if (t instanceof Solid) {
                        collide(((Solid) t)); //Collide with a wall.                        
                    } else if (t instanceof Entity) {
                        collide(((Entity) t)); //Collide with an entity.
                    } else {
                        collide(t); //Collide with a thing.
                    }
                    t.collide(this);
                    collisions.add(t);
                }
                while (t instanceof Solid && intersects(t)) {
                    if (adjustX) {
                        if (xVel > 0) {
                            x--;
                        } else {
                            x++;
                        }
                    } else {
                        if (yVel > 0) {
                            y--;
                        } else {
                            y++;
                        }
                    }
                }
            }
        }
    }
}
