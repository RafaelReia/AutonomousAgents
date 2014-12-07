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

public class MultiQlearningEnvironment extends NewEnvironment {

	private static final int N_EPISODES = 10000;

	public MultiQlearningEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		super(predators_, prey_);
	}

	private ArrayList<Predator> copyPredators(ArrayList<Predator> o) {
		ArrayList<Predator> t = new ArrayList<Predator>();
		for (Predator p : o) {
			t.add(new Predator(p));
		}
		return t;
	}
	
	public int episodeQL(QValuesSet qvalues, double alpha, double gamma,
			double epsilon) {
		/* Initializing the array values */
		int steps = 0;

		ArrayList<Predator> nowPredators = copyPredators(predators);
		Prey nowPrey = new Prey(prey);
		State nowState = new State(nowPredators, nowPrey);
		Qvalue nowValue = qvalues.get(nowState);

		while (!Agent.isCaught() && !Agent.isClash()) {
			steps++;

			ArrayList<Predator> nextPredators = copyPredators(nowPredators);
			Prey nextPrey = new Prey(nowPrey);
			
			ArrayList<Integer> aPredators = nowValue.choosePredatorActions(epsilon);
			int aPrey = nowValue.choosePreyActions(epsilon);
			aPrey = nextPrey.actionAfterTrip(aPrey);

			for (int i = 0; i < nextPredators.size(); i++) {
				nextPredators.get(i).move(aPredators.get(i));
			}
			nextPrey.move(aPrey);
			State nextState = new State(nextPredators, nextPrey);
			Qvalue nextValue = qvalues.get(nextState);
			
			System.out.print(steps + " ");
			nextPrey.print();
			for (int i = 0; i < nextPredators.size(); i++) {
				System.out.print(" ");
				nextPredators.get(i).print();
			}
			System.out.println();
			
			Pair<Integer, Integer> reward = getReward(nextPredators, nextPrey);
			testEnd(nextPredators, nextPrey);
			int rewardPredator = reward.a;
			int rewardPrey = reward.b;
			
			double predatorQSA = nowValue.getPredatorValue(aPredators);
			double preyQSA = nowValue.getPreyValue(aPrey);
			
			double predatorMaxQS_b = nextValue.getMaxPredatorValue();
			double preyMaxQS_b = nextValue.getMaxPreyValue();
			
			predatorQSA += alpha * (rewardPredator + gamma * predatorMaxQS_b - predatorQSA);
			preyQSA += alpha * (rewardPrey + gamma * preyMaxQS_b - preyQSA);
			
			nowValue.setPredatorValue(aPredators, predatorQSA);
			nowValue.setPreyValue(aPrey, preyQSA);
			
			qvalues.put(nowState, nowValue);

			nowPredators = nextPredators;
			nowPrey = nextPrey;
			nowState = nextState;
			nowValue = nextValue;
		}

		return Agent.isCaught() ? steps : -steps;
	}

	int runs;
	int[] steps = new int[N_EPISODES];

	public int test(double alpha, double gamma, double epsilon, double initValue) {
		int time = 0;
		QValuesSet Qvalues = new QValuesSet(initValue, predators.size());
		int aux;
		runs++;
		for (int i = 0; i < N_EPISODES; i++) {
			aux = episodeQL(Qvalues, alpha, gamma, epsilon);
			System.out.println("episode: " + i + ", steps: " + aux);
			steps[i] += aux;
		}

		return time;
	}

	public int run(double[][] parameterSettings) {
		// For every parameter setting
		for (int ps = 0; ps < parameterSettings.length; ps++) {
			for (int i = 0; i < 1; i++) {
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
