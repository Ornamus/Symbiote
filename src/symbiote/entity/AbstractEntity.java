package symbiote.entity;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import symbiote.Main;
import symbiote.client.Client;
import symbiote.network.AbstractPacket;
import symbiote.server.Server;
import symbiote.world.Block;

public abstract class AbstractEntity {
    public static int getNextID() {
        return ++STATICID;
    }
    
    private static int STATICID = -1;
    
    public AbstractEntity() {
    }
    
    public AbstractEntity(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public int id = -1;
    
    public double x = 0;
    public double y = 0;
    public double width = 16;
    public double height = 16;
    
    public double xVel = 0;
    public double yVel = 0;
    public double speed = 3;
    public boolean velocity = true;
    public boolean velocityDecrease = true;
    
    public double angle = 0;
    public double size = 1; //TODO: Phase this out because it's useless if you manually handle width and height...?
    
    public void tick() {
        physicsTick();
    }
    public abstract AbstractPacket getPacket();
    
    public void destroy() {
        // TODO: determine side-effects
        if (Main.client) {
            Client.screen.thingMap.remove(this.id);
        } else {
            Server.entities.remove(this.id);
        }
    }
    
    
    /**
     * Checks whether this thing intersects a shape.
     * @return Whether this thing intersects a shape.
     */
    public boolean intersects(Shape shape) {
        Area areaA = new Area(getCollisionBox());
        areaA.intersect(new Area(shape));
        return !areaA.isEmpty();
    }
    
    /**
     * Checks whether this thing intersects another Thing.
     * @return Whether this thing intersects another Thing.
     */
    public boolean intersects(AbstractEntity t) {
        return intersects(t.getCollisionBox());
    }
    
    /**
     * Gets the collision box of this Thing.
     * @return The collision box of this Thing.
     */
    public Shape getCollisionBox() {
        Shape shape = new Rectangle2D.Double(x, y, width, height);
        //if (!dontUse) {
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), x + width / 2, y + height / 2);
            shape = transform.createTransformedShape(shape);
        //} else {
        //    shape = new Rectangle2D.Double(-1, -1, 0, 0);
        //}
        return shape;
    }

    public double getCenterX() {
        return x + ((width * size) / 2);
    }
    
    public double getCenterY() {
        return y + ((height * size) / 2);
    }
    
    public double getCollisionBoxCenterX() {
        Rectangle box = getCollisionBox().getBounds();
        return box.x + (box.width / 2);
    }
    
    public double getCollisionBoxCenterY() {
        Rectangle box = getCollisionBox().getBounds();
        return box.y + (box.height / 2);
    }
    
    public void physicsTick() {
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
    
    public void collide(AbstractEntity e) {}
    
    public List<AbstractEntity> collisions = new ArrayList<>();
    
    /**
     * Fixes the coordinates of this Entity in the case that it is intersecting a Solid and calls all relevant collide() functions.
     * @param adjustX Whether the Entity needs to be fixed on the X plane or Y plane in the case of it colliding with a Solid.
     */
    public void handleCollisions(boolean adjustX) {
        for (AbstractEntity t : Client.screen.thingMap.values()) {
            if (t != this && !collisions.contains(t)) {
                if (intersects(t)) {
                    collide(t);
                    t.collide(this);
                    collisions.add(t);
                }
                if (t instanceof Block) {
                    while (intersects(t)) {
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

    /**
     * Sets the position and angle of this entity.
     * @param x
     * @param y
     * @param angle
     */
    public void setPositionAndAngle(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
