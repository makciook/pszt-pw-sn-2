/**
 * Algorytm propagacji wstecznej - http://home.agh.edu.pl/~vlsi/AI/backp_t_en/backprop.html
 *
 *
 *   http://kunuk.wordpress.com/2010/10/11/neural-network-backpropagation-with-java/
 *
 */

import java.util.Random;
import java.util.Vector;

class Pair {
    private double x;
    private double y;
    public Pair(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){ return x; }
    public double getY(){ return y; }
    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }
}

/**
 * Reprezentacja sieci neuronowej jako zbioru neuronów, pogrupowanych w warstwy - input, hidden oraz output.
 */
public class NeuronNetwork extends Thread {

    private Neuron[] inputLayer;
    private Neuron[][] hiddenLayer;
    private Neuron[] outputLayer;

    private Vector<Pair> group1;
    private Vector<Pair> group2;

    private int neurons;
    private int layers_number;
    private boolean recalc = false;

    private final double LEARN_RATIO = 0.9;
    private final int OUTPUT_NEURONS = 2;
    private final int NEURONS_NUM = 20;
    private final int CREDITS = 500;
    private final float momentum = 0.7f;

    @Override
    public void run() {
        while(true) {
            if(recalc)
                recalcBase();
            else
                try {
                    sleep(200);
                } catch(Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public void addPoint(int group, double x, double y) {
        if(group == 1)
            group1.add(new Pair(x, y));
        else
            group2.add(new Pair(x, y));
        recalc = true;
    }

    public void recalcBase() {
        recalc = false;
        double exp[] = new double[2];
        int lim;
        if(group1.size() > group2.size())
            lim = group1.size();
        else
            lim = group2.size();
        System.out.println("Poszly koniec po betonie\n");
        for(int i = 0; i < CREDITS; ++i) {
            for(int j = 0; j < lim; ++j) {
                if(j < group1.size()) {
                    exp[0] = 0.1;
                    exp[1] = 0.9;
                    Pair p = group1.elementAt(j);
                    learn(p.getX(), p.getY(), exp);
                }
                if(j < group2.size()) {
                    exp[0] = 0.9;
                    exp[1] = 0.1;
                    Pair p = group2.elementAt(j);
                    learn(p.getX(), p.getY(), exp);
                }
            }
        }
        System.out.println("Oczekiwane: " + exp[0] + " " + exp[1]);
        System.out.println("Wynik " + outputLayer[0].getValue() + " "  + outputLayer[1].getValue());
    }

    public NeuronNetwork(int lays_num) {
        layers_number = lays_num;
        neurons = NEURONS_NUM/lays_num;                        // ilość elementów w każdej z warstw ukrytych
        group1 = new Vector<Pair>();
        group2 = new Vector<Pair>();

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
        //System.out.println(x + " " + y);
        setInput(x,y);            // ustawienie danych wejściowych
        calculateLayers();              // wykonanie obliczeń przez sieć

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
                hiddenLayer[i][j].calculateValue(false);
            }
        }

        // wykonanie obliczeń przez warstwę wyjściową
        for(int i = 0; i < OUTPUT_NEURONS; ++i) {
            outputLayer[i].calculateValue(true);
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

        /*int i = 0;
        for (Neuron n : outputLayer) {
            Synaps connections[] = n.getConnections();
            for (Synaps con : connections) {
                double ak = n.getValue();
                double ai = con.getPrevNeuron().getValue();
                double desiredOutput = expected[i];

                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -LEARN_RATIO * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDelta(deltaWeight);
                con.setWeight(newWeight + momentum * con.getDelta());
            }
            i++;
        }

        // update weights for the hidden layer
        for (Neuron n : hiddenLayer[0]) {
            Synaps connections[] = n.getConnections();
            for (Synaps con : connections) {
                double aj = n.getValue();
                double ai = con.getPrevNeuron().getValue();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.getId()).getWeight();
                    double desiredOutput = (double) expected[j];
                    double ak = out_neu.getValue();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }

                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -LEARN_RATIO * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDelta(deltaWeight);
                con.setWeight(newWeight + momentum* con.getDelta());
            }
        }     */

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
