package main;
import agents.PredatorRandom;
import agents.Prey;


public class Main {

	public static double calcAverage (int[] array)
	{
		double sum = 0;
		// Sum
		for (int i = 0; i < array.length; i++)
		{
			sum += array[i];
		}
		// Divide by number of items
		return sum / array.length;
	}
	
	public static double calcStdDev (int[] array, double average)
	{
		int sum = 0;
		// Sum
		for (int i = 0; i < array.length; i++)
		{
			sum += Math.pow(Math.abs(array[i]-average), 2.0);
		}
		// Divide by number of items and take square
		return Math.sqrt(sum / array.length);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] execTime = new int[100];
		int nRuns = 100;
		for (int i = 0; i < nRuns; i++)
		{
			BasicEnvironment env = new BasicEnvironment(new PredatorRandom(0, 0), new Prey(5, 5));
			execTime[i] = env.run();
			
			// For garbage cleaning the object
			env = null;
		}
		
		// Print times
		for (int j = 0; j < nRuns; j++)
		{
			System.out.println((j+1) + "&" + execTime[j] +  "\\\\");
		}
		
		// Calculate average
		double average = calcAverage(execTime);
		double stdDev = calcStdDev(execTime,nRuns);
		
		System.out.println("Average&" + average + "\\\\");
		System.out.println("Standard deviation&" + stdDev + "\\\\");
	}

}
