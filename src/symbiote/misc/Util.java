package symbiote.misc;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import symbiote.client.Board;
import symbiote.client.Client;

public class Util {

    public static Random random = new Random();
        
    /**
     * Returns a rounded off int version of a double.
     * @return The rounded off int version of a double. 
     */
    public static int round(double d) {
        return (int) Math.round(d);
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list);
        return list;
    }

    /**
     * A linear interpolation, which doesn't check the bounds of the 'a' parameter.
     * @param start The lowest value at a=0
     * @param end The highest value at a=1
     * @param a The interpolation between start and end, from (0-1) inclusive 
     * @return The interpolated value
     */
    public static double unboundedLerp(double start, double end, double a) {
        return (1 - a) * start + a * end;
    }
    
    /**
     * A linear interpolation, which constraints the bounds of the 'a' parameter to between 0-1 inclusive.
     * @param start The lowest value at a=0
     * @param end The highest value at a=1
     * @param a The interpolation between start and end, from (0-1) inclusive 
     * @return The interpolated value
     */
    public static double lerp(double start, double end, double a) {
        return unboundedLerp(start, end, Math.min(Math.max(a, 0), 1));
    }
    
    /**
     * Gets the mouse's location on-screen (not relative to game objects, just the JPanel itself)
     * @return The mouse's location on-screen.
     */
    public static Point getMouseOnScreen() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point p2 = Client.board.getLocationOnScreen();
        int mouseX = p.x - p2.x;
        int mouseY = p.y - p2.y;
        return new Point(mouseX, mouseY);
    }
    
    /**
     * Gets the mouse's location relative to where it is in the game world.
     * @return The mouse's location relative to where it is in the game world.
     */
    public static Point getMouseInWorld() {
        Point p = getMouseOnScreen();
        p.x += Board.offsetX;
        p.y += Board.offsetY;
        return p;
    }

    /**
     * Gets a random int between min and max (inclusive).
     * @param min The smallest number that should be returned.
     * @param max The biggest number that should be returned.
     * @return  A random int between min and max.
     */
    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    /**
     * Gets the angle between line1 and line2. A negative angle is left and a positive angle is right.
     * @return The angle between line1 and line2.
     */
    public static double lineAngle(Line2D line1, Line2D line2) {
        double xDiff1 = line1.getX2() - line1.getX1();
        double xDiff2 = line2.getX2() - line2.getX1();
        double yDiff1 = line1.getY2() - line1.getY1();
        double yDiff2 = line2.getY2() - line2.getY1();
        double angle = Math.atan2(xDiff1 * yDiff2 - yDiff1 * xDiff2, xDiff1 * xDiff2 + yDiff1 * yDiff2);
        double[] crossProduct = crossProduct(new double[]{xDiff1, yDiff1, 0}, new double[]{xDiff2, yDiff2, 0});
        if (sign(crossProduct[2]) == -1) { //Turn left
            if (angle < 0) {
                angle *= -1; //Make it positive
            }
        } else { //Turn right
            if (angle > 0) {
                angle *= -1; //Make it negative
            }
        }
        angle *= 57.2957795; //Convert from radians to degrees
        return angle;
    }

    /**
     * Gets the angle between (x, y) and (x2, y2).
     * @return 
     */
    public static double angle(double x, double y, double x2, double y2) {
        return lineAngle(new Line2D.Double(x, y, x2, y2), new Line2D.Double(x2, y2, x2, y2 - 1));
    }

    public static double[] crossProduct(double[] v0, double[] v1) {
        double crossProduct[] = new double[3];

        crossProduct[0] = v0[1] * v1[2] - v0[2] * v1[1];
        crossProduct[1] = v0[2] * v1[0] - v0[0] * v1[2];
        crossProduct[2] = v0[0] * v1[1] - v0[1] * v1[0];

        return crossProduct;
    }

    /**
     * Gets the sign of a double.
     * @return 1 if the double is 0 or bigger, -1 if it is smaller than 0.
     */
    public static double sign(double d) {
        double sign = (Math.abs(d) / d);
        if (sign >= 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        return sign;
    }
    
    /**
     * Checks whether the RGB values of two colors match.
     * @return Whether the RGB values of two colors match.
     */
    public static boolean same(Color c1, Color c2) {
        return (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue());
    }
}
