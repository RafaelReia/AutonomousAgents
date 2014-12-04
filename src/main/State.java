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
	public int compareTo(State o) {
		int cmp = 0;
		for (int i = 0; i < dim; i++) {
			cmp = this.positions.get(i).compareTo(o.positions.get(i));
			if (cmp != 0)
				break;
		}
		return cmp;
	}

}
