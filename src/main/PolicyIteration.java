package main;

import static agents.Agent.*;

import java.util.ArrayList;

import agents.Predator;
import agents.Prey;

public class PolicyIteration extends BasicEnvironment{
	
	// Constructur for one prey and one predator
	public PolicyIteration(Predator predator_, Prey prey_)
	{
		super(predator_, prey_);
	}
	
	// Constructor for multiple predators/preys
	public PolicyIteration(ArrayList<Predator> predators_, Prey prey_)
	{
		super(predators_, prey_);
	}
	
	public int run() {
		int[][][][] policies = new int[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		double[][][][] values = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		for (int i = 0; i < WORLDSIZE; i++)
			for (int j = 0; j < WORLDSIZE; j++)
				for (int k = 0; k < WORLDSIZE; k++)
					for (int l = 0; l < WORLDSIZE; l++)
						values[i][j][k][l] = (int)(Math.random() * 5); 
		boolean policy_stable = false;
		int count = 0;
		while (!policy_stable) {
			count++;
			System.out.println("Round " + count + ":");
			Pair<double[][][][], Integer>tmp1 = evaluatePolicy(values, policies, prey);
			values = tmp1.a;
			printValues(values, 5, 5);
			if (tmp1.b == 1) {
				policy_stable = true;
				break;
			}
			Pair<int[][][][], Boolean> tmp2 = improvePolicy(policies, values, prey);
			policies = tmp2.a;
			policy_stable = tmp2.b;
			System.out.println();
		}
		
		return 0;
	}
	

	public double sumStates(double[][][][] values, int posX, int posY,
			int px, int py, int a, Prey prey) {
	
		double output = 0.0;
		double gamma = 0.9; // TODO
	
		// Calculate reward of taking action a to go to s'
		// Calculate value of s'
		
		for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
			Predator dummyPredator = new Predator(posX, posY);
			Prey dummyPrey = new Prey(px, py);
			
			double actionReward = getReward(dummyPredator,dummyPrey,a,aPrey);
	
			dummyPredator.move(a);
			dummyPrey.move(aPrey);
			
			double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
			output += prey.prob(getPredators() ,aPrey)*(actionReward + gamma * value);
		}
		
		return output;
	}

	public Pair<double[][][][], Integer> evaluatePolicy(double[][][][] values, int[][][][] policies, Prey prey) {
		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state
	
		double delta = 100.0;
		double theta = 1e-6;
		// Repeat until values have converged
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
									py, policies[x][y][px][py], prey);
							// Updating delta, which is used for the convergence
							// check
							delta = Math.max(delta,
									Math.abs(temp - values[x][y][px][py]));
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
		double[][][][] values, Prey prey) {
	
		boolean policy_stable = true;
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				for (int px = 0; px < WORLDSIZE; px++) {
					for (int py = 0; py < WORLDSIZE; py++) {
						int temp = policies[x][y][px][py];
						double maxValue = 0;
						for (int a = 0; a < DIR_NUM; a++) {
							double value = sumStates(values, x, y, px, py, a, prey);
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
	
	private void printPolicy(int[][] policy) {
		System.out.println();
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

	private void printValues(double[][][][] values, int px, int py) {
		System.out.println();

		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				System.out.printf("%.02f\t", values[x][y][px][py]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
