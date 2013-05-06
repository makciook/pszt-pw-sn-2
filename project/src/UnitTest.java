import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created with IntelliJ IDEA.
 * User: maciek
 * Date: 06.05.13
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class UnitTest {
    Robot robot;
    Rectangle zero;
    double minx;
    double miny;
    double maxx;
    double maxy;
    double a;
    double b;

    public UnitTest(Rectangle start, int w, int h) {
        try {
            robot = new Robot();
            minx = start.getX();
            miny = start.getY();
            maxx = minx + w;
            maxy = miny + h;
            //System.out.println("minx: "+minx+", miny: "+miny+", maxx: "+maxx+", maxy: "+maxy);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void click(double x, double y, int which) {    // 0 - left, 1 - right
        //System.out.println("Sprawdzam: ("+x+","+y+")");
        if(x>minx && y>miny+25 && x<maxx && y<maxy) {
            robot.mouseMove((int)x,(int)y);
            robot.mousePress((which==0) ? InputEvent.BUTTON1_MASK : InputEvent.BUTTON3_MASK);
            robot.mouseRelease((which==0) ? InputEvent.BUTTON1_MASK : InputEvent.BUTTON3_MASK);
            //System.out.println("Klikam w: ("+x+","+y+")");
        }
    }

    public void drawRandomLine() {
        int min = 0;
        int max = 5;
        int which;
        double x,yrand, y;
        a = min + Math.random() * (max-min);
        b = min + Math.random() * (max-min);
        System.out.println("y="+a+"x+"+b);
        for(int i = 0; i < 800; ++i) {
            x = Math.random() * (maxx - minx);
            y = a*x+b;
            yrand = miny + Math.random() * (maxy - miny);
            which = (yrand <= y) ? 1 : 0;
            click(x+minx,yrand,which);
            //System.out.println("\tx:"+x+", y: "+y+", yrand: "+yrand);
        }

    }
}
