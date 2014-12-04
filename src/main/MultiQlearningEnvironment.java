package main;

import static agents.Agent.DIR_NUM;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import agents.Agent;
import agents.Predator;
import agents.Prey;

public class MultiQlearningEnvironment extends BasicEnvironment {

	private static final int N_EPISODES = 10000;

	public MultiQlearningEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		super(predators_, prey_);
	}

	private int chooseAction(LinkedList<Double> linkedList, double epsilon) {
		double max = -Double.MAX_VALUE;
		Random rdm = new Random();
		ArrayList<Integer> cand = new ArrayList<Integer>();
		if (rdm.nextDouble() > epsilon)
			for (int i = 0; i < DIR_NUM; i++) {
				if (linkedList.get(i) > max) {
					cand.clear();
					max = linkedList.get(i);
				}
				if (linkedList.get(i) == max) {
					cand.add(i);
				}
			}
		else {
			// choose any random action for the epsilon part
			return rdm.nextInt(DIR_NUM);
		}
		return cand.get(rdm.nextInt(cand.size()));
	}

	public int episodeQL(QValuesSet qvalues, double alpha, double gamma,
			double epsilon) {
		/* Initializing the array values */
		int steps = 0;

		Predator nowPredator = new Predator(predators.get(0));
		Prey nowPrey = new Prey(prey);

		while (!Agent.isEnd()) {
			steps++;

			Predator nextPredator = new Predator(nowPredator);
			Prey nextPrey = new Prey(nowPrey);

			int aPredator = chooseAction(qvalues.get(predators, prey), epsilon);
			int aPrey = nowPrey.planMoveRandom(nowPredator); // check this if
																// not working
																// XXX

			nextPrey.move(aPrey);
			nextPredator.move(aPredator, nextPrey);

			double reward = getReward(nextPredator, nextPrey);
			qvalues.set(
					predators,
					prey,
					aPredator,
					calcNextValue(qvalues, nowPredator, nowPrey, nextPredator,
							nextPrey, aPredator, reward, alpha, gamma));

			nowPredator = nextPredator;
			nowPrey = nextPrey;
		}

		return steps;
	}

	private double calcNextValue(QValuesSet qvalues, Predator nowPredator,
			Prey nowPrey, Predator nextPredator, Prey nextPrey, int aPredator,
			double reward, double alpha, double gamma) {
		return qvalues.get(predators, prey, aPredator)
				+ alpha
				* (reward + gamma * getMax(qvalues.get(predators, prey)) - qvalues
						.get(predators, prey, aPredator));
	}

	private double getMax(LinkedList<Double> linkedList) {
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < DIR_NUM; i++) {
			if (linkedList.get(i) >= max) {
				max = linkedList.get(i);
			}
		}
		return max;
	}

	int runs;
	int[] steps = new int[N_EPISODES];

	public int test(double alpha, double gamma, double epsilon, double initValue) {
		int time = 0;
		QValuesSet Qvalues = new QValuesSet(initValue, 5);
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
