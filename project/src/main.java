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
    private static final boolean TESTMODE = true;

    public static void main(String args[]) {

        if(!TESTMODE) {
            NeuronNetwork siec = new NeuronNetwork(2);
            mainwindow okno = new mainwindow(siec);
            siec.start();
            //UnitTest testy = new UnitTest(okno.getCurrentLocation(), okno.getSizeX()*okno.getScale(), okno.getSizeY()*okno.getScale());
            //testy.drawStraightLine();
        }
        else {
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

                testy2.drawCircle();
                waitt(2);
                testy2.screenshot(nazwa2);
                waitt(1);
                testy2.switchView();
                waitt(2);
                testy2.screenshot(nazwa1);
                okno2.closeWindow();
            }
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
