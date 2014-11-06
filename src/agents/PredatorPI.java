package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPI extends Predator {

	public PredatorPI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPI(Agent predator) {
		super(predator);
	}

	public int[][] improvePolicy(int[][] policy, double[][] values,
			ArrayList<Prey> preys) {

		boolean test = true;
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				int old = policy[x][y];

				maxAction(values, x, y, preys);
				policy[x][y] = maxArg;
				// Updating delta, which is used for the convergence check
				// delta = Math.max(delta, Math.abs(temp - valuesAux[x][y]));
				if (old != maxArg)
					test = false;
			}
		}

		if (!test) {
			evaluatePolicy(policy, preys);
		}
		printPolicy(policy);
		return policy;
	}

	private void printPolicy(int[][] policy) {
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

	public int planMovePI(int[][] policy) {
		return policy[getX()][getY()];
	}

	public int[][] evaluatePolicy(int[][] policy, ArrayList<Prey> preys) {
		double[][] values = new double[WORLDSIZE][WORLDSIZE];
		double[][] valuesAux = new double[WORLDSIZE][WORLDSIZE];

		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state

		double delta = 100.0;
		double theta = 0.1;
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
			// System.out.println("TEST" + delta);
			/*
			 * System.out.println("TEST"); for (int x = 0; x < WORLDSIZE; x++) {
			 * for (int y = 0; y < WORLDSIZE; y++) {
			 * System.out.print((int)values[x][y]+ " "); } System.out.println();
			 * } try { System.in.read(); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
		}

		// Output best action given the current value field
		// Double maxValue = 0.0;
		// maxValue = maxAction(values, getX(), getY(), preys);

		/*
		 * System.out.println("TEST 22"); for (int x = 0; x < WORLDSIZE; x++) {
		 * for (int y = 0; y < WORLDSIZE; y++) {
		 * System.out.print((int)values[x][y]+ " "); } System.out.println(); }
		 * try { System.in.read(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		return improvePolicy(policy, values, preys);
	}

}
