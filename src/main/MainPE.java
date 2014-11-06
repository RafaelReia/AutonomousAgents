package main;
import agents.PredatorPE;
import agents.Prey;

public class MainPE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PolicyEvaluation env = new PolicyEvaluation(new PredatorPE(2, 3), new Prey(2, 2));
		env.run();
	}

}
