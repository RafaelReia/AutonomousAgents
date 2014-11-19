package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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

		double delta = 10.0;
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

							values[x][y][px][py] = maxAction(values, x, y, px,
									py, prey, env);
							// Updating delta, which is used for the convergence
							// check
							delta = Math.max(delta,
									Math.abs(temp - values[x][y][px][py]));
						}
					}
				}
			}
			count++;
			 System.out.println(delta);

		}
		System.out.println("Number of iterations: " + count);
		//printValues(values);
		// Output best action given the current value field
		Double maxValue = 0.0;
		maxValue = maxAction(values, getX(), getY(), prey.getX(), prey.getY(),
				prey, env);

		printValues(values, 5, 5);
		
		return 0;
	}

	private void printValues(double[][][][] values, int px, int py) {
		System.out.println();

		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.printf("%.02f\t", values[x][y][px][py]);
			}
			System.out.println();
		}
		System.out.println();
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Helper function which loops over actions to look for action
	// with highest output according to value iteration.
	// maxValue is that value, maxArg is the corresponding action
	public double maxAction(double[][][][] values, int posX, int posY,
			int px, int py, Prey prey, BasicEnvironment env) {

		double highestOutput = -Double.MAX_VALUE;
		ArrayList<Integer> bestDirection = new ArrayList<Integer>();
		double gamma = 0.9; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			
			
			double output = 0.0;
			
			for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
				Prey dummyPrey = new Prey(px, py);
				Predator dummyPredator = new Predator(posX, posY);
				double actionReward = env.getReward(dummyPredator,prey,a,aPrey);
				dummyPredator.move(a);
				dummyPrey.move(aPrey);
				
				double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
				output += prey.prob(env.getPredators() ,aPrey)*(actionReward + gamma * value);
			}
			if (output >= highestOutput) {
				highestOutput = output;
				bestDirection.add(a);
			}

		}
		
		//if(highestOutput>0)
			//System.out.println("ITS GREATER THAT HOOOHHOHOHOHOHOHOH!!!! " + highestOutput);
		
		Random rdm = new Random();

		return highestOutput;
	}	
}
