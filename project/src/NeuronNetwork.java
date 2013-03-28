/**
 *
 * TODO:
 */

import java.util.Vector;
import java.util.Random;


public class NeuronNetwork {

    private Neuron[] inputLayer;
    private Neuron[][] hiddenLayer;
    private Neuron[] outputLayer;


    private int neurons;
    private int layers_number;

    private final double LEARN_RATIO = 0.9;
    private final int OUTPUT_NEURONS = 1;
    private final int NEURONS_NUM = 4;

    public NeuronNetwork(int lays_num) {
        layers_number = lays_num;
        neurons = NEURONS_NUM/lays_num;                        // ilość elementów w każdej z warstw ukrytych
        inputLayer = new Neuron[2];                     // tworzenie warstwy wejściowej
        for(int i = 0; i < 2; ++i) {
            Neuron input = new Neuron();
            inputLayer[i] = input;
        }

        hiddenLayer =  new Neuron[lays_num][neurons];                     // tworzenie lays_num warstw ukrytych
        Neuron[] prev_lay = inputLayer;
        for(int i = 0; i < lays_num; ++i) {
            for(int j = 0; j < neurons; ++j) {
                Neuron hidden = new Neuron();
                hiddenLayer[i][j] = hidden;
                hidden.addConnections(prev_lay);                // tworzymy połączenia z neuronami w poprzedniej warstwie
            }
            prev_lay = hiddenLayer[i];
        }

        outputLayer = new Neuron[OUTPUT_NEURONS];                    // tworzenie warstwy wyjściowej
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            Neuron output = new Neuron();
            outputLayer[i] = output;
            output.addConnections(prev_lay);
        }

        // inicjalizacja wag losowymi wartościami
        Random losuj = new Random();
        for(int i = 0; i < lays_num; ++i) {
            for(Neuron neuron : hiddenLayer[i]) {
                Synaps[] cons = neuron.getConnections();
                for(Synaps con : cons) {
                    con.setWeight(losuj.nextDouble()*2-1);
                }
            }
        }

        // czytaj komentarz wyżej
        for(Neuron neuron : outputLayer) {
            Synaps[] cons = neuron.getConnections();
            for(Synaps con : cons) {
                con.setWeight(losuj.nextDouble()*2-1);
            }
        }


    }

    public void learn(double x, double y, double expected[]) {
        setInput(x,y);                                          // ustarwienie danych wejściowych
        calculate();                                            // wykonanie obliczeń przez sieć

        //System.out.println("x: " + x + " y " + y);
        //System.out.println("Oczekiwane: " + expected[0] + " " + expected[1]);
        //System.out.println("Wynik " + outputLayer[0].getValue() + " "  + outputLayer[1].getValue());

        applyBackpropagation(expected);

        /*for(Neuron neuron : hiddenLayer[0]) {
            for(Synaps cos : neuron.getConnections()) {
                System.out.print("Neuron " + neuron.getId() + " con weight " + cos.getWeight());
            }
            System.out.println();
        }

        for(Neuron neuron : outputLayer) {
            for(Synaps cos : neuron.getConnections()) {
                System.out.print("Neuron " + neuron.getId() + " con weight " + cos.getWeight());
            }
            System.out.println();
        }*/
    }

    private void setInput(double x, double y) {
        inputLayer[0].setValue(x);
        inputLayer[1].setValue(y);
    }

    private void calculate() {

        // wykonanie obliczeń przez warstwy ukryte
        for(int i = 0; i < layers_number; ++i) {
            for(int j = 0; j < neurons; ++j) {
                hiddenLayer[i][j].calculateValue();
                //System.out.println("Neuron " + hiddenLayer[i][j].getId() + " " + hiddenLayer[i][j].getValue());
            }
        }

        // wykonanie obliczeń przez warstwę wyjściową
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            outputLayer[i].calculateValue();
        }
    }

    public double calc(double x, double y) {
        setInput(x,y);
        calculate();
        return outputLayer[0].getValue();
    }

    private void applyBackpropagation(double expected[]) {

        /*calcOutputLayerError(expected);
        changeOutputLayerWeights();
        calcHiddenLayersError();
        changeHiddenLayersWeights();*/
    }

    private void calcOutputLayerError(double expected[]) {
        double out;
        double delta;
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            out = outputLayer[i].getValue();
            delta= out*(1-out)*(expected[i]-out);
            outputLayer[0].setDelta(delta);
        }
        //System.out.println("Bledy wyjsciowe: " + deltaa[0] + " " + deltaa[1]);
    }

    private void changeOutputLayerWeights() {
        for(Neuron neuron : outputLayer) {
            for(Synaps con : neuron.getConnections()) {
                Neuron prev = con.getPrevNeuron();
                double weight = con.getWeight() + LEARN_RATIO*neuron.getDelta()*prev.getValue();
                con.setWeight(weight);
                //System.out.println("Waga wyjsciowa: " + weight);
                double delta;
                delta = prev.getValue()*(1-prev.getValue())*neuron.getDelta()*con.getWeight();
                prev.setDelta(prev.getDelta() + delta);
                //System.out.println("Delta neuronu " + prev.getId() + ": " + prev.getDelta());
            }
        }
    }

    private void calcHiddenLayersError() {
        for(int i = layers_number-1; i >= 0; --i) {
            for(Neuron neuron : hiddenLayer[i]) {
                for(Synaps con : neuron.getConnections()) {
                    double delta;
                    Neuron prev = con.getPrevNeuron();
                    delta = prev.getValue()*(1-prev.getValue())*neuron.getDelta()*con.getWeight();
                    prev.setDelta(prev.getDelta() + delta);
                    //System.out.println("Delta neuronu " + prev.getId() + ": " + prev.getDelta());
                }
            }
        }
    }

    private void changeHiddenLayersWeights() {
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
