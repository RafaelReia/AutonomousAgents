package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public Predator(Agent predator) {
		super(predator);
	}
	
	Integer maxArg = 0; // This is the ouput variable for maxAction

	// Helper function which loops over actions to look for action
	// with highest output according to value iteration.
	// maxValue is that value, maxArg is the corresponding action
	public double maxAction(double[][] values, int posX, int posY,
			Double maxValue, ArrayList<Prey> preys) {

		double highestOutput = 0.0;
		int bestDirection = 0;
		double gamma = 0.8; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			Predator dummyP = new Predator(posX, posY);
			double actionReward = dummyP.getReward(a, preys);

			double value = values[dummyP.getX()][dummyP.getY()];
			double output = actionReward + gamma * value;
			/*if(output > 0 && output < 20)
				System.out.println("output > 0"+output);
			if(actionReward > 0 && actionReward < 20)
				System.out.println("actionReward > 0"+output);*/
			//System.out.println(output);
			// Save output if higher than of previous actions
			if (output > highestOutput) {
				highestOutput = output;
				bestDirection = a;
			}

		}
		maxArg = bestDirection;

		return highestOutput;
	}

	public int move(int DIR, ArrayList<Prey> preys) {
		move(DIR);

		int reward = 0;
		for (Iterator<Prey> iterator = preys.iterator(); iterator.hasNext();) {
			if (this.equals(iterator.next())) {
				reward += 10;
				iterator.remove();
			}
		}

		return reward;
	}

	// Get reward for a certain field, without really moving into that direction
	public int getReward(int DIR, ArrayList<Prey> preys) {
		// Create dummy predator with same location as current predator
		// Predator dummy = new Predator(posX, posY);
		move(DIR);

		// Move dummy predator
		int reward = 0;
		for (Iterator<Prey> iterator = preys.iterator(); iterator.hasNext();) {
			if (this.equals(iterator.next())) {
				reward += 10;
			}
		}
		return reward;
	}


	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
