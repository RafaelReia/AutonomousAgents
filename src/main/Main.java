package main;
import agents.PredatorRandom;
import agents.Prey;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicEnvironment env = new BasicEnvironment(new PredatorRandom(0, 0), new Prey(5, 5));
		env.run();
	}

}
