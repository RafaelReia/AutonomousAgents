package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPE extends Predator {

	public PredatorPE(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPE(Agent predator) {
		super(predator);
	}

	public double[][] evaluatePolicy(ArrayList<Prey> preys) {
		double[][] values = new double[WORLDSIZE][WORLDSIZE];
		double[][] valuesAux = new double[WORLDSIZE][WORLDSIZE];

		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state

		double delta = 100.0;
		double theta = 0.001;
		// Repeat untill values have converged
		while (delta > theta) {
			delta = 0.0;
			for (int x = 0; x < WORLDSIZE; x++) {
				for (int y = 0; y < WORLDSIZE; y++) {
					double temp = values[x][y];

					valuesAux[x][y] = sumActions(values, x, y, preys);
					// Updating delta, which is used for the convergence check
					delta = Math.max(delta, Math.abs(temp - valuesAux[x][y]));
				}
			}
			values = valuesAux;
			valuesAux = new double[WORLDSIZE][WORLDSIZE];
			//System.out.println("TEST" + delta);
			/*
			 * System.out.println("TEST"); for (int x = 0; x < WORLDSIZE; x++) {
			 * for (int y = 0; y < WORLDSIZE; y++) {
			 * System.out.print((int)values[x][y]+ " "); } System.out.println();
			 * } try { System.in.read(); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
		}

		// Output best action given the current value field
		Double maxValue = 0.0;
		maxValue = maxAction(values, getX(), getY(), preys);

		/*
		 * System.out.println("TEST 22"); for (int x = 0; x < WORLDSIZE; x++) {
		 * for (int y = 0; y < WORLDSIZE; y++) {
		 * System.out.print((int)values[x][y]+ " "); } System.out.println(); }
		 * try { System.in.read(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		return values;
	}

}
