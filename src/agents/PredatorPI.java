package agents;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Iterator;

import main.BasicEnvironment;
import main.PolicyEvaluation;
import main.PolicyIteration;
import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPI extends Predator {

	public PredatorPI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPI(Agent predator) {
		super(predator);
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

	public double sumStates(double[][][][] values, int posX, int posY,
			int px, int py, int a, Prey prey, BasicEnvironment env) {

		double output = 0.0;
		int bestDirection = 0;
		double gamma = 0.9; // TODO

		// Calculate reward of taking action a to go to s'
		// Calculate value of s'
		
		for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
			Predator dummyPredator = new Predator(posX, posY);
			Prey dummyPrey = new Prey(px, py);
			
			double actionReward = env.getReward(dummyPredator,dummyPrey,a,aPrey);

			dummyPredator.move(a);
			dummyPrey.move(aPrey);
			
			double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
			output += prey.prob(env.getPredators() ,aPrey)*(actionReward + gamma * value);
			//if(output >0)
				//System.out.println("SOMETHING BIIIIIIIIG!!! "+output);
			//System.out.println("Predator Prob " + this.prob(a) + "  Prey Prob" + prey.prob(env.getPredators() ,aPrey) + " Action Re" + actionReward);
		}
		
		//if(output>0)
			//System.out.println("ITS GREATER THAT ZEROOOO!!!! " + output);

		return output;
	}
	
	public Pair<double[][][][], Integer> evaluatePolicy(double[][][][] values, int[][][][] policies, Prey prey, BasicEnvironment env) {
		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state

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
							//System.out.println(policies[x][y][px][py]);
							values[x][y][px][py] = sumStates(values, x, y, px,
									py, policies[x][y][px][py], prey, env);
							// Updating delta, which is used for the convergence
							// check
							delta = Math.max(delta,
									Math.abs(temp - values[x][y][px][py]));
							//System.out.println(x + " " + y + " " + px + " " + py + ": " + delta);
						}
					}
				}
			}
			System.out.println(delta);
			count++;
		}
		System.out.println("Number of iterations: " + count);
		return new Pair<double[][][][], Integer>(values, count);
	}

	public Pair<int[][][][], Boolean> improvePolicy(int[][][][] policies,
			double[][][][] values, Prey prey, PolicyIteration env) {
		
		boolean policy_stable = true;
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				for (int px = 0; px < WORLDSIZE; px++) {
					for (int py = 0; py < WORLDSIZE; py++) {
						int temp = policies[x][y][px][py];
						double maxValue = 0;
						for (int a = 0; a < DIR_NUM; a++) {
							double value = sumStates(values, x, y, px, py, a, prey, env);
							if (value > maxValue) {
								maxValue = value;
								policies[x][y][px][py] = a;
							}
						}
						if (temp != policies[x][y][px][py]) {
							policy_stable = false;
						}
					}
				}
			}
		}
		
		System.out.println("policy-stable: " + policy_stable);
		
		return new Pair<int[][][][], Boolean>(policies, policy_stable);
	}
}