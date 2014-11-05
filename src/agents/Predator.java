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

			double value = values[this.getX()][this.getY()];
			double output = actionReward + gamma * value;

			// Save output if higher than of previous actions
			if (output > highestOutput) {
				highestOutput = output;
				bestDirection = a;
			}

		}
		maxArg = bestDirection;
		if(maxValue > 0)
			System.out.println("maxValue > 0");
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

	public int planMoveRandom() {
		return (int) Math.floor(Math.random() * DIR_NUM);
	}

	double[][] values = new double[WORLDSIZE][WORLDSIZE];
	public int planMoveVI(ArrayList<Prey> preys) {

		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state

		double delta = 1.0;
		double theta = 0.1;
		// Repeat untill values have converged
		while (delta > theta) {
			delta = 0.0;
			for (int x = 0; x < WORLDSIZE; x++) {
				for (int y = 0; y < WORLDSIZE; y++) {
					double temp = values[x][y];

					Double maxValue = 0.0;
					
					values[x][y] = maxAction(values, x, y, maxValue, preys);
					// Updating delta, which is used for the convergence check
					delta = Math.max(delta, Math.abs(temp - values[x][y]));
				}
			}
			// System.out.println("TEST" + delta);

		}

		/*
		 * Repeat ∆ ← 0 For each s ∈ S: temp ← v(s) v(s) ← max a s p(s |s,
		 * a)[r(s, a, s ) + γv(s )] ∆ ← max(∆, |temp − v(s)|) until ∆ < θ (a
		 * small positive number)
		 * 
		 * 
		 * Output a deterministic policy, π, such that π(s) = arg max a SUM p(s
		 * |s, a) r(s, a, s ) + γv(s )
		 */

		// Output best action given the current value field
		Double maxValue = 0.0;
		maxValue = maxAction(values, getX(), getY(), maxValue, preys);
		
		System.out.println("TEST");
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.print(values[x][y]+ " ");
			}
			System.out.println();
		}
		
		return maxArg;
	}
	Integer maxArg = 0;

	public int planMovePI(ArrayList<Prey> preys) {
		return 0;
	}

	public int planMovePE(ArrayList<Prey> preys) {
		return 0;
	}

	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
