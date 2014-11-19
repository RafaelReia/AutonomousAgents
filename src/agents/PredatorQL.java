/**
 * 
 */
package agents;

import static main.BasicEnvironment.WORLDSIZE;

import java.util.Random;

import main.QLearning;

/**
 *
 */
public class PredatorQL extends Predator {

	private static final double alpha = 0.1; /* 0.1 until 0.5 */
	private static final double gamma = 0.1; /* 0.1 ,0.5 , 0.7, 0.9 */
	private static final double e = 0.1; /* e-greedy policy */

	/**
	 * @param locX_
	 * @param locY_
	 */
	public PredatorQL(int locX_, int locY_) {
		super(locX_, locY_);
	}

	/**
	 * @param predator
	 */
	public PredatorQL(Agent predator) {
		super(predator);
	}



	private int chooseAction(double[] qvalues) {
		double max = -Double.MAX_VALUE;
		int action = 0;
		Random rdm = new Random();
		if (rdm.nextDouble() > e)
			for (int i = 0; i < DIR_NUM; i++) {
				if (qvalues[i] >= max) {
					action = i;
					max = qvalues[i];
				}
			}
		else {
			// choose any random action for the epsilon part
			return rdm.nextInt(DIR_NUM);
		}
		return action;
	}

	

	public void planEpisodeQL(Prey prey, QLearning env, double[][][][][] Qvalues) {
		/* Initializing the array values */

		int steps = 0;
		boolean isNotTerminal = true;
		while (isNotTerminal) {
			steps++;
			int a = chooseAction(Qvalues[getX()][getY()][prey.getX()][prey
					.getY()]);
			int aPrey = env.movePrey(); //check this if not working XXX
			move(a);
			double reward = env.getReward(this, prey, a, aPrey);
			Qvalues[getX()][getY()][prey.getX()][prey.getY()][a] = calcNextValue(Qvalues,prey, a, reward);
			if(reward > 0){ //check this if not working XXX
				System.out.println("@" + reward + " " + steps);
				isNotTerminal=false;
			}
		}

	}

	private double calcNextValue(double[][][][][] Qvalues, Prey prey, int a, double reward) {
		return Qvalues[getX()][getY()][prey.getX()][prey.getY()][a]
				+ alpha
				* (reward
						+ gamma
						* getMax(Qvalues[getX()][getY()][prey.getX()][prey
								.getY()]) - Qvalues[getX()][getY()][prey.getX()][prey
						.getY()][a]);
	}

	private int getMax(double[] qvalues) {
		double max = -Double.MAX_VALUE;
		int action = 0;
		for (int i = 0; i < DIR_NUM; i++) {
			if (qvalues[i] >= max) {
				action = i;
				max = qvalues[i];
			}
		}
		return action;
	}

}
