package symbiote.misc;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;
import symbiote.client.Board;
import symbiote.client.Client;

public class Util {

    public static Random random = new Random();
    
    public static int round(double d) {
        return (int) Math.round(d);
    }
    
    public static Point getMouseOnScreen() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point p2 = Client.board.getLocationOnScreen();
        int mouseX = p.x - p2.x;
        int mouseY = p.y - p2.y;
        return new Point(mouseX, mouseY);
    }
    
    public static Point getMouseInWorld() {
        Point p = getMouseOnScreen();
        p.x += Board.offsetX;
        p.y += Board.offsetY;
        return p;
    }

    public static int randomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

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

    public static double sign(double d) {
        double sign = (Math.abs(d) / d);
        if (sign >= 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        return sign;
    }
    
    public static boolean same(Color c1, Color c2) {
        return (c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue());
    }
}
