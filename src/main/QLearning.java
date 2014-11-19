/**
 * 
 */
package main;

import static main.BasicEnvironment.WORLDSIZE;

import java.util.ArrayList;

import agents.Predator;
import agents.PredatorQL;
import agents.PredatorVI;
import agents.Prey;

/**
 *
 */
public class QLearning extends BasicEnvironment {

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

	private void initValues(double[][][][][] values) {
		final int InitValue = 15;
		for (int px = 0; px < WORLDSIZE; px++)
			for (int py = 0; py < WORLDSIZE; py++)
				for (int x = 0; x < WORLDSIZE; x++)
					for (int y = 0; y < WORLDSIZE; y++)
						for (int m = 0; m < 5; m++) {
							values[px][py][x][y][m] = InitValue;
						}

	}

	public int run() {
		int time = 0;
		// while (preys.size() > 0) {
		// time ++;
		// System.out.print(time);
		//
		// for (Prey prey : preys) {
		// int dir = prey.planMoveRandom(predators);
		// prey.move(dir);
		// System.out.print(" ");
		// prey.print();
		// }
		double[][][][][] Qvalues = new double[WORLDSIZE][WORLDSIZE][WORLDSIZE][WORLDSIZE][5];

		initValues(Qvalues);
		for (int i = 0; i < 100000; i++)
			for (Predator predator : predators) {
				PredatorQL predatorQL = new PredatorQL(predator);
				predatorQL.planEpisodeQL(prey, this, Qvalues);
				// predator.move(dir, preys);
				// System.out.print(" ");
				// predator.print();
			}
		//
		// System.out.println();
		// }

		return time;
	}

	public int movePrey() {
		int a = prey.planMoveRandom(predators);
		prey.move(a);
		return a;
	}
}
