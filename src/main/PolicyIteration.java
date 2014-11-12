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
		while (preys.size() > 0) {
			time ++;
			System.out.print(time);
			
			for (Prey prey : preys) {
				int dir = prey.planMoveRandom(predators);
				prey.move(dir);
				System.out.print(" ");
				prey.print();
			}
			
			for (Predator predator : predators) {
				PredatorPI predatorPI = new PredatorPI(predator);
				int[][] policy = new int[11][11];
				policy = predatorPI.evaluatePolicy(policy, preys);
				printPolicy(policy);
				int dir = predatorPI.planMovePI(policy);
				predator.move(dir, preys);
				System.out.print(" ");
				predator.print();
			}

			System.out.println();
			System.out.println(time);
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
