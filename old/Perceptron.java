package pszt;

public class Perceptron {
	private final int inputLayerNeurons;
	private final int hiddenLayerNeurons;
	private final int outputLayerNeurons;
	private double[][] hiddenLayerWeights;
	private double[][] outputLayerWeights;
	
	private final double basicRate = 0.1;
	private double learningRate;
	private double[] errorOutput;
	private double[] errorHidden;
	private double[] hiddenLayerCurrentValues;
	private double[] hiddenLayerOutputCurrentValues;
	private double[] outputLayerCurrentValues;
	
	public Perceptron (int inputN, int hiddenLayerN, int outputLayerN, boolean initialize)
	{
		inputLayerNeurons = inputN;
		hiddenLayerNeurons = hiddenLayerN;
		outputLayerNeurons = outputLayerN;
		if (inputN > 0 && hiddenLayerN > 0 && outputLayerN > 0)
		{
			hiddenLayerWeights = new double[hiddenLayerNeurons][inputLayerNeurons+1];
			outputLayerWeights = new double[outputLayerNeurons][hiddenLayerNeurons+1];
			hiddenLayerCurrentValues = new double[hiddenLayerNeurons];
			hiddenLayerOutputCurrentValues = new double[hiddenLayerNeurons];
			outputLayerCurrentValues = new double[outputLayerNeurons];
			errorOutput = new double[outputLayerNeurons];
			errorHidden = new double[hiddenLayerNeurons];
		}
		if (initialize)
		{
			for (int i = 0; i < hiddenLayerNeurons; i++)
				for (int j = 0; j <= inputLayerNeurons; j++)
					hiddenLayerWeights[i][j] = 10*Math.random()-5;

			for (int i = 0; i < outputLayerNeurons; i++)
				for (int j = 0; j <= hiddenLayerNeurons; j++)
					outputLayerWeights[i][j] = 2*Math.random()-1;
		}
	}
	
	public Perceptron (double[][] hiddenWeights, double[][] outputWeights)
	{
		inputLayerNeurons = hiddenWeights[0].length-1;
		hiddenLayerNeurons = hiddenWeights.length;
		outputLayerNeurons = outputWeights.length;
		boolean ok = true;
		for (int i = 0; i < hiddenLayerNeurons; i++)
		{
			if (hiddenWeights[i].length != inputLayerNeurons+1)
				ok = false;
		}
		for (int i = 0; i < outputLayerNeurons; i++)
		{
			if (outputWeights[i].length != hiddenLayerNeurons+1)
				ok = false;
		}
		if (ok)
		{
			hiddenLayerWeights = hiddenWeights.clone();
			outputLayerWeights = outputWeights.clone();
			hiddenLayerCurrentValues = new double[hiddenLayerNeurons];
			hiddenLayerOutputCurrentValues = new double[hiddenLayerNeurons];
			outputLayerCurrentValues = new double[outputLayerNeurons];
			errorOutput = new double[outputLayerNeurons];
			errorHidden = new double[hiddenLayerNeurons];
		}
	}
	
	public double[] calcall(double ... args)
	{
		double[] retval = new double[hiddenLayerNeurons+outputLayerNeurons];
		calculate(args);
		for (int i = 0; i < retval.length; i++)
		{
			if (i < outputLayerNeurons)
				retval[i] = outputLayerCurrentValues[i];
			else
				retval[i] = hiddenLayerOutputCurrentValues[i-outputLayerNeurons];
		}
		return retval;
	}
	
	public double[] calculate(double ... args)
	{
		if (args.length == inputLayerNeurons)
		{
			//double[] hiddenLayerOutput = new double[hiddenLayerNeurons];
			//double[] outputLayerOutput = new double[outputLayerNeurons];
			
			
			for (int i = 0; i < hiddenLayerNeurons; i++)
			{
				hiddenLayerCurrentValues[i] = 0;
				for (int j = 0; j < inputLayerNeurons; j++)
				{
					hiddenLayerCurrentValues[i] += args[j]*hiddenLayerWeights[i][j];
				}
				hiddenLayerCurrentValues[i] += hiddenLayerWeights[i][inputLayerNeurons];
				hiddenLayerOutputCurrentValues[i] = sigma(hiddenLayerCurrentValues[i]);
			}
			//hiddenLayerOutputCurrentValues = hiddenLayerOutput;
			
			for (int i = 0; i < outputLayerNeurons; i++)
			{
				outputLayerCurrentValues[i] = 0;
				for (int j = 0; j < hiddenLayerNeurons; j++)
				{
					outputLayerCurrentValues[i] += hiddenLayerOutputCurrentValues[j]*outputLayerWeights[i][j];
				}
				outputLayerCurrentValues[i] += outputLayerWeights[i][hiddenLayerNeurons];
			}
			//outputLayerCurrentValues = outputLayerOutput;
			return outputLayerCurrentValues.clone();
		}
		return null;
	}
	
