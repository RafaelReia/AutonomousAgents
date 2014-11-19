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
		double theta = 1e-6;
		// Repeat untill values have converged
		int count = 0;
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
							//System.out.println(x + " " + y + " " + px + " " + py + ": " + delta);
						}
					}
				}
			}
			count++;
			System.out.println(delta);
			
		}
		System.out.println("Number of iterations: " + count);
		return values;
	}

	private double prob(int a) {
		return 0.2;
	}

	/*
	 * Helper function which loops over actions to and sum the values of all
	 * possible actions from that state. Output is the sum.
	 */
	public double sumActions(double[][][][] values, int posX, int posY,
			int px, int py, Prey prey, BasicEnvironment env) {

		double output = 0.0;
		int bestDirection = 0;
		double gamma = 0.8; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			
			for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
				Predator dummyPredator = new Predator(posX, posY);
				Prey dummyPrey = new Prey(px, py);
				
				double actionReward = env.getReward(dummyPredator,dummyPrey,a,aPrey);

				dummyPredator.move(a);
				dummyPrey.move(aPrey);
				
				double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
				output += this.prob(a) * prey.prob(env.getPredators() ,aPrey)*(actionReward + gamma * value);
				//if(output >0)
					//System.out.println("SOMETHING BIIIIIIIIG!!! "+output);
				//System.out.println("Predator Prob " + this.prob(a) + "  Prey Prob" + prey.prob(env.getPredators() ,aPrey) + " Action Re" + actionReward);
			}

		}
		
		//if(output>0)
			//System.out.println("ITS GREATER THAT ZEROOOO!!!! " + output);

		return output;
	}
}
