package main;

import agents.Predator;
import agents.Prey;

public class MainQL {

	public static void main(String[] args) {
		
		QLearning env = new QLearning(new Predator(0, 0),
				new Prey(5, 5));
		env.run();
	}
}
