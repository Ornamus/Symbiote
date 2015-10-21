package symbiote.world.rooms;

import java.util.List;

import symbiote.entity.AbstractEntity;
import symbiote.misc.CrashError;

public class Exit extends ThingCollection {
    
    public boolean hallwayPossible;
    public boolean horizontal;
    public boolean vertical;
    public Direction direction = null;
    
    public boolean taken = false;
    
    public Exit(List<AbstractEntity> parts, Room r) {
        super(0, 0, parts);
        double firstX = parts.get(0).x;
        double firstY = parts.get(0).y;
        for (AbstractEntity t : parts) {
            if (t.x != firstX) {
                vertical = true;
                break;
            }
            if (t.y != firstY) {
                horizontal = true;
                break;
            }
        }
        hallwayPossible = !(horizontal && vertical);
        if (hallwayPossible) {
            int index = 1;
            String v;
            while (true) {
                v = "Exit" + index;
                if (r.config.exists(v + ".Coordinates") && r.config.exists(v + ".Direction")) {
                    String[] coordinateParts = r.config.getString(v + ".Coordinates").split(",");
                    if (contains(Double.parseDouble(coordinateParts[0]) * Room.gridUnit, Double.parseDouble(coordinateParts[1]) * Room.gridUnit)) {
                        String dir = r.config.getString(v + ".Direction");
                        if (dir.equalsIgnoreCase("UP")) {
                            direction = Direction.UP;
                        } else if (dir.equalsIgnoreCase("DOWN")) {
                            direction = Direction.DOWN;
                        } else if (dir.equalsIgnoreCase("LEFT")) {
                            direction = Direction.LEFT;
                        } else if (dir.equalsIgnoreCase("RIGHT")) {
                            direction = Direction.RIGHT;
                        } else {
                            new CrashError(r.config.name + " has invalid direction information for " + v + "!");
                        }
                        break;
                    }
                    index++;
                } else {
                    new CrashError(r.config.name + " is missing exit information!");
                }
            }
        }
        x = getLowestX();
        y = getLowestY();
    }
    
    public boolean contains(double x, double y) {
        for (AbstractEntity t : things) {
            if (t.getCollisionBox().contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canAttach(Exit e) {
        return (hallwayPossible && e.hallwayPossible && direction.opposite(e.direction) && getRelevantDimension() == e.getRelevantDimension() && !taken && !e.taken);
    }

    public double getLowestX() {
        AbstractEntity lowest = null;
        for (AbstractEntity t : things) {
            if (lowest == null || t.x < lowest.x) {
                lowest = t;
            }
        }
        return lowest.x;
    }
    
    public double getHighestX() {
        AbstractEntity highest = null;
        for (AbstractEntity t : things) {
            if (highest == null || t.x > highest.x) {
                highest = t;
            }
        }
        return highest.x;
    }
    
    public double getLowestY() {
        AbstractEntity lowest = null;
        for (AbstractEntity t : things) {
            if (lowest == null || t.y < lowest.y) {
                lowest = t;
            }
        }
        return lowest.y;
    }
    
    public double getHighestY() {
        AbstractEntity highest = null;
        for (AbstractEntity t : things) {
            if (highest == null || t.y > highest.y) {
                highest = t;
            }
        }
        return highest.y;
    }
    
    /**
     * Returns the "relevant dimension", which is either this Exit's width or height, depending on what direction the exit is.
     * @return The "relevant dimension", which is either this Exit's width or height, depending on what direction the exit is.
     */
    public double getRelevantDimension() {
        if (hallwayPossible) {
            if (horizontal) {
                return getHighestY() - getLowestY();
            } else {
                return getHighestX() - getLowestX();
            }
        }
        return -1;
    }
    
    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);
        
        public double x,y;
        
        Direction(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public boolean opposite(Direction d) {
            if (this == Direction.UP) {
                return (d == Direction.DOWN);
            } else if (this == Direction.DOWN) {
                return (d == Direction.UP);
            } else if (this == Direction.LEFT) {
                return (d == Direction.RIGHT);
            } else if (this == Direction.RIGHT) {
                return (d == Direction.LEFT);
            } else {
                return false;
            }
        }
    }
}
