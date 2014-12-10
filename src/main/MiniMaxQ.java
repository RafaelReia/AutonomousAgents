/**
 * 
 */
package main;

import main.Triple;

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

	private int chooseActionPredator(double epsilon, Predator nowPredator, Prey nowPrey) {
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
				aux += PivaluesPredator[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][i];
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
		System.out.println("ERROR");
		return cand.get(rdm.nextInt(cand.size()));
	}

	public Triple
	learnQL(double [][][][][][] Qvalues, double alpha, double gamma,
			double epsilon, double[][][][] Vvalues, double[][][][][] Pivalues, Agent nowPlayer, Agent nowOpponent,  int aPlayer, int aOpponent, Agent nextPlayer, Agent nextOpponent)  {
			double reward =0;
			
			//Q[s,a,o] := (1-alpha) * Q[s,a,o] + alpha * (rew + gamma * V[s鈥橾)
			// compute for the predator
			Qvalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()]
					[nowOpponent.getY()][aPlayer][aOpponent] = (1-alpha)* Qvalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()]
							[nowOpponent.getY()][aPlayer][aOpponent] 
									+ alpha*(reward + gamma
											//computes using the "now values of the prey" because
											//we don't know for where it moved. //FIXME
											* Vvalues[nextPlayer.getX()][nextPlayer.getY()][nowOpponent.getX()]
											[nowOpponent.getY()]); 
			
			// Compute the new policy using linear programming
			
			// The variable array for the linear solver has length DIR_NUM + 1. The first
			// DIR_NUM entries contain the pi[s,a] variables, the last entry contains V.
			
			
			double[] V = new double[DIR_NUM + 1];
			V[DIR_NUM] = 1.0; // the last position is the V variable, which has coefficient 1
			// all other positions are other variables, which have coefficient 0
			LinearObjectiveFunction f = new LinearObjectiveFunction(V, 0); // max V
			Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			
			// Put current Pivalues in variable array, rest of variables are 0
			
			// Inequality constraint for all o':
			//SUM_a(Q[s][a][o']*pi[s][a] * ) >= V
			for (int oPrime = 0; oPrime < DIR_NUM; oPrime++)
			{
				// The q's are the coefficients of the pi's
				double[] coefficientsPiQ = new double[DIR_NUM + 1];
				for (int a = 0; a < DIR_NUM; a++)
				{
					coefficientsPiQ[a] = Qvalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()]
						[nowOpponent.getY()][aPlayer][oPrime];
				}
				constraints.add(new LinearConstraint(
						coefficientsPiQ, 0.0,
						Relationship.GEQ, V, 0.0));

			}
			
			// Equality constraint SUM_a(pi[s,a]) = 1
			// The coefficients of all pi[s,a] should be 1, so the pi values are summed
			double[] coefficientsPi1 = new double[DIR_NUM+1];
			Arrays.fill(coefficientsPi1, 1);
			coefficientsPi1[DIR_NUM] = 0; // coefficient of V is 0
			constraints.add(new LinearConstraint(
					coefficientsPi1, 
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
			
			// Extract solutions
			for(int a = 0; a < DIR_NUM;a++)
			{
				Pivalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()][nowOpponent.getY()][a] = solution.getPoint()[a];
				System.out.println("Pi[s,"+a+"]: "+Pivalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()][nowOpponent.getY()][a]);
			}
			double min = solution.getValue();
			Vvalues[nowPlayer.getX()][nowPlayer.getY()][nowOpponent.getX()][nowOpponent.getY()] = min;
			System.out.println("V[s]: " + min);
			
			
		return new Triple(Qvalues,Vvalues,Pivalues);
	}

	private int chooseActionPrey(double epsilon, Predator nowPredator, Prey nowPrey) {
		Random rdm = new Random();
		if (rdm.nextDouble() > 0.2)
		{
			double max = -Double.MAX_VALUE;
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
					System.out.println("aux: "+ aux);
					aux += PivaluesPredator[nowPrey.getX()][nowPrey.getY()][nowPredator.getX()][nowPredator.getY()][i];
					System.out.println("aux: "+ aux);
					if (randomValue <= aux)
					{
						System.out.println("i");
						return i;
					}
				}
			}
			else {
				// choose any random action for the epsilon part
				System.out.println("random");
				return rdm.nextInt(DIR_NUM);
			}
			return cand.get(rdm.nextInt(cand.size()));
		}
		else
		{
			return 0;
		}
	}
	
	private void learnMiniMax(){
		
		
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

	private void initValues(double initValueQ, double initValueV) {
		for (int predX = 0; predX < WORLDSIZE; predX++)
			for (int predY = 0; predY < WORLDSIZE; predY++)
				for (int preyX = 0; preyX < WORLDSIZE; preyX++)
					for (int preyY = 0; preyY < WORLDSIZE; preyY++) {
						// Initialize V-values
						VvaluesPredator[predX][predY][preyX][preyY] = initValueV;
						VvaluesPrey[predX][predY][preyX][preyY] = initValueV;
						for (int predA = 0; predA < 5; predA++) {
							// Initialize pivalues
							PivaluesPredator[predX][predY][preyX][preyY][predA] = 1.0/5;
							PivaluesPrey[predX][predY][preyX][preyY][predA] = 1.0/5;
							for (int preyA = 0; preyA < 5; preyA++) {
								// Initialize Q-values
								QvaluesPredator[predX][predY][preyX][preyY][predA][preyA] = initValueQ;
								QvaluesPrey[predX][predY][preyX][preyY][predA][preyA] = initValueQ;
							}
						}
					}
	}

	int runs;
	int[] totalSteps = new int[N_EPISODES];
	
	double[][][][][][] QvaluesPredator = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5][5];
	double[][][][] VvaluesPredator = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
	double[][][][][] PivaluesPredator = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];
	double[][][][][][] QvaluesPrey = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5][5];
	double[][][][] VvaluesPrey = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
	double[][][][][] PivaluesPrey = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];
	
	public int test(double alpha, double gamma, double epsilon, double initValueQ, double initValueV) {
		int time = 0;
		initValues(initValueQ, initValueV);
		
		Triple auxPredator;
		Triple auxPrey;
		runs++;
		for (int i = 0; i < N_EPISODES; i++) {
			// New predator and prey every episode
			Predator nowPredator = new Predator(predators.get(0));
			Prey nowPrey = new Prey(prey);
			int steps = 0;
			double lastReward = 0;
			while(lastReward != 10)
			{
				Predator nextPredator = new Predator(nowPredator);
				Prey nextPrey = new Prey(nowPrey);
				
				//FIXME I set the action to 0 because we don't use/know the opponents chosen action. 
				int aPredator = chooseActionPredator(epsilon, nowPredator, nowPrey);
				
				int aPrey = chooseActionPrey(epsilon, nowPredator, nowPrey);
				
				auxPredator = learnQL(QvaluesPredator, alpha, gamma, epsilon, VvaluesPredator, PivaluesPredator, nowPredator, nowPrey, aPredator, aPrey, nextPredator, nextPrey);
				auxPrey = learnQL(QvaluesPredator, alpha, gamma, epsilon, VvaluesPredator, PivaluesPredator, nowPrey, nowPredator, aPrey, aPredator, nextPrey, nextPredator);
				
				alpha = alpha*gamma;
				
				//move the next prey state.
				nextPrey.move(aPrey);
				nextPredator.move(aPredator, nextPrey);
				steps++;
				// System.out.println("episode: " + i + ", steps: " + aux);
				
			}
			totalSteps[i] += steps;
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
				sum += totalSteps[i] / runs;
				f.println(totalSteps[i] / runs);
			}
			double average = sum / N_EPISODES;
			System.out.println("Average: " + average);

			f.close();
		}
		return 0;

	}
	

}
