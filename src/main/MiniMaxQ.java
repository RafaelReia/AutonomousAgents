/**
 * 
 */
package main;


import static agents.Agent.*;

import java.util.Arrays;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import agents.Agent;
import agents.Predator;
import agents.Prey;

/**
 *
 */
public class MiniMaxQ extends BasicEnvironment {

	private static final int N_EPISODES = 10000;

	/**
	 * @param predator_
	 * @param prey_
	 */
	public MiniMaxQ(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public MiniMaxQ(ArrayList<Predator> predators_, Prey preys_) {
		super(predators_, preys_);
	}

	private int chooseAction(double[] pivalues, double epsilon) {
		double max = -Double.MAX_VALUE;
		Random rdm = new Random();
		ArrayList<Integer> cand = new ArrayList<Integer>();
		if (rdm.nextDouble() > epsilon)
		{
			// Draw new number to determine which policy action to take
			double randomValue = rdm.nextDouble();
			double aux = 0.0;
			
			for (int i=0; i < DIR_NUM; i++)
			{
				// If random value less than complementary probability of i
				// Assign to i
				aux += pivalues[i];
				if (randomValue <= aux)
				{
					return i;
				}
			}
		}
		else {
			// choose any random action for the epsilon part
			return rdm.nextInt(DIR_NUM);
		}
		return cand.get(rdm.nextInt(cand.size()));
	}

	public int episodeQL(double[][][][][][] Qvalues, double alpha, double gamma,
			double epsilon, double[][][][] Vvalues, double[][][][][] Pivalues)  {
		/* Initializing the array values */
		int steps = 0;

		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);

		while (!Agent.isCaught()) { //FIXME when it's not over
			steps++;

			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);

			//FIXME I set the action to 0 because we don't use/know the opponents chosen action. 
			int aPredator = chooseAction(
					Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
							.getY()][0], epsilon);
			
			//FIXME I set the action to 0 because we don't use/know the opponents chosen action.
			// Prey chooses actions or trips
			int aPrey = chooseOrTrip(
					Qvalues[nowPrey.getX()][nowPrey.getY()][nowPredator.getX()][nowPredator
					                                    							.getY()][0], epsilon);

