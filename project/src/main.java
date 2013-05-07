/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 27.03.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class main {
    public static void main(String args[]) {

       /* NeuronNetwork siec = new NeuronNetwork(2);
        mainwindow okno = new mainwindow(siec);
        siec.start();
        UnitTest testy = new UnitTest(okno.getCurrentLocation(), okno.getSizeX()*okno.getScale(), okno.getSizeY()*okno.getScale());
        testy.drawStraightLine(); */

        NeuronNetwork siec2 = new NeuronNetwork(1);
        mainwindow okno2 = new mainwindow(siec2);
        //okno2.setLoc(500,500);
        siec2.start();
        //UnitTest testy2 = new UnitTest(okno2.getCurrentLocation(), okno2.getSizeX()*okno2.getScale(), okno2.getSizeY()*okno2.getScale());
        //testy2.drawPozioma();
       //testy2.drawQuadOneByOne();
    }
}
