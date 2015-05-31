package symbiote.world;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import symbiote.Thing;
import symbiote.misc.Pair;
import symbiote.misc.Util;
import symbiote.world.rooms.Exit;
import symbiote.world.rooms.Exit.Direction;
import symbiote.world.rooms.Hallway;
import symbiote.world.rooms.Room;

public class DungeonGenerator {
    
    public static String[] roomNames = {"test"};
    private static List<Thing> things = new ArrayList<>();
    private static List<Room> rooms = new ArrayList<>();
    
    public static List<Thing> generate() { //Program every task so that it loops and can terminate once all available options have been tried and failed
        
        things.clear();
        rooms.clear();
    
        Room prevRoom = new Room(10, 10, roomNames[Util.randomInt(0, roomNames.length - 1)]);
        rooms.add(prevRoom);

        /*
         for (int i=0; i<5; i++) {
         prevRoom = rooms.get(Util.randomInt(0, rooms.size() - 1));
         Room newRoom = new Room(10, 10, roomNames[Util.randomInt(0, roomNames.length - 1)]);
         if (attach(prevRoom, newRoom)) {
         rooms.add(newRoom);
         } else {
         i--;
         //System.out.println("A room failed to be created");
         }
         }
         */
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> result = makeHallway(prevRoom);
            if ((Boolean) result.get("success")) {
                Hallway h = (Hallway) result.get("hallway");
                for (Thing t : h.things) {
                    things.add(t);
                }
            }
        }

        for (Room r : rooms) {
            for (Thing t : r.things) {
                things.add(t);
            }
        }
        return things;
    }

    public static HashMap<String, Object> makeHallway(Room r) {
        HashMap<String, Object> result = new HashMap();
        Hallway h = null;

        Exit e = null;
        Collections.shuffle(r.exits);
        for (Exit exit : r.exits) {
            if (exit.hallwayPossible && !exit.taken) {
                e = exit;
            }
        }
        if (e != null) {
            Rectangle re = null;
            int length = Util.randomInt(10, 15) * Room.gridUnit;
            if (e.horizontal) {
                re = new Rectangle(Util.round(e.getLowestX() + (e.direction == Direction.LEFT ? -length : 16)), Util.round(e.getLowestY()) - Room.gridUnit, Util.round(length), Util.round(e.getRelevantDimension()) + (Room.gridUnit * 3));
            } else if (e.vertical) {
                re = new Rectangle(Util.round(e.getLowestX()) - Room.gridUnit, Util.round(e.getLowestY()) + (e.direction == Direction.UP ? -length : Room.gridUnit), Util.round(e.getRelevantDimension()) + (Room.gridUnit * 3), length);
            }
            h = new Hallway(re, e.horizontal);
            //h.things.add(new Thing(e.getLowestX(), e.getLowestY(), "exit.png"));
            
            
            result.put("success", true);
            e.taken = true;
        }
        if (result.get("success") == null) result.put("success", false);
        result.put("hallway", h);

        return result;
    }
    
    public static boolean attach(Room r1, Room r2) {
        List<Pair<Exit, Exit>> failed = new ArrayList<>();
        while (true) {
            Pair<Exit, Exit> pair = new Pair(null, null);
            boolean stop = false;
            Collections.shuffle(r1.exits);
            for (Exit e1 : r1.exits) {
                for (Exit e2 : r2.exits) {
                    pair.one = e1;
                    pair.two = e2;
                    if (!contains(failed, pair) && e1.canAttach(e2)) {
                        stop = true;
                        break;
                    } else {
                        pair.one = null;
                        pair.two = null;
                    }
                }
                if (stop) break;
            }
            if (pair.one != null && pair.two != null) {
                double xDiff = pair.one.getLowestX() - pair.two.getLowestX();
                double yDiff = pair.one.getLowestY() - pair.two.getLowestY();
                
                System.out.println(xDiff + ", " + yDiff);
                r2.add(xDiff, yDiff);
                boolean intersect = false;
                for (Room r : rooms) {
                    if (r.intersects(r2, false)) {
                        intersect = true;
                        break;
                    }
                }
                if (!intersect) {
                    pair.one.taken = true;
                    pair.two.taken = true;
                    return true;
                } else {
                    r2.add(-xDiff, -yDiff);
                    failed.add(pair);
                }
            } else {
                return false;
            }
        }
    }
    
    public static boolean attach(Exit ex, Room r) {
        List<Exit> failed = new ArrayList<>();
        while (true) {
            Exit ex2 = null;
            
            Collections.shuffle(r.exits);           
            for (Exit e : r.exits) {                
                if (!failed.contains(e) && e.canAttach(ex)) {
                    ex2 = e;
                    break;
                }
            }
            if (ex2 != null) {
                double xDiff = ex.getLowestX() - ex2.getLowestX();
                double yDiff = ex.getLowestY() - ex2.getLowestY();
                
                r.add(xDiff, yDiff);
                boolean intersect = false;
                for (Room r2 : rooms) {
                    if (r2.intersects(r, false)) {
                        intersect = true;
                        break;
                    }
                }
                if (!intersect) {
                    ex.taken = true;
                    ex2.taken = true;
                    return true;
                } else {
                    r.add(-xDiff, -yDiff);
                    failed.add(ex2);
                }
            } else {
                return false;
            }
        }
    }    
        
    public static boolean contains(List<Pair<Exit, Exit>> pairs, Pair<Exit, Exit> pair) {
        for (Pair p : pairs) {
            if (p.equals(pair)) {
                return true;
            }
        }
        return false;
    }
}
