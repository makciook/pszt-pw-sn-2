import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

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
        if(x>minx+5 && y>miny+25 && x<maxx-5 && y<maxy-5) {
            robot.mouseMove((int)x,(int)y);
            robot.mousePress((which==0) ? InputEvent.BUTTON1_MASK : InputEvent.BUTTON3_MASK);
            robot.mouseRelease((which==0) ? InputEvent.BUTTON1_MASK : InputEvent.BUTTON3_MASK);
            System.out.println("Klikam w: ("+x+","+y+")");
        }
    }

    public void drawStraightLine() {
        int min = 0;
        int max = 5;
        int which;
        double x,yrand, y;
        double a = min + Math.random() * (max-min);
        double b = min + Math.random() * (max-min);
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

    public void drawPozioma() {
        int min = 0;
        int max = 5;
        int which;
        double x,yrand, y;
        double a = 0;
        double b = 200;
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

    public void drawPionowa() {
        int min = 0;
        int max = 5;
        int which;
        double x,yrand, y;
        double a = 200;
        double b = 0;
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

    public void drawStraightLineOneByOne() {
        double min = 0;
        double max = 1.5;
        int which = 0,lastwhich = 0;
        double x, yrand=0, y;
        double a = min + Math.random() * (max-min);
        double b = min + Math.random() * (100-min);
        System.out.println("y="+a+"x+"+b);
        for(int i = 0; i < 800; ++i) {
            x = Math.random() * (maxx - minx);
            y = a*x+b;
            do {
                yrand = miny + Math.random() * (maxy-miny);
                which = (yrand <= y) ? 1 : 0;
            } while(which==lastwhich);
            System.out.println("\t"+i+"\tx:"+x+", y: "+y+", yrand: "+yrand);
			try {
				click(x+minx,yrand,which);
			} catch(Exception e) { }
			
            lastwhich = which;
        }
    }

    public void drawCircle() {
        double a = 100 + Math.random() * (300-100);
        double b = 100 + Math.random() * (300-100);
        double r = 50 + Math.random() * (200-50);
        double x,y ;
        int which=0;

        for(int i = 0; i < 800; ++i) {
            x = Math.random() * (maxx - minx);
            y = Math.random() * (maxx - minx);
            if( (x-a)*(x-a)+(y-b)*(y-b) <= r*r )
                which = 0;
            else
                which = 1;

            click(x+minx,y,which);
        }

    }

    public void drawQuadOneByOne() {
        int min = 0;
        int max = 2;
        int which = 0,lastwhich = 0;
        double x, yrand=0, y;
        double a = min + Math.random() * (0.001-min);
        double b = min + Math.random() * (0.1-min);
        double c = min + Math.random() * (200-min);
        System.out.println("y="+a+"x+"+b);
        for(int i = 0; i < 800; ++i) {
            x = Math.random() * (maxx - minx);
            y = a*x*x+b*x+c;

                yrand = miny + Math.random() * (maxy-miny);
                which = (yrand <= y) ? 1 : 0;

            click(x+minx,yrand,which);
            System.out.println("\tx:"+x+", y: "+y+", yrand: "+yrand);
        }
    }

    public void switchView() {
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
    }

    public void screenshot(String filename) {

        try {
            BufferedImage screencapture = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) ).getSubimage(0,0,400,400);

            File file = new File(filename);

            ImageIO.write(screencapture, "jpg", file);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
