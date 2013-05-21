import com.sun.corba.se.impl.logging.ORBUtilSystemException;

import java.util.Vector;

/**
 * Reprezentacja neuronu poprzez należące do niego synapsy
 */
public class Neuron {

    private Synaps[] connections;             // synapsy wchodzące do neuronu
    private double value = 0;
    private double delta = 0;
    private static int counter = 0;
    private int id;

    public Neuron() {
        ++counter;
        id = counter;
    }

    /**
     *  Funkcja obliczająca wartość w neuronie
     * @param outputLayer - true jeśli neuron jes w warstwie wyjściowej
     *                    Zastosowane dla użycia innej funkcji aktywującej
     */
    public void calculateValue(boolean outputLayer) {
        double s = 0;
        for(Synaps con : connections) {
            double waga = con.getWeight();
            Neuron input = con.getPrevNeuron();
            double v = input.getValue();
            s += v*waga;
        }

        value = (outputLayer == true) ? sigmoOut(s) : sigmoid(s);
        delta = 0;
    }

    public int getId() {
        return id;
    }

    public void addConnections(Neuron[] inputs) {
        connections = new Synaps[inputs.length]; // + neuron stala jedynka
        int i = 0;
        for(Neuron in : inputs) {
            connections[i] = new Synaps(in, this);
            ++i;
        }
    }
    // dodaje polaczenia i podlacza neuron generujacy stala jedyne na wejsciu.
    public void addConnections(Neuron[] inputs, Neuron constOne) {
        connections = new Synaps[inputs.length+1]; // + neuron stala jedynka
        int i = 0;
        for(Neuron in : inputs) {
            connections[i] = new Synaps(in, this);
            ++i;
        }
        connections[i] = new Synaps(constOne, this);
    }

    public Synaps[] getConnections() {
        return connections;
    }

    /**
     * Funkcja aktywacji neuronów w warstwach ukrytych
     * @param x
     * @return
     */
    private double sigmoid(double x) {
        return 1.0 / (1.0 +  (Math.exp(-x)));
    }

    /**
     * Funkcja aktywacji dla neuronów w warstwie zewnętrznej
     * @param x
     * @return
     */
    private double sigmoOut(double x) {
        return x;
    }

    public void setValue(double output) {
        value = output;
    }

    public double getValue() {
        return value;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double d) {
        delta = d;
    }
}
