package main;

import java.util.ArrayList;

import agents.Predator;
import agents.PredatorRandom;
import agents.Prey;

public class MainMultiQlearning extends Main{
	public static void main(String[] args) {
		int[] execTime = new int[100];
		int nRuns = 1;
		for (int i = 0; i < nRuns; i++) {
			ArrayList<Predator> predators = new ArrayList<Predator>();
			predators.add(new Predator(0, 10));
			predators.add(new Predator(10, 10));
//			predators.add(new Predator(0, 0));
//			predators.add(new Predator(10, 0));
			MultiQlearningEnvironment env = new MultiQlearningEnvironment(predators, new Prey(5,
					5));
			double[][] parameterSettings = {{0.5,0.9,0.1,15}};
			execTime[i] = env.run(parameterSettings);

			// For garbage cleaning the object
			env = null;
		}
		// Print times
		for (int j = 0; j < nRuns; j++) {
			System.out.println((j + 1) + "&" + execTime[j] + "\\\\");
		}

		// Calculate average
		double average = calcAverage(execTime);
		double stdDev = calcStdDev(execTime, nRuns);

		System.out.println("Average&" + average + "\\\\");
		System.out.println("Standard deviation&" + stdDev + "\\\\");
	}
}
