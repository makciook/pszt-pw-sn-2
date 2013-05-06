/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 27.03.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class main {
    public static void main(String args[]) {

        NeuronNetwork siec = new NeuronNetwork(2);
        mainwindow okno = new mainwindow(siec);
        siec.start();
        okno.setLoc(500,500);
        UnitTest testy = new UnitTest(okno.getCurrentLocation(), okno.getSizeX()*okno.getScale(), okno.getSizeY()*okno.getScale());
        testy.drawRandomLine();

        NeuronNetwork siec2 = new NeuronNetwork(2);
        mainwindow okno2 = new mainwindow(siec2);
        siec2.start();
        UnitTest testy2 = new UnitTest(okno2.getCurrentLocation(), okno2.getSizeX()*okno.getScale(), okno2.getSizeY()*okno2.getScale());
        testy2.drawRandomLine();

    }
}
