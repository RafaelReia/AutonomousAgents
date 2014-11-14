package main;
import java.util.ArrayList;

import agents.Predator;
import agents.PredatorVI;
import agents.Prey;

public class ValueIteration extends BasicEnvironment{
	
	// Constructur for one prey and one predator
	public ValueIteration(PredatorVI predator_, Prey prey_)
	{
		super(predator_, prey_);
	}
	
	// Constructor for multiple predators/preys
	public ValueIteration(ArrayList<Predator> predators_, ArrayList<Prey> preys_)
	{
		super(predators_, preys_);
	}
	
	public int run() {
		int time = 0;
//		while (preys.size() > 0) {
//			time ++;
//			System.out.print(time);
//			
//			for (Prey prey : preys) {
//				int dir = prey.planMoveRandom(predators);
//				prey.move(dir);
//				System.out.print(" ");
//				prey.print();
//			}
			
			for (Predator predator : predators) {
				PredatorVI predatorVI = new PredatorVI(predator);
				int dir = predatorVI.planMoveVI(preys.get(0),this);
//				predator.move(dir, preys);
//				System.out.print(" ");
//				predator.print();
			}
//
//			System.out.println();
//		}
		
		return time;
	}



}
