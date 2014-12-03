package main;

import java.util.ArrayList;

import agents.Episode;
import agents.Predator;
import agents.Prey;
import static agents.Agent.DIR_NUM;

public class MonteCarloOffPolicy extends SoftmaxQLearning {

	private static final int N_EPISODES = 10000;

	int runs;
	int [] steps = new int[N_EPISODES];
	
	/**
	 * @param predator_
	 * @param prey_
	 */
	public MonteCarloOffPolicy(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public MonteCarloOffPolicy(ArrayList<Predator> predators_, Prey preys_) {
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
		double[][][][][] N = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] D = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] pi_b = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		int[][][][] pi = new int[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		boolean[][][][][] vst;
		
		Episode episode;
		
		initValues(pi_b, 0.2);
		
		boolean converged = false;
		int i;
		for (i = 0; !converged; i++) {
			converged = false;
			// generate an episode based on policy pi_b
			episode = generateEpisode(pi_b, alpha, gamma, temp);
			int T = episode.length() + 1;
			
			// tao <- latest time at which a_tao != pi(s_tao)
			int tao;
			for (tao = T - 2; tao >= 0; tao--) {
				Predator predator = episode.getPredator(tao);
				Prey prey = episode.getPrey(tao);
				int a = episode.getPredatorAction(tao);
				if (a != pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()]) {
					break;
				}
			}
			
			if (tao < 0) {
				converged = true;
				break;
			}
			
			// Pre-process the production part
			double[] w = new double[T];
			w[T - 1] = 1;
			for (int t = T - 2; t >= 0; t--) {
				Predator predator = episode.getPredator(t);
				Prey prey = episode.getPrey(t);
				int a = episode.getPredatorAction(t);
				w[t] = w[t + 1] / pi_b[predator.getX()][predator.getY()][prey.getX()][prey.getY()][a];
			}
			
			vst = new boolean[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
			
			// For each pair s,a appearing in the episode at time tao or later
			for (int t = tao; t < T - 1; t++) {
				Predator predator = episode.getPredator(t);
				Prey prey = episode.getPrey(t);
				int aPredator = episode.getPredatorAction(t);
				int aPrey = episode.getPreyAction(t);
				
				if (vst[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator]) {
					continue;
				}
				
				// t <- the time of first occurrence of s,a such that t >= tao
				vst[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] = true;
				N[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] += w[t] * getReward(predator, prey, aPredator, aPrey);
				D[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] += w[t];
				Q[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] = N[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] / D[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator];
				
				// Update policy
				if (Q[predator.getX()][predator.getY()][prey.getX()][prey.getY()][aPredator] > Q[predator.getX()][predator.getY()][prey.getX()][prey.getY()][pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()]]) {
					pi[predator.getX()][predator.getY()][prey.getX()][prey.getY()] = aPredator;
					//converged = false;
				}
			}
			printPolicy(pi, this.prey.getX(), this.prey.getY());
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
			//pi_b[nowPredator.getX()][nowPredator.getY()][nowPrey.getX()][nowPrey.getY()][aPredator]
			//		= calcNextValue(pi_b, nowPredator, nowPrey, nextPredator, nextPrey, aPredator, reward, alpha, gamma);
			
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
}
