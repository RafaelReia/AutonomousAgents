package agents;

import java.util.ArrayList;
import java.util.Iterator;

import main.BasicEnvironment;
import static main.BasicEnvironment.WORLDSIZE;

public class PredatorVI extends Predator {

	public PredatorVI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorVI(Agent predator) {
		super(predator);
	}

	public int planMoveVI(Prey prey, BasicEnvironment env) {
		double[][][][] values = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];

		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state

		double delta = 1.0;
		double theta = 0.1;
		// Repeat untill values have converged
		while (delta > theta) {
			delta = 0.0;
			for (int x = 0; x < WORLDSIZE; x++) {
				for (int y = 0; y < WORLDSIZE; y++) {
					for (int px = 0; px < WORLDSIZE; px++) {
						for (int py = 0; py < WORLDSIZE; py++) {
							double temp = values[x][y][px][py];

							values[x][y][px][py] = maxAction(values, x, y, px, py, prey, env);
							// Updating delta, which is used for the convergence
							// check
							delta = Math.max(delta,
									Math.abs(temp - values[x][y][px][py]));
						}
					}
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
		maxValue = maxAction(values, getX(), getY(), prey.getX(),prey.getY() , prey, env);

		return maxArg;
	}

}
