package main;

import java.util.ArrayList;

import agents.Predator;
import agents.Prey;

public class NewEnvironment extends BasicEnvironment {

	public NewEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		super(predators_, prey_);
	}

	public int getReward(ArrayList<Predator> predators, Prey prey) {
		for (int i = 0; i < predators.size() - 1; i++)
			for (int j = i + 1; j < predators.size(); j++)
				if (predators.get(i).equals(predators.get(j))) {
					return -10;
				}
		
		for (int i = 0; i < predators.size(); i++) {
			if (predators.get(i).equals(prey)) {
				return 10;
			}
		}
		
		return 0;
	}
	
	public int getReward(ArrayList<Predator> predators, Prey prey, ArrayList<Integer> aPredators, int aPrey) {
		ArrayList<Predator> dummyPredators = new ArrayList<Predator>();
		Prey dummyPrey;
		
		for (int i = 0; i < predators.size(); i++) {
			dummyPredators.add(new Predator(predators.get(i)));
		}
		dummyPrey = new Prey(prey);
		
		for (int i = 0; i < predators.size(); i++) {
			predators.get(i).move(aPredators.get(i));
		}
		dummyPrey.move(aPrey);
		
		return getReward(dummyPredators, dummyPrey);
	}
}
