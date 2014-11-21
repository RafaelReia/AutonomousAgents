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
	private static final double gamma = 0.9; /* 0.1 ,0.5 , 0.7, 0.9 */
	private static double e = 0.2; /* e-greedy policy */

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
		e = 0.1;
		int steps = 0;
		boolean isNotTerminal = true;
		while (isNotTerminal) {
			steps++;
			
			PredatorQL lastPredator = new PredatorQL(this);
			Prey lastPrey = new Prey(env.getPreys());
			
			int a = chooseAction(Qvalues[getX()][getY()][env.getPreys().getX()][env.getPreys().getY()]);
			int aPrey = env.movePrey(); //check this if not working XXX

			move(a);
			
//			this.print();
//			System.out.print(" ");
//			env.getPreys().print();
//			System.out.println();
			
			//prey.print();
			//this.print();
			//System.out.println(steps);
			double reward = env.getReward(this, env.getPreys());
			Qvalues[lastPredator.getX()][lastPredator.getY()][lastPrey.getX()][lastPrey.getY()][a] = calcNextValue(Qvalues, lastPredator, lastPrey, this, env.getPreys(), a, reward);
			if(reward > 0){ // if reward is not zero... XXX check this if not working
				System.out.println(/*"@" + reward + " " + */steps);
				isNotTerminal=false;
			}
		}

	}

	private double calcNextValue(double[][][][][] Qvalues, PredatorQL lastPredator, Prey lastPrey, PredatorQL predator, Prey prey, int a, double reward) {
		return Qvalues[lastPredator.getX()][lastPredator.getY()][lastPrey.getX()][lastPrey.getY()][a]
				+ alpha
				* (reward
						+ gamma
						* getMax(Qvalues[predator.getX()][predator.getY()][prey.getX()][prey
								.getY()]) - Qvalues[lastPredator.getX()][lastPredator.getY()][lastPrey.getX()][lastPrey
						.getY()][a]);
	}

	private double getMax(double[] qvalues) {
		double max = -Double.MAX_VALUE;
		int action = 0;
		for (int i = 0; i < DIR_NUM; i++) {
			if (qvalues[i] >= max) {
				action = i;
				max = qvalues[i];
			}
		}
		return max;
	}

}
