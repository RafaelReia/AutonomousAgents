package main;

import agents.PredatorPE;
import agents.Prey;

public class MainPE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Case 1:");
		PolicyEvaluation env = new PolicyEvaluation(new PredatorPE(0, 0),
				new Prey(5, 5));
		env.run();
		System.out.println("\nCase 2:");
		env = new PolicyEvaluation(new PredatorPE(2, 3),
				new Prey(5, 4));
		env.run();
		System.out.println("\nCase 3:");
		env = new PolicyEvaluation(new PredatorPE(2, 10),
				new Prey(10, 0));
		env.run();
		System.out.println("\nCase 4:");
		env = new PolicyEvaluation(new PredatorPE(10, 10),
				new Prey(0, 0));
		env.run();
	}

}