			//move the next prey state.
			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);

			//!!!!!!!!!!!Learn!!!!!!!!!!
			/*Reward is the reward getted by moving for the state*/
			//FIXME in our case is always 0 right? because when there is a reward the game ends.
			double reward =0;
			
			//TODO finish this;
			/* After receiving reward rew for moving from state s to s鈥�
			 * via action a and opponent鈥檚 action o
			 * */
			
			//Q[s,a,o] := (1-alpha) * Q[s,a,o] + alpha * (rew + gamma * V[s鈥橾)
			// compute for the predator
			Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()]
					[nowPrey.getY()][aPredator][aPrey] = (1-alpha)* Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()]
							[nowPrey.getY()][aPredator][aPrey] 
									+ alpha*(reward + gamma
											//computes using the "now values of the prey" because
											//we don't know for where it moved. //FIXME
											* Vvalues[nextPredator.getX()][nextPredator.getY()][nowPrey.getX()]
											[nowPrey.getY()]); 
			
			// Compute the new policy using linear programming
			
			// The variable array for the linear solver has length 2 * DIR_NUM + 1. The first
			// DIR_NUM entries contain the pi[s,a] variables, the second DIR_NUM entries
			// contain the Q[s,a,o]*pi[s,a] variables, the last entry contains V.
			
			// First compute min(o', sum(a', pi[s,a' * Q[s,a',o')
			
			double[] V = new double[2 * DIR_NUM + 1];
			Arrays.fill(V,0.0); // all other positions are other variables, which have coefficient 0
			V[2 * DIR_NUM] = 1.0; // the last position is the V variable, which has coefficient 1
			LinearObjectiveFunction f = new LinearObjectiveFunction(V, 0); // max V
			Collection constraints = new ArrayList();
			
			// Put current Pivalues in variable array, rest of variables are 0
			double[] PivaluesCurrent = Pivalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()];

			// Inequality constraint for all o':
			//SUM_a(pi[s][a] * Q[s][a][o']) >= V
			for (int oPrime = 0; oPrime < DIR_NUM; oPrime++)
			{
				double[] QvaluesCurrent = new double[DIR_NUM];
				for (int i = 0; i < DIR_NUM; i++)
				{
					QvaluesCurrent[i] = Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][i][oPrime];
				}
				constraints.add(new LinearConstraint(
						multiplyArrays(PivaluesCurrent,
									QvaluesCurrent), 0.0,
						Relationship.GEQ, V, 0.0));

			}
			
			// Equality constraint SUM_a(pi[s,a]) = 1
			constraints.add(new LinearConstraint(
					createVariableArray(PivaluesCurrent), 
					Relationship.EQ, 
					1.0));

			// create and run the solver
			RealPointValuePair solution = null;
			try {
				solution = new SimplexSolver().optimize(f, constraints, GoalType.MAXIMIZE, true);
			} catch (OptimizationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			double x = solution.getPoint()[0];
			double y = solution.getPoint()[1];
			double min = solution.getValue();
			
			alpha = alpha*gamma;
			
			
			//Computes with the next prey
			/*double reward = getReward(nextPredator, nextPrey);
			Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
					.getY()][aPredator] = calcNextValue(Qvalues, nowPredator,
					nowPrey, nextPredator, nextPrey, aPredator, reward, alpha,
					gamma);*/

			//It's only move here.
			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}

		return steps;
	}

	private int chooseOrTrip(double[] ds, double epsilon) {
		Random rdm = new Random();
		if (rdm.nextDouble() > 0.2)
		{
			return chooseAction(ds, epsilon);
		}
		else
		{
			return 0;
		}
	}
	
	private void learnMiniMax(){
		
		
	}

	private double calcNextValue(double[][][][][] Qvalues,
			Predator nowPredator, Prey nowPrey, Predator nextPredator,
			Prey nextPrey, int aPredator, double reward, double alpha,
			double gamma) {
		return Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
				.getY()][aPredator]
				+ alpha
				* (reward
						+ gamma
						* getMax(Qvalues[nextPredator.getX()][nextPredator
								.getY()][nextPrey.getX()][nextPrey.getY()]) - Qvalues[nowPredator
							.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
						.getY()][aPredator]);
	}

	private double getMax(double[] qvalues) {
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < DIR_NUM; i++) {
			if (qvalues[i] >= max) {
				max = qvalues[i];
			}
		}
		return max;
	}

	private void initValues(double[][][][][][] qvalues, double[][][][] vvalues, double[][][][][] pivalues,
			double initValueQ, double initValueV) {
		for (int predX = 0; predX < WORLDSIZE; predX++)
			for (int predY = 0; predY < WORLDSIZE; predY++)
				for (int preyX = 0; preyX < WORLDSIZE; preyX++)
					for (int preyY = 0; preyY < WORLDSIZE; preyY++) {
						// Initialize V-values
						vvalues[predX][predY][preyX][preyY] = initValueV;
						for (int predA = 0; predA < 5; predA++) {
							// Initialize pivalues
							pivalues[predX][predY][preyX][preyY][predA] = 1/5;
							for (int preyA = 0; preyA < 5; preyA++) {
								// Initialize Q-values
								qvalues[predX][predY][preyX][preyY][predA][preyA] = initValueQ;
							}
						}
					}
	}

	int runs;
	int[] steps = new int[N_EPISODES];

	public int test(double alpha, double gamma, double epsilon, double initValueQ, double initValueV) {
		int time = 0;
		double initValue = 1.0;
		double[][][][][][] Qvalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5][5];
		double[][][][] Vvalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		double[][][][][] Pivalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];
		initValues(Qvalues, Vvalues, Pivalues, initValueQ, initValueV);

		int aux;
		runs++;
		for (int i = 0; i < N_EPISODES; i++) {
			aux = episodeQL(Qvalues, alpha, gamma, epsilon, Vvalues, Pivalues);
			// System.out.println("episode: " + i + ", steps: " + aux);
			steps[i] += aux;
		}

		return time;
	}

	public int run(double[][] parameterSettings) {
		// For every parameter setting
		for (int ps = 0; ps < parameterSettings.length; ps++) {
			for (int i = 0; i < 100; i++) {
				test(parameterSettings[ps][0], parameterSettings[ps][1],
						parameterSettings[ps][2], parameterSettings[ps][3], parameterSettings[ps][4]);
			}
			// Open file to write results
			PrintWriter f = null;
			try {
				f = new PrintWriter("output" + ps + ".txt");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			double sum = 0;
			for (int i = 0; i < N_EPISODES; i++) {
				sum += steps[i] / runs;
				f.println(steps[i] / runs);
			}
			double average = sum / N_EPISODES;
			System.out.println("Average: " + average);

			f.close();
		}
		return 0;

	}
	
	
	/* For each element in the array, multiply it with the corresponding element
	 * in the other array and sum the resulting products
	 * THese sums are placed in the DIR_NUM till 2*DIR_NUM entries of the variable array */
	double[] multiplyArrays(double[] array1, double[] array2)
	{
		double[] newArray = new double[2 * DIR_NUM + 1];
		Arrays.fill(newArray,0.0);
		for (int a = 0; a < DIR_NUM; a++)
		{
			newArray[a+DIR_NUM] = array1[a] * array2[a];
		}
		return newArray;
	}
	
	// Puts array in the first DIR_NUM elements of new_array
	double[] createVariableArray(double[] array)
	{
		// Put current Pivalues in variable array, rest of variables are 0
		double[] newArray = new double[2*DIR_NUM + 1];
		Arrays.fill(newArray, 0.0);
		for (int i = 0; i < DIR_NUM; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}

}
