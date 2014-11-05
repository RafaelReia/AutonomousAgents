package main;
import agents.Prey;

public class MainPI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PolicyIteration env = new PolicyIteration(new Predator(0, 0), new Prey(5, 5));
		env.run();
	}

}
