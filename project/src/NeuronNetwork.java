/**
 *
 * TODO:
 */

import java.util.Vector;
import java.util.Random;


public class NeuronNetwork {

    private Vector<Neuron> inputLayer;
    private Vector<Neuron> hiddenLayer[];
    private Vector<Neuron> outputLayer;

    private final int NEURONS_NUMS = 20;
    private int neurons = 0;
    private int layers_number;
    private final double LEARN_RATIO = 1;

    public NeuronNetwork(int lays_num) {
        layers_number = lays_num;
        neurons = NEURONS_NUMS/lays_num;                        // ilość elementów w każdej z warstw ukrytych
        inputLayer = new Vector<Neuron>(2);                     // tworzenie warstwy wejściowej
        for(int i = 0; i < 2; ++i) {
            Neuron input = new Neuron();
            inputLayer.add(input);
        }

        hiddenLayer = new Vector[lays_num];                     // tworzenie lays_num warstw ukrytych
        Vector<Neuron> prev_lay = inputLayer;
        for(int i = 0; i < lays_num; ++i) {
            for(int j = 0; j < neurons; ++j) {
                Neuron hidden = new Neuron();
                hiddenLayer[j].add(hidden);
                hidden.addConnections(prev_lay);                // tworzymy połączenia z neuronami w poprzedniej warstwie
            }
            prev_lay = hiddenLayer[i];
        }

        outputLayer = new Vector<Neuron>(2);                    // tworzenie warstwy wyjściowej
        for(int i = 0; i < 2; ++i) {
            Neuron output = new Neuron();
            outputLayer.add(output);
            output.addConnections(prev_lay);
        }

        // inicjalizacja wag losowymi wartościami
        Random losuj = new Random();
        for(int i = 0; i < lays_num; ++i) {
            for(Neuron neuron : hiddenLayer[i]) {
                Vector<Synaps> cons = neuron.getConnections();
                for(Synaps con : cons) {
                    con.setWeight(losuj.nextDouble());
                }
            }
        }

        // czytaj komentarz wyżej
        for(Neuron neuron : outputLayer) {
            Vector<Synaps> cons = neuron.getConnections();
            for(Synaps con : cons) {
                con.setWeight(losuj.nextDouble());
            }
        }


    }

    public void learn(int x, int y, int expected[]) {
        setInput(x,y);                                          // ustarwienie danych wejściowych
        calculate();                                            // wykonanie obliczeń przez sieć

        applyBackpropagation(expected);
    }

    private void setInput(int x, int y) {
        inputLayer.elementAt(0).setValue(x);
        inputLayer.elementAt(1).setValue(y);
    }

    private void calculate() {

        // wykonanie obliczeń przez warstwy ukryte
        for(int i = 0; i < layers_number; ++i) {
            for(int j = 0; j < neurons; ++j) {
                hiddenLayer[i].elementAt(j).calculateValue();
            }
        }

        // wykonanie obliczeń przez warstwę wyjściową
        for(int i = 0; i < 2; ++i) {
            outputLayer.elementAt(i).calculateValue();
        }
    }

    private void applyBackpropagation(int expected[]) {

        double out1 = outputLayer.elementAt(0).getValue();
        double out2 = outputLayer.elementAt(1).getValue();
        double deltaa[] = new double[2];
        deltaa[0] = out1*(1-out1)*(expected[0]-out1);
        deltaa[1] = out2*(1-out2)*(expected[1]-out2);
        outputLayer.elementAt(0).setDelta(deltaa[0]);
        outputLayer.elementAt(1).setDelta(deltaa[1]);

        for(Neuron neuron : outputLayer) {
            for(Synaps con : neuron.getConnections()) {
                Neuron prev = con.getPrevNeuron();
                double weight = con.getWeight() + LEARN_RATIO*neuron.getDelta()*prev.getValue();
                con.setWeight(weight);
                double delta;
                delta = prev.getValue()*(1-prev.getValue())*neuron.getDelta()*con.getWeight();
                prev.setDelta(prev.getDelta() + delta);
            }
        }

        for(int i = layers_number-1; i >= 0; --i) {
            for(Neuron neuron : hiddenLayer[i]) {
                for(Synaps con : neuron.getConnections()) {
                    double delta;
                    Neuron prev = con.getPrevNeuron();
                    delta = prev.getValue()*(1-prev.getValue())*neuron.getDelta()*con.getWeight();
                    prev.setDelta(prev.getDelta() + delta);
                }
            }
        }

        for(int i = 0; i < layers_number; ++i) {
            for(Neuron neuron : hiddenLayer[i]) {
                for(Synaps con : neuron.getConnections()) {
                    double weight;
                    Neuron prev = con.getPrevNeuron();
                    weight = con.getWeight() + LEARN_RATIO*prev.getDelta()*prev.getValue();
                    con.setWeight(weight);
                }
            }
        }


    }

}
