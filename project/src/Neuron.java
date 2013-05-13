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

    //public void calculateValue() {
    public void calculateValue(boolean outputLayer) {
        double s = 0;
        for(Synaps con : connections) {
            double waga = con.getWeight();
          //  if(waga > 600 || waga < -600)
            //    System.out.println("alert" + waga);
            Neuron input = con.getPrevNeuron();
            double v = input.getValue();
            s += v*waga;
        }

        value = sigmoid(s);
        delta = 0;
    }

    public int getId() {
        return id;
    }

    /*public void addConnection(Synaps input) {
        connections.add(input);
    }*/

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

    public Synaps getConnection(int i) {
        for(Synaps s : connections) {
            if(s.getPrevNeuron().getId()==i)
                return s;
        }
        return null;
    }

    private double func(double s) {
        return sigmoid(s);
    }

    private double sigmoid(double x) {
     //   System.out.println("Value"+x);
        return 1.0 / (1.0 +  (Math.exp(-x)));

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
