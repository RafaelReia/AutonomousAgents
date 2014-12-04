package main;

import java.util.ArrayList;

import agents.Predator;
import agents.Prey;

public class State implements Comparable<State>{
	public int dim;
	public ArrayList<Pair<Integer, Integer>> positions;
	
	
	
	public State(ArrayList<Predator> predators, Prey prey) {
		this.dim = predators.size() + 1;
		positions = new ArrayList<Pair<Integer, Integer>>(dim);

		for (Predator p : predators) {
			positions.add(new Pair<Integer, Integer>(p.getX(), p.getY()));
		}
		positions.add(new Pair<Integer, Integer>(prey.getX(), prey.getY()));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ArrayList<?>) {
			ArrayList<Pair<Integer, Integer>> aux = (ArrayList<Pair<Integer, Integer>>) obj;
			for (int i=0;i<dim;i++) {
				aux.get(i).equals(positions.get(i));
			}
		}
		return false;

	}

	@Override
	public int compareTo(State o) {
		return this.equals(o)?0:-1;
	}

}
