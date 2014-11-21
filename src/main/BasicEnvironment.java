package main;
import java.util.ArrayList;
import java.util.Arrays;

import agents.Predator;
import agents.PredatorRandom;
import agents.Prey;

public class BasicEnvironment {
	
	public static final int WORLDSIZE 	= 11;
	
	ArrayList<Predator> predators = new ArrayList<Predator>();
	Prey prey;
	
	public ArrayList<Predator> getPredators() {
		return predators;
	}

	public Prey getPreys() {
		return prey;
	}

	// Constructur for one prey and one predator
	public BasicEnvironment(Predator predator_, Prey prey_) {
		this(new ArrayList<Predator>(Arrays.asList(predator_)), prey_);
	}
	
	// Constructor for multiple predators/preys
	public BasicEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		
		
		if (predators == null) {
			System.out.println("It's null");
			return;
		}
		
		predators.clear();
		prey = prey_;
		
		System.out.print(0);
		
		System.out.print(" ");
		prey.print();
		
		for (Predator predator : predators_) {
			predators.add(new Predator(predator));
			System.out.print(" ");
			predator.print();
		}
		
		System.out.println();
	}
	
	public int run() {
		int time = 0;
		while (!prey.isCaught()) {
			time ++;
			System.out.print(time);
			
			int dir = prey.planMoveRandom(predators);
			prey.move(dir);
			System.out.print(" ");
			prey.print();
			
			for (Predator predator : predators) {
				PredatorRandom predatorRandom = new PredatorRandom(predator);
				dir = predatorRandom.planMoveRandom();
				predator.move(dir, prey);
				System.out.print(" ");
				predator.print();
			}

			System.out.println();
		}
		
		return time;
	}

	public int getReward(Predator predator, Prey prey) {
		if (predator.equals(prey))
			return 10;
		return 0;
	}
	
	public int getReward(Predator predator, Prey prey, int aPredator, int aPrey) {
		Predator dummyPredator = new Predator(predator);
		Prey dummyPrey = new Prey(prey);
		
		dummyPredator.move(aPredator);
		dummyPrey.move(aPrey);
		
		return getReward(dummyPredator, dummyPrey);
	}

	protected void printValues(double[][][][] values, int px, int py) {
		System.out.println();

		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.printf("%.02f\t", values[x][y][px][py]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
