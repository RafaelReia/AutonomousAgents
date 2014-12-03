package main;

import java.util.ArrayList;

import agents.Predator;
import agents.Prey;

public class NewEnvironment extends BasicEnvironment {

	public NewEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		super(predators_, prey_);
	}

	public Pair<Integer, Integer> getReward(ArrayList<Predator> predators, Prey prey) {
		for (int i = 0; i < predators.size() - 1; i++)
			for (int j = i + 1; j < predators.size(); j++)
				if (predators.get(i).equals(predators.get(j))) {
					return new Pair<Integer,Integer>(10,-10);
				}
		
		for (int i = 0; i < predators.size(); i++) {
			if (predators.get(i).equals(prey)) {
				return  new Pair<Integer,Integer>(-10,10);
			}
		}
		
		return  new Pair<Integer,Integer>(0,0);
	}
	
	public Pair<Integer, Integer> getReward(ArrayList<Predator> predators, Prey prey, ArrayList<Integer> aPredators, int aPrey) {
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
