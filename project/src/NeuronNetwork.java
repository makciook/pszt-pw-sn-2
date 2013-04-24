/**
 * Algorytm propagacji wstecznej - http://home.agh.edu.pl/~vlsi/AI/backp_t_en/backprop.html
 * TODO:
 */

import java.util.Random;

/**
 * Reprezentacja sieci neuronowej jako zbiór neuronów, pogrupowanych w warstwy - input, hidden oraz output.
 */
public class NeuronNetwork extends Thread {

    private Neuron[] inputLayer;
    private Neuron[][] hiddenLayer;
    private Neuron[] outputLayer;

    private double[] curTable;

    private int neurons;
    private int layers_number;

    private final double LEARN_RATIO = 0.9;
    private final int OUTPUT_NEURONS = 2;
    private final int NEURONS_NUM = 20;

    @Override
    public void run() {
        while(true) {
            applyBackPropagation(curTable);
            try {
                sleep(200);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NeuronNetwork(int lays_num) {
        layers_number = lays_num;
        neurons = NEURONS_NUM/lays_num;                        // ilość elementów w każdej z warstw ukrytych

        createInputLayer();
        createHiddenLayer(lays_num);
        createOutputLayer();
    }

    /**
     * Tworzenie warstwy wejściowej (2 neurony)
     */
    private void createInputLayer() {
        inputLayer = new Neuron[2];
        for(int i = 0; i < 2; ++i) {
            Neuron input = new Neuron();
            inputLayer[i] = input;
        }
    }

    /**
     * Tworzenie warstw ukrytych
     * @param lays_num ilość warstw ukrytych do utworzenia
     */
    private void createHiddenLayer(int lays_num) {
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
    }

    /**
     * Tworzenie warstwy wyjściowej
     */
    private void createOutputLayer() {
        Neuron[] prev_lay = hiddenLayer[layers_number-1];
        outputLayer = new Neuron[OUTPUT_NEURONS];                    // tworzenie warstwy wyjściowej
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            Neuron output = new Neuron();
            outputLayer[i] = output;
            output.addConnections(prev_lay);
        }

        // czytaj komentarz wyżej
        Random losuj = new Random();
        for(Neuron neuron : outputLayer) {
            Synaps[] cons = neuron.getConnections();
            for(Synaps con : cons) {
                con.setWeight(losuj.nextDouble()*2-1);
            }
        }
    }

    /**
     * Publiczna metoda uczenia sieci rozpoznawania punktu
     * @param x współrzędna x punktu na płaszczyźnie
     * @param y współrzędna y punktu na płaszczyźnie
     * @param expected wartość oczekiwana (nauczona)
     */
    public void learn(double x, double y, double expected[]) {
        setInput(x,y);            // ustawienie danych wejściowych
        calculateLayers();              // wykonanie obliczeń przez sieć

        System.out.println("x: " + x + " y " + y);
        System.out.println("Oczekiwane: " + expected[0] + " " + expected[1]);
        System.out.println("Wynik " + outputLayer[0].getValue() + " "  + outputLayer[1].getValue());

        applyBackPropagation(expected);  // zastosowanie propagacji wstecznej
    }

    private void setInput(double x, double y) {
        inputLayer[0].setValue(x);
        inputLayer[1].setValue(y);
    }

    /**
     * Przeprowadzenie obliczeń przez warstwy ukryte i wyjściową
     */
    private void calculateLayers() {
        // wykonanie obliczeń przez warstwy ukryte
        for(int i = 0; i < layers_number; ++i) {
            for(int j = 0; j < neurons; ++j) {
                hiddenLayer[i][j].calculateValue();
            }
        }

        // wykonanie obliczeń przez warstwę wyjściową
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            outputLayer[i].calculateValue();
        }
    }

    /**
     * Publiczna metoda obliczania wyników dla punktu x,y
     * @param x Współrzędna x punktu na płaszczyźnie
     * @param y Współrzędna y punktu na płaszczyźnie
     * @return Tablica wyników
     */
    public double[] calcResults(double x, double y) {
        setInput(x,y);
        calculateLayers();
        double result[] = new double[OUTPUT_NEURONS];
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            result[i] = outputLayer[i].getValue();      // zbieranie wyników z warstwy wyjściowej
        }
        return result;
    }

    /**
     * Algorytm wstecznej propagacji dla podanego parametru oczekiwanego
     * @param expected wartość parametru oczekiwanego
     */
    private void applyBackPropagation(double expected[]) {

        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            Neuron n = outputLayer[i];
            n.setDelta(expected[i] - n.getValue());
        }

        Neuron cur_layer[] = outputLayer;
        double delta = 0;
        for(int i = layers_number; i > 0; --i) {
            for(Neuron n : cur_layer) {
                Synaps connections[] = n.getConnections();
                for(Synaps con : connections) {
                    delta = con.getWeight()*n.getDelta();
                    Neuron prev = con.getPrevNeuron();
                    prev.setDelta(prev.getDelta() + delta);
                }
            }
            cur_layer = hiddenLayer[i-1];
        }

        cur_layer = hiddenLayer[0];
        double new_weight = 0;
        for(int i = 0; i < layers_number+1; ++i) {
            for(Neuron n : cur_layer) {
                Synaps connections[] = n.getConnections();
                for(Synaps con : connections) {
                    Neuron prev = con.getPrevNeuron();
                    double v = n.getValue();
                    new_weight = con.getWeight() + LEARN_RATIO*n.getDelta()*prev.getValue()*v*(1-v);
                    con.setWeight(new_weight);
                }
            }
            if(i >= layers_number-1)
                cur_layer = outputLayer;
            else
                cur_layer = hiddenLayer[i+1];
        }


    }



}
