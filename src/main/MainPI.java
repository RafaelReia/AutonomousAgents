package main;

import agents.PredatorPE;
import agents.PredatorPI;
import agents.Prey;

public class MainPI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PolicyIteration env = new PolicyIteration(new PredatorPI(0, 0),
				new Prey(5, 5));
		env.run();
//		env = new PolicyIteration(new PredatorPI(0, 0), new Prey(5, 5));
//		env.run();
//		env = new PolicyIteration(new PredatorPI(2, 3), new Prey(5, 4));
//		env.run();
//		env = new PolicyIteration(new PredatorPI(2, 10), new Prey(10, 10));
//		env.run();
//		env = new PolicyIteration(new PredatorPI(10, 10), new Prey(0, 0));
//		env.run();
	}

}
