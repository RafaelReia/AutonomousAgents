/**
 * 
 */
package main;

import static agents.Agent.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import agents.Predator;
import agents.Prey;

/**
 *
 */
public class SoftmaxQLearning extends QLearning {

	private static final int N_EPISODES = 100;

	/**
	 * @param predator_
	 * @param prey_
	 */
	public SoftmaxQLearning(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public SoftmaxQLearning(ArrayList<Predator> predators_, Prey preys_) {
		super(predators_, preys_);
	}

	protected int chooseAction(double[] qvalues, double temp) {
		int action = 0;
		double prob[] = new double[DIR_NUM];
		double sumProb = 0;
		
		for (int a = 0; a < DIR_NUM; a++) {
			prob[a] = Math.exp(qvalues[a] / temp);
			sumProb += prob[a];
		}
		
		for (int a = 0; a < DIR_NUM; a++) {
			prob[a] /= sumProb;
		}
		
		sumProb = 0;
		double randProb = Math.random();
		for (int a = 0; a < DIR_NUM; a++) {
			sumProb += prob[a];
			if (randProb < sumProb) {
				action = a;
				break;
			}
		}
		return action;
	}


	public int episodeQL(double[][][][][] Qvalues, double alpha, double gamma, double temp) {
		/* Initializing the array values */
		int steps = 0;
		
		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);
		
		while (!nowPrey.isCaught()) {
			steps++;
			
			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);
			
			int aPredator = chooseAction(Qvalues[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()], temp);
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

	protected double calcNextValue(double[][][][][] Qvalues, Predator nowPredator, Prey nowPrey, Predator nextPredator, Prey nextPrey, int aPredator, double reward, double alpha, double gamma) {
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
	public int test(double alpha, double gamma, double temp, double initValue) {
		double[][][][][] Qvalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];
		int aux;
		runs++;
		initValues(Qvalues, initValue);
		for (int i = 0; i < N_EPISODES; i++) {
			//System.out.println(episodeQL(Qvalues, alpha, gamma, temp));
			aux = episodeQL(Qvalues, alpha, gamma, temp);
			//System.out.println("episode: " + i + ", steps: " + aux);
			steps[i] += aux;
		}

		return 0;
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
