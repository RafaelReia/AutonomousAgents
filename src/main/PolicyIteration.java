package main;
import static main.BasicEnvironment.WORLDSIZE;

import java.util.ArrayList;

import agents.Predator;
import agents.PredatorPE;
import agents.PredatorPI;
import agents.Prey;

public class PolicyIteration extends BasicEnvironment{
	
	// Constructur for one prey and one predator
	public PolicyIteration(Predator predator_, Prey prey_)
	{
		super(predator_, prey_);
	}
	
	// Constructor for multiple predators/preys
	public PolicyIteration(ArrayList<Predator> predators_, ArrayList<Prey> preys_)
	{
		super(predators_, preys_);
	}
	
	public int run() {
		int time = 0;
		
		for (Predator predator : predators) {
			PredatorPI predatorPI = new PredatorPI(predator);
			int[][][][] policies = new int[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
			double[][][][] values = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
			predatorPI.evaluatePolicy(policies, preys.get(0),this);
			policies = predatorPI.improvePolicy(policies, values, preys.get(0),this);
		}
		
		return time;
	}
	private void printPolicy(int[][] policy) {
		System.out.println();
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				switch (policy[x][y]) {
				case 0:
					System.out.print(0 + " ");
					break;
				case 1:
					System.out.print("^ ");
					break;
				case 2:
					System.out.print("v ");
					break;
				case 3:
					System.out.print("> ");
					break;
				case 4:
					System.out.print("< ");
					break;
				}
				
			}
			System.out.println();
		}
	}


}
