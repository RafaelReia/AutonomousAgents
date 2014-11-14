package main;

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
			//prey.print();
		
		for (Predator predator : predators) {
			//predator.print();
			//System.out.println();
			PredatorPE predatorPE = new PredatorPE(predator);
			double[][][][] values = predatorPE.evaluatePolicy(preys.get(0),this);
			
			preys.get(0).print();
			predator.print();
			System.out.println(", Evaluation = "+values[predator.getX()][predator.getY()][preys.get(0).getX()][preys.get(0).getY()]);
			//System.out.println("TEMP= "+values[2][3][5][4]);
			//System.out.println("TEMP= "+values[2][10][10][10]);
			//System.out.println("TEMP= "+values[10][10][0][0]);
			//printValues(values);
		}
		
		return time;
	}

	private void printValues(double[][][][] values) {
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				for (int px = 0; px < WORLDSIZE; px++) {
					for (int py = 0; py < WORLDSIZE; py++) {
						double temp = values[x][y][px][py];
						if(temp>0)
							System.out.println("TEMP= "+temp);
					}
				}
			}
			//System.out.println();
		}
	}

}
