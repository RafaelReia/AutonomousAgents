/**
 * 
 */
package main;

import static agents.Agent.*;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

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

	public int episodeQL(double[][][][][] Qvalues, double alpha, double gamma,
			double epsilon) {
		/* Initializing the array values */
		int steps = 0;

		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);

		while (!nowPrey.isCaught()) {
			steps++;

			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);

			int aPredator = chooseAction(
					Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
							.getY()], epsilon);
			
			// Prey chooses actions or trips
			int aPrey = chooseOrTrip(
					Qvalues[nowPrey.getX()][nowPrey.getY()][nowPredator.getX()][nowPredator
					                                    							.getY()], epsilon);

			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);

			double reward = getReward(nextPredator, nextPrey);
			Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey
					.getY()][aPredator] = calcNextValue(Qvalues, nowPredator,
					nowPrey, nextPredator, nextPrey, aPredator, reward, alpha,
					gamma);

			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}

		return steps;
	}

	private int chooseOrTrip(double[] ds, double epsilon) {
		Random rdm = null;
		if (rdm.nextDouble() > 0.2)
		{
			return chooseAction(ds, epsilon);
		}
		else
		{
			return 0;
		}
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
			aux = episodeQL(Qvalues, alpha, gamma, epsilon);
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
						parameterSettings[ps][2], parameterSettings[ps][3]);
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

}
