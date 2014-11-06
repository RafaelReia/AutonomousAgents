package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorVI extends Predator {

	public PredatorVI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorVI(Agent predator) {
		super(predator);
	}


	public int planMoveVI(ArrayList<Prey> preys) {
		double[][] values = new double[WORLDSIZE][WORLDSIZE];

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

					values[x][y] = maxAction(values, x, y, preys);
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
		maxValue = maxAction(values, getX(), getY(),preys);
		
		/*System.out.println("TEST");
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.print(values[x][y]+ " ");
			}
			System.out.println();
		}*/
		
		return maxArg;
	}

	
}
