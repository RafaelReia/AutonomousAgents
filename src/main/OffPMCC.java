package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import agents.Episode;
import agents.Predator;
import agents.Prey;
import static agents.Agent.DIR_NUM;

public class OffPMCC extends BasicEnvironment {
	private static final int N_EPISODES = 10000;

	int runs;
	int [] steps = new int[N_EPISODES];
	
	/**
	 * @param predator_
	 * @param prey_
	 */
	public OffPMCC(Predator predator_, Prey prey_) {
		super(predator_, prey_);
	}

	/**
	 * @param predators_
	 * @param preys_
	 */
	public OffPMCC(ArrayList<Predator> predators_, Prey preys_) {
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
	
	public void run(double[][] parameterSettings) {
		// For every parameter setting
		for (int ps = 0; ps < parameterSettings.length;ps++)
		{
			for(int i = 0;i<100;i++)
			{
				test(parameterSettings[ps][0],parameterSettings[ps][1],parameterSettings[ps][2]);
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
	}
	
	public void test(double alpha, double gamma, double temp) {
		double[][][][][] Q = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] N = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] D = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		double[][][][][] pi_b = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][DIR_NUM];
		int[][][][] pi = new int[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE];
		boolean[][][][][] vst;
		
		Episode episode;
		
		initValues(pi_b, 0.2);
		
		for (int i = 0; i < N_EPISODES; i++) {
			// generate an episode based on policy pi_b
			episode = generateEpisode(pi_b);
			int T = episode.length() + 1;
			
			// tao <- latest time at which a_tao != pi(s_tao)
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
				}
			}
		}
	}	
	
}
