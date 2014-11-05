package main;
import agents.PredatorVI;
import agents.Prey;

public class MainVI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ValueIteration env = new ValueIteration(new PredatorVI(0, 0), new Prey(5, 5));
		env.run();
	}

}
