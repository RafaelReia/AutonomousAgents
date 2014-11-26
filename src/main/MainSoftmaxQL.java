package main;

import agents.Predator;
import agents.Prey;

public class MainSoftmaxQL {

	public static void main(String[] args) {
		
		SoftmaxQLearning env = new SoftmaxQLearning(new Predator(0, 0),
				new Prey(5, 5));
		env.run();
	}
}
