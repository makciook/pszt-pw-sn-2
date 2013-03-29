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

        /*double asd[] = new double[1];

         for(int i = 0; i < 15000; ++i) {
            asd[0] = 0;
            siec.learn(1.0,1.0,asd);
            asd[0] = 1;
            siec.learn(1.0,0.0,asd);
            asd[0] = 1;
            siec.learn(0.0,1.0,asd);
            asd[0] = 0;
            siec.learn(0.0,0.0,asd);
         }

        System.out.println("Wynik: " + siec.calc(1.0, 0.0));*/
    }
}
