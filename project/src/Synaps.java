/**
 * User: Daniel
 * Date: 27.03.13
 * Time: 19:49
 * TODO:
 */

/**
 * Reprezentacja synapsy poprzez parametry jak waga i delta oraz połączenie - wychodzące (next) i wchodzące (prev)
 */
public class Synaps {
    private Neuron prev;                // od tego neurona idzie połączenie
    private Neuron next;                // do tego
    private double weight = 0;          // waga połączenia
    private double delta = 0;              // propagacja wsteczna

    public Synaps(Neuron from, Neuron to) {
        prev = from;
        next = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double new_weight) {
        weight = new_weight;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double new_delta) {
        delta = new_delta;
    }

    public Neuron getPrevNeuron() {
        return prev;
    }

    public Neuron getNextNeuron() {
        return next;
    }
}
