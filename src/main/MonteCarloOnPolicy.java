package main;

import java.util.ArrayList;

import agents.Episode;
import agents.Predator;
import agents.Prey;
import static agents.Agent.DIR_NUM;

public class MonteCarloOnPolicy extends SoftmaxQLearning {

	private static final int N_EPISODES = 10000;

	int runs;
	int [] steps = new int[N_EPISODES];
	
	/**
	 * @param predator_
	 * @param prey_
	 */
	public MonteCarloOnPolicy(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public MonteCarloOnPolicy(ArrayList<Predator> predators_, Prey preys_) {
		super(predators_, preys_);
	}

	private void initValues(double[][][][][] values, double initValue) {
		for (int px = 0; px < WORLDSIZE; px++)
			for (int py = 0; py < WORLDSIZE; py++)
				for (int x = 0; x < WORLDSIZE; x++)
					for (int y = 0; y < WORLDSIZE; y++)
						for (int m = 0; m < DIR_NUM; m++) {
							values[px][py][x][y][m] = initValue;
						}

	}
	
	
	
	public int run(double[][] parameterSettings) {
		// For every parameter setting
		double[] ii = new double[parameterSettings.length];
		for (int ps = 0; ps < parameterSettings.length;ps++)
		{
			ii[ps] = 0;
			for(int i = 0;i<10;i++)
			{
				ii[ps] += test(parameterSettings[ps][0],parameterSettings[ps][1],parameterSettings[ps][2]);
			}
			ii[ps] /= 10;
		}
		for (int i = 0; i < parameterSettings.length; i++) {
			System.out.println(ii[i]);
		}
		return 0;
	}
	
	public int test(double alpha, double gamma, double temp) {
		double[][][][][] Q = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][][] Returns = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM][N_EPISODES];
		double[][][][][] pi_b = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] pi = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		
		Episode episode;
		
		initValues(pi_b, 0.2);
		
		boolean converged = false;
		int i;
		for (i = 0; !converged; i++) {
			converged = true;
			// generate an episode based on policy pi_b
			episode = generateEpisode(pi_b, alpha, gamma, temp);
			int T = episode.length() + 1;
			
			/*// tao <- latest time at which a_tao != pi(s_tao)
			int tao;
			for (tao = 0; tao < T - 1; tao++) {
				Predator predator = episode.getPredator(tao);
				Prey prey = episode.getPrey(tao);
				int a = episode.getPredatorAction(tao);
				if (a != pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()]) {
					break;
				}
			}
			
			// Pre-process the production part
			double[] w = new double[T];
			w[T - 1] = 1;
			for (int t = T - 2; t >= 0; t--) {
				Predator predator = episode.getPredator(t);
				Prey prey = episode.getPrey(t);
				int a = episode.getPredatorAction(t);
				w[t] = w[t + 1] / pi_b[predator.getX()][predator.getY()][prey.getX()][prey.getY()][a];
			}*/
			
			// For each pair s,a appearing in the episode
			for (int t = 0; t < T - 1; t++) {
				Predator predator = episode.getPredator(t);
				Prey prey = episode.getPrey(t);
				int aPredator = episode.getPredatorAction(t);
				int aPrey = episode.getPreyAction(t);
				
				// Calculate reward of this state
				double G = getReward(predator,prey);
				
				
				// Append reward G to Returns(s,a)
				Returns[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator][i] = G;
				
				// Q(s,a) = average(Returns(s,a))
				Q[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] = calcAverage( Returns[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] );
				
			}
			
			// For each s appearing in the episode
			for (int t = 0; t < T - 1; t++) {
				Predator predator = episode.getPredator(t);
				Prey prey = episode.getPrey(t);
				int aPredator = episode.getPredatorAction(t);
				int aPrey = episode.getPreyAction(t);
				
				int newA = getArgMax(Q[predator.getX()][predator.getY()][prey.getX()][prey.getY()]);
				// Loop over all possible actions to update policy
				for (int j = 0; j < DIR_NUM; j++) {
					if (aPredator == newA)
					{
						pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] = (1-temp+(temp/DIR_NUM));
					}
					else // aPredator != newA
					{
						pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] = temp/DIR_NUM;
					}
				}
			}
			//printPolicy(pi, this.prey.getX(), this.prey.getY());
		}
		return i;
	}

	private Episode generateEpisode(double[][][][][] pi_b, double alpha, double gamma, double temp) {
		Episode episode = new Episode(); 
		
		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);
		
		while (!nowPrey.isCaught()) {
			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);
			
			int aPredator = chooseAction(pi_b[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()], temp);
			int aPrey = nowPrey.planMoveRandom(nowPredator); //check this if not working XXX

			episode.append(nowPredator, nowPrey, aPredator, aPrey);
			
			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);
			
			double reward = getReward(nextPredator, nextPrey);
			pi_b[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][aPredator]
					= calcNextValue(pi_b, nowPredator, nowPrey, nextPredator, nextPrey, aPredator, reward, alpha, gamma);
			
			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}
		
		return episode;
	}
	
	private int countSteps(int[][][][] pi) {
		int steps = 0;
		
		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);
		
		while (!nowPrey.isCaught()) {
			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);
			
			int aPredator = pi[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()];
			int aPrey = nowPrey.planMoveRandom(nowPredator); //check this if not working XXX

			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);
			
			steps++;
			
			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}
		
		return steps;
	}
	
	public static double calcAverage (double[] array)
	{
		double sum = 0;
		// Sum
		for (int i = 0; i < array.length; i++)
		{
			sum += array[i];
		}
		// Divide by number of items
		return sum / array.length;
	}
	
	private int getArgMax(double[] qvalues) {
		double max = -Double.MAX_VALUE;
		int maxDir = 0;
		for (int i = 0; i < DIR_NUM; i++) {
			if (qvalues[i] >= max) {
				max = qvalues[i];
				maxDir = i;
			}
		}
		return maxDir;
	}
}
