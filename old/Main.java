package pszt;

public class Main {
	/*public static void main(String[] args)
	{
		// Hidden layer.
		double[][] h = { {1, 1}, {1, 2}, {2, 1} };
		// Output layer.
		double[][] o = { {2, 1, 3, 0}, {2, 2, 1, 1} };
		// Initializing the perceptron with those two layers. 
		Perceptron p = new Perceptron(h, o);
		// Input - from the sample.
		double[] x = new double[20];
		// Expected output - from the sample.
		double[][] e = new double[20][2];
		// Output - from the net.
		double[][] y = new double[20][2];
		// Initializing the expected output. Should be replaced by values from the sample. 
		for (int i = 0; i < e.length; i++){
			e[i][0] = Math.random();
			e[i][1] = Math.random();
		}
		// Calculating the net's output.
		for (int i = 0; i < x.length; i++)
		{	
			// Initializing the input. Should be replaced by values from the sample.
			// For example with scaling:
			//x[i] = scaling(0.0,20.0,-1.0,1.0,i);
			x[i] = i;
			
			// Calculating the output.
			y[i] = p.calculate(x[i]);
			// For example with scaling:
			//for (int j = 0; j < y[i].length; j++){
			//	y[i][j] = scaling(-10.0,10.0,-1.4,1.4,y[i][j]);
			//}
			// Computing the error on the base of previous output.
			
			//p.learn(e[i]);
		}
		// Results.
		for (int i = 0; i < y.length; i++)
			System.out.println(y[i][0] + "; " + y[i][1]);
	}*/

	/*
	 * Scaling the number x which belongs to [a,b] to the number which belongs to [c,d]. 
	 */
	static private double scaling(double a, double b, double c, double d, double x){
		return (x - a) * (d - c ) / ( b - a ) + c;
	}
	
}
