package main;

import static main.BasicEnvironment.WORLDSIZE;

import java.util.ArrayList;

import agents.Predator;
import agents.PredatorPE;
import agents.Prey;

public class PolicyEvaluation extends BasicEnvironment {

	// Constructor for one prey and one predator
	public PolicyEvaluation(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	// Constructor for multiple predators/preys
	public PolicyEvaluation(ArrayList<Predator> predators_,
			ArrayList<Prey> preys_) {
		super(predators_, preys_);
	}

	public int run() {
		int time = 0;
		
		for(Prey prey:preys)
			prey.print();
		
		for (Predator predator : predators) {
			predator.print();
			System.out.println();
			PredatorPE predatorPE = new PredatorPE(predator);
			double[][] values = predatorPE.evaluatePolicy(preys);
			printValues(values);
		}
		

		return time;
	}

	private void printValues(double[][] values) {
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.print((int)values[x][y] + " ");
			}
			System.out.println();
		}
	}

}
