package main;

import java.util.ArrayList;

import agents.Predator;
import agents.PredatorRandom;
import agents.Prey;

public class MainMultiRandom extends Main {

	public static void main(String[] args) {
		int[] execTime = new int[100];
		int nRuns = 100;
		for (int i = 0; i < nRuns; i++) {
			ArrayList<Predator> predators = new ArrayList<Predator>();
			predators.add(new PredatorRandom(0, 10));
			predators.add(new PredatorRandom(10, 10));
			predators.add(new PredatorRandom(0, 0));
			predators.add(new PredatorRandom(10, 0));
			NewEnvironment env = new NewEnvironment(predators, new Prey(5,
					5));
			execTime[i] = env.run();

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