	private double sigma(double x)
	{
		return 2/Math.PI*Math.atan(x);
	}
	
	private double sigmaprim(double x)
	{
		return 2/Math.PI/(1+x*x);
	}
	
	public void learn(double[] args, double[] results, int n)
	{
		learningRate = basicRate/n;
		calculate(args);
		computeOutputError(results);
		computeHiddenError();
		changeOutputWeights();
		changeHiddenWeights(args);
		//changeOutputWeights(1,hiddenLayerNeurons,inputLayerNeurons);
	}
	
	/**
	 * Computing the error between the current output of the net and the expected output from the samples.
	 * @param results - expected output from the samples
	 * @return array of errors
	 */
	public void computeOutputError(double[] results){
		// Checking whether the argument dimensions fit.
		if (results.length == outputLayerNeurons){
			for (int i = 0; i < outputLayerNeurons; i++){
				// Counting the error.
				errorOutput[i] = outputLayerCurrentValues[i] - results[i];
			}
		}
		//System.out.print(errorOutput[0]); System.out.print(" ");
	}
	
	/**
	 * Computing the error for the hidden layer. We do not need args because we don't know the
	 * target values.
	 */
	private void computeHiddenError(){
		// Taking each neuron of hidden layer.
		for (int i = 0; i < hiddenLayerNeurons; i++){
			// Initializing on 0.
			errorHidden[i] = 0;
			// Learning about an error from all the output layer neurons.
			for (int j = 0; j < outputLayerNeurons; j++){
				// Updating.
				errorHidden[i] += errorOutput[j] * outputLayerWeights[j][i];
			}
			errorHidden[i] *= sigmaprim(hiddenLayerCurrentValues[i]);
		}
		//System.out.println(errorHidden[0]);
	}
	
	public void changeOutputWeights()
	{
		for (int i = 0; i < outputLayerNeurons; i++)
		{
			for (int j = 0; j < hiddenLayerNeurons; j++)
			{
				outputLayerWeights[i][j] -= learningRate*errorOutput[i]*hiddenLayerOutputCurrentValues[j];
			}
			outputLayerWeights[i][hiddenLayerNeurons] -= learningRate*errorOutput[i];
		}
		System.out.print(outputLayerWeights[0][0]); System.out.print(" ");
		System.out.print(outputLayerWeights[0][hiddenLayerNeurons]); System.out.print(" ");
	}
	
	public void changeHiddenWeights(double[] args)
	{
		for (int i = 0; i < hiddenLayerNeurons; i++)
		{
			for (int j = 0; j < inputLayerNeurons; j++)
			{
				hiddenLayerWeights[i][j] -= learningRate*errorHidden[i]*args[j];
			}
			hiddenLayerWeights[i][inputLayerNeurons] -= learningRate*errorHidden[i];
		}
		System.out.print(hiddenLayerWeights[0][0]); System.out.print(" ");
		System.out.println(hiddenLayerWeights[0][inputLayerNeurons]);
	}
	
	/**
	 * Changing the weights in the output layer.
	 * Like in the equation here:
	 * http://www4.rgu.ac.uk/files/chapter3%20-%20bp.pdf
	 * Page 18 Point 2
	 * 
	 * rightLayerN - the later layar
	 * leftLayerN - the previous layar
	 * layerN - 0 output, 1 hidden
	 */
	private void changeOutputWeights(int layerN, int rightLayerN, int leftLayerN){
		// Taking each output layer neuron.
		for (int i = 0; i < rightLayerN; i++){
			// And updating each weight of it.
			for (int j = 0; j < leftLayerN; j++){
				// Update with error for this neuron and output from connecting neuron - j.
				if (layerN == 0){
					outputLayerWeights[i][j] += errorOutput[i] * hiddenLayerOutputCurrentValues[j];
				}
				else{
					hiddenLayerWeights[i][j] += errorHidden[i] * hiddenLayerOutputCurrentValues[j];
				}
			}
			// For the input = 1.
			if (layerN == 0){
				outputLayerWeights[i][hiddenLayerNeurons] += errorOutput[i];
			}
			else{
				hiddenLayerWeights[i][inputLayerNeurons] += errorHidden[i];
			}
		}
	}
	
}
