# Introduction #

Project PSZT-PW-SN-2 was developed for PSZT @ Warsaw University of Technology. It's a fully-objective, easy to resize and extend implementation of Neural Networks in JAVA.

It's task is to recognize colored areas based on mouse-clicks on the panel.

## How to use ##

Run the program, and click multiple times with left button in an area. Now choose another area and click with right button. Press SPACE and see how the network recognized the area.

You can try all kind of shapes. If they are really complicated, press SPACE to show the division, and press "A" few times - which will force the network to recalculate values again and better adjust to given points.


---


# Implementation #

## Classes ##
  * DefaultNeuron - class representing a single neuron in the network, with abstract methods:
```
private double sigmoid(double x);
private double sigmOut(double x);
```
which have to be implemented on extension.

  * Synaps - a connection between two neurons. Has attributes of weight and delta.

  * NeuronNetwork - a network performing operations.

  * UnitTest - a Robot adapted to test our example Neuron Network.

## Algorithms ##

Back propagation algorithm was based on the one you can find on the [AGH website](http://home.agh.edu.pl/~vlsi/AI/backp_t_en/backprop.html). It needed slight modifications - Bias neuron at each hidden layer, different sigmoid function in the output layer and derivative in delta counting.

