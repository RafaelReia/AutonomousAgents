package main;
import agents.PredatorRandom;
import agents.Prey;


public class Main {

	public static long calcAverage (long[] array)
	{
		long sum = 0;
		// Sum
		for (int i = 0; i < array.length; i++)
		{
			sum += array[i];
		}
		// Divide by number of items
		return sum / array.length;
	}
	
	public static long calcStdDev (long[] array, long average)
	{
		long sum = 0;
		// Sum
		for (int i = 0; i < array.length; i++)
		{
			sum += Math.abs(array[i]-average)^2;
		}
		// Divide by number of items and take square
		return (long) Math.sqrt(sum / array.length);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long[] execTime = new long[100];
		int nRuns = 100;
		for (int i = 0; i < nRuns; i++)
		{
			long s0 = System.nanoTime();
			BasicEnvironment env = new BasicEnvironment(new PredatorRandom(0, 0), new Prey(5, 5));
			env.run();
			long s1 = System.nanoTime();
			execTime[i] = s1 - s0;
			
			// For garbage cleaning the object
			env = null;
		}
		
		// Print times
		for (int j = 0; j < nRuns; j++)
		{
			System.out.println((j+1) + "&" + execTime[j] +  "\\\\");
		}
		
		// Calculate average
		long average = calcAverage(execTime);
		long stdDev = calcStdDev(execTime,nRuns);
		
		System.out.println("Average&" + average + "\\\\");
		System.out.println("Standard deviation&" + stdDev + "\\\\");
	}

}
