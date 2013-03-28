/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 27.03.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class main {
    public static void main(String args[]) {

        NeuronNetwork siec = new NeuronNetwork(1);

        mainwindow okno = new mainwindow(siec);

        double asd[] = new double[1];

        asd[0] = 0.5;
        siec.learn(0.35,0.9,asd);
    }
}
