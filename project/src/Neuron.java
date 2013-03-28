/**
 * User: Daniel
 * Date: 27.03.13
 * Time: 19:28
 */

import java.util.Vector;


public class Neuron {

    private Vector<Synaps> connections;             // synapsy wchodzące do neuronu
    private double value = 0;
    private double delta = 0;

    public Neuron() {
        connections = new Vector<Synaps>();
    }

    public void calculateValue() {
        double s = 0;
        for(Synaps con : connections) {
            double w = con.getWeight();
            Neuron input = con.getPrevNeuron();
            double v = input.getValue();

            s += v*w;
        }

        value = f(s);
        delta = 0;
    }

    public void addConnection(Synaps input) {
        connections.add(input);
    }

    public void addConnections(Vector<Neuron> inputs) {
        for(Neuron in : inputs) {
            connections.add(new Synaps(in, this));
        }
    }

    public Vector<Synaps> getConnections() {
        return connections;
    }

    private double f(double s) {
        return sigmoid(s);
    }

    private double sigmoid(double x) {
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
