import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

public class main {
    private static final int TESTMODE = 0;     // 0 - bez testow
                                               // 1 - testy kompleksowe

                                               // 2 - test sinusa
                                               // 3 - test koło
                                               // 4 - test krzywa
                                               // 5 - test pozioma
                                               // 6 - test prosta
                                               // 7 - test podwójne koło
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
            for(int i = 0; i<MAX_TESTS; ++i) {
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
        else if(TESTMODE >= 2) {
            NeuronNetwork siec = new NeuronNetwork(1);
            mainwindow okno = new mainwindow(siec);
            siec.start();
            UnitTest testy2 = new UnitTest(okno.getCurrentLocation(), okno.getSizeX()*okno.getScale(), okno.getSizeY()*okno.getScale());
            switch(TESTMODE) {
                case 2: testy2.drawSinus();
                        break;
                case 3: testy2.drawCircle(200, 200, 100);
                        break;
                case 4: testy2.drawQuadOneByOne();
                        break;
                case 5: testy2.drawPozioma();
                        break;
                case 6: testy2.drawStraightLine();
                        break;
                case 7: testy2.drawDoubleCircle(150,150,70, 300,300, 50);
                        break;
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
