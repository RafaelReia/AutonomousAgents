package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import agents.Agent;
import agents.Predator;
import agents.Prey;

public class MultiQlearningEnvironment extends NewEnvironment {

	private static final int N_EPISODES = 100000;

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

	public int episodeQL(QValuesSet qvalues, double alphaPredator,
			double gammaPredator, double epsilonPredator, double alphaPrey, double gammaPrey, double epsilonPrey) {
		/* Initializing the array values */
		int steps = 0;

		ArrayList<Predator> nowPredators = copyPredators(predators);
		Prey nowPrey = new Prey(prey);
		StateRelative nowState = new StateRelative(nowPredators, nowPrey);
		Qvalue nowValue = qvalues.get(nowState);

		while (!Agent.isCaught() && !Agent.isClash()) {
			steps++;

			ArrayList<Predator> nextPredators = copyPredators(nowPredators);
			Prey nextPrey = new Prey(nowPrey);

			ArrayList<Integer> aPredators = nowValue
					.choosePredatorActions(epsilonPredator);
			int aPrey = nowValue.choosePreyActions(epsilonPrey);
			aPrey = nextPrey.actionAfterTrip(aPrey);

			for (int i = 0; i < nextPredators.size(); i++) {
				nextPredators.get(i).move(aPredators.get(i));
			}
			nextPrey.move(aPrey);
			StateRelative nextState = new StateRelative(nextPredators, nextPrey);
			Qvalue nextValue = qvalues.get(nextState);

			// System.out.print(steps + " ");
			// nextPrey.print();
			// for (int i = 0; i < nextPredators.size(); i++) {
			// System.out.print(" ");
			// nextPredators.get(i).print();
			// }
			// System.out.println();

			Pair<Integer, Integer> reward = getReward(nextPredators, nextPrey);
			int rewardPredator = reward.a;
			int rewardPrey = reward.b;

			testEnd(nextPredators, nextPrey);
			if (Agent.isCaught() || Agent.isClash()) {
				nextValue = new Qvalue(0,0,nextPredators.size());
			}

			double predatorQSA = nowValue.getPredatorValue(aPredators);
			double preyQSA = nowValue.getPreyValue(aPrey);

			double predatorMaxQS_b = nextValue.getMaxPredatorValue();
			double preyMaxQS_b = nextValue.getMaxPreyValue();

			predatorQSA += alphaPredator
					* (rewardPredator + gammaPredator * predatorMaxQS_b - predatorQSA);
			preyQSA += alphaPrey
					* (rewardPrey + gammaPrey * preyMaxQS_b - preyQSA);

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

	public int test(double alphaPredator, double gammaPredator,
			double epsilonPredator, double initValuePredator, double alphaPrey,
			double gammaPrey, double epsilonPrey, double initValuePrey) {
		int time = 0;
		QValuesSet Qvalues = new QValuesSet(initValuePredator, initValuePrey,
				predators.size());
		int aux;
		runs++;
		for (int i = 0; i < N_EPISODES; i++) {
			aux = episodeQL(Qvalues, alphaPredator, gammaPredator,
					epsilonPredator,alphaPrey, gammaPrey,
					epsilonPrey);
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
						parameterSettings[ps][2], parameterSettings[ps][3],
						parameterSettings[ps][4], parameterSettings[ps][5],
						parameterSettings[ps][6], parameterSettings[ps][7]);
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
