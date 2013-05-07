import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 27.03.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class main {
    private static final int TESTMODE = 2;     // 0 - bez testow
                                               // 1 - testy kompleksowe
                                               // 2 - test sinusa
    private static final int MAX_TESTS = 6;

    public static void main(String args[]) {

        if(TESTMODE == 0) {
            NeuronNetwork siec = new NeuronNetwork(2);
            mainwindow okno = new mainwindow(siec);
            siec.start();
        }
        else if(TESTMODE == 1) {
            /**
             * Test trwa koło 5 sekund, jest ich 6 więc nie ruszać myszką w tym czasie!!
             */
            for(int i = 0; i<6; ++i) {
                NeuronNetwork siec2 = new NeuronNetwork(1);
                mainwindow okno2 = new mainwindow(siec2);
                //okno2.setLoc(500,500);
                siec2.start();
                UnitTest testy2 = new UnitTest(okno2.getCurrentLocation(), okno2.getSizeX()*okno2.getScale(), okno2.getSizeY()*okno2.getScale());

                String nazwa2 = "scr_";
                String nazwa1 = "scr_";
                nazwa2 += Integer.toString(i) + "_0.jpg";
                nazwa1 += Integer.toString(i) + "_1.jpg";

                if(i>=MAX_TESTS-2)
                    testy2.drawQuadOneByOne();
                else {
                    double a = 70 + Math.random() * (250-10);
                    double b = 70 + Math.random() * (250-10);
                    double r = 60;
                    testy2.drawCircle(a, b, r);
                }
                waitt(2);
                testy2.screenshot(nazwa2);
                waitt(1);
                testy2.switchView();
                waitt(4);
                testy2.screenshot(nazwa1);
                okno2.closeWindow();
            }
        }
        else if(TESTMODE == 2) {
            NeuronNetwork siec = new NeuronNetwork(2);
            mainwindow okno = new mainwindow(siec);
            siec.start();
            UnitTest testy2 = new UnitTest(okno.getCurrentLocation(), okno.getSizeX()*okno.getScale(), okno.getSizeY()*okno.getScale());
            //testy2.drawSinus();
            //testy2.drawCircle(200, 200, 100);
            //testy2.drawQuadOneByOne();
            //testy2.drawPozioma();
            //testy2.drawStraightLine();
            testy2.drawDoubleCircle(150,150,70, 300,300, 50);
        }
    }

    private static void waitt(int seconds) {
        Date date = new Date();   // given date

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        while(true) {
            Date date2 = new Date();
            Calendar cal2 = GregorianCalendar.getInstance();
            cal2.setTime(date2);
            if(cal2.get(Calendar.SECOND) == calendar.get(Calendar.SECOND)+seconds )
                break;
        }
    }
}
