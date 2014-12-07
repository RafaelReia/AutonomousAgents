package main;

import java.util.ArrayList;

import static main.BasicEnvironment.WORLDSIZE;
import agents.Predator;
import agents.Prey;

public class StateRelative extends State {

	public StateRelative(ArrayList<Predator> predators, Prey prey) {
		super(predators, prey);
		this.toRelative();
	}
	
	private void toRelative() {
		dim = dim - 1;
		Pair<Integer, Integer> prey = positions.get(dim);
		positions.remove(dim);
		
		for (int i = 0; i < dim; i++) {
			positions.set(i, getRelPos(positions.get(i), prey));
		}
	}
	
	private Pair<Integer, Integer> getRelPos(Pair<Integer, Integer> src, Pair<Integer, Integer> tar) {
		Pair<Integer, Integer> rel= new Pair<Integer, Integer>(0, 0);
		
		int d1 = src.a - tar.a;
		int d2 = d1 - (int)Math.signum(d1) * WORLDSIZE;
		rel.a = Math.abs(d1) < Math.abs(d2) ? d1 : d2;
		
		d1 = src.b - tar.b;
		d2 = d1 - (int)Math.signum(d1) * WORLDSIZE;
		rel.b = Math.abs(d1) < Math.abs(d2) ? d1 : d2;
		
		return rel;
	}
}
