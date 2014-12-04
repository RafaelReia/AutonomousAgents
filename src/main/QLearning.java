/**
 * 
 */
package main;

import static agents.Agent.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import agents.Predator;
import agents.Prey;

/**
 *
 */
public class QLearning extends BasicEnvironment {

	private static final int N_EPISODES = 10000;
	/**
	 * @param predator_
	 * @param prey_
	 */
	public QLearning(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public QLearning(ArrayList<Predator> predators_, Prey preys_) {
		super(predators_, preys_);
	}

	private int chooseAction(double[] qvalues, double epsilon) {
		double max = -Double.MAX_VALUE;
		Random rdm = new Random();
		ArrayList<Integer> cand = new ArrayList<Integer>();
		if (rdm.nextDouble() > epsilon)
			for (int i = 0; i < DIR_NUM; i++) {
				if (qvalues[i] > max) {
					cand.clear();
					max = qvalues[i];
				}
				if (qvalues[i] == max) {
					cand.add(i);
				}
			}
		else {
			// choose any random action for the epsilon part
			return rdm.nextInt(DIR_NUM);
		}
		return cand.get(rdm.nextInt(cand.size()));
	}


	public int episodeQL(double[][][][][] Qvalues, double alpha, double gamma, double epsilon) {
		/* Initializing the array values */
		int steps = 0;
		
		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);
		
		while (!nowPrey.isCaught()) {
			steps++;
			
			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);
			
			int aPredator = chooseAction(Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()], epsilon);
			int aPrey = nowPrey.planMoveRandom(nowPredator); //check this if not working XXX

			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);
			
			double reward = getReward(nextPredator, nextPrey);
			Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][aPredator]
					= calcNextValue(Qvalues, nowPredator, nowPrey, nextPredator, nextPrey, aPredator, reward, alpha, gamma);
			
			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}
		
		return steps;
	}

	private double calcNextValue(double[][][][][] Qvalues, Predator nowPredator, Prey nowPrey, Predator nextPredator, Prey nextPrey, int aPredator, double reward, double alpha, double gamma) {
		return Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][aPredator]
				+ alpha
				* (reward
						+ gamma
						* getMax(Qvalues[nextPredator.getX()][nextPredator.getY()][nextPrey.getX()][nextPrey.getY()])
						- Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][aPredator]);
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
	
	private void initValues(double[][][][][] values, double initValue) {
		for (int px = 0; px < WORLDSIZE; px++)
			for (int py = 0; py < WORLDSIZE; py++)
				for (int x = 0; x < WORLDSIZE; x++)
					for (int y = 0; y < WORLDSIZE; y++)
						for (int m = 0; m < 5; m++) {
							values[px][py][x][y][m] = initValue;
						}

	}
	int runs;
	int [] steps = new int[N_EPISODES];
	public int test(double alpha, double gamma, double epsilon, double initValue) {
		int time = 0;
		double[][][][][] Qvalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];
		initValues(Qvalues, initValue);
		int aux;
		runs++;
		for (int i = 0; i < N_EPISODES; i++) {
			aux = episodeQL(Qvalues, alpha, gamma, epsilon);
			//System.out.println("episode: " + i + ", steps: " + aux);
			steps[i] += aux;
		}
		
		return time;
	}
	
	public int run(double[][] parameterSettings) {
		// For every parameter setting
		for (int ps = 0; ps < parameterSettings.length;ps++)
		{
			for(int i = 0;i<100;i++)
			{
				test(parameterSettings[ps][0],parameterSettings[ps][1],parameterSettings[ps][2],parameterSettings[ps][3]);
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
				sum += steps[i]/runs;
				f.println(steps[i]/runs);
			}
			double average = sum / N_EPISODES;
			System.out.println("Average: " +  average);
			
			f.close();
		}
		return 0;
		
	}

}
