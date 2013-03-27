/**
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

    public NeuronNetwork(int lays_num) {
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

        for(Neuron neuron : outputLayer) {
            Vector<Synaps> cons = neuron.getConnections();
            for(Synaps con : cons) {
                con.setWeight(losuj.nextDouble());
            }
        }




    }

}
