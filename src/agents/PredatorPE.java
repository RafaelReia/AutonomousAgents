package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import main.BasicEnvironment;
import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPE extends Predator {

	public PredatorPE(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPE(Agent predator) {
		super(predator);
	}

	public double[][][][] evaluatePolicy(Prey prey, BasicEnvironment env) {
		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state
		double[][][][] values = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		double[][][][] valuesAux = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];

		double delta = 100.0;
		double theta = 1;
		// Repeat untill values have converged
		while (delta > theta) {
			delta = 0.0;
			for (int x = 0; x < WORLDSIZE; x++) {
				for (int y = 0; y < WORLDSIZE; y++) {
					for (int px = 0; px < WORLDSIZE; px++) {
						for (int py = 0; py < WORLDSIZE; py++) {
							double temp = values[x][y][px][py];

							values[x][y][px][py] = sumActions(values, x, y, px,
									py, prey, env);
							// Updating delta, which is used for the convergence
							// check
							delta = Math.max(delta,
									Math.abs(temp - values[x][y][px][py]));
						}
					}
				}
			}
		}

		return values;
	}

}
