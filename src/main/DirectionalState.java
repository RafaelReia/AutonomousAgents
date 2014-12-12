package main;

import static main.BasicEnvironment.WORLDSIZE;

import java.util.ArrayList;

import agents.Agent;
import agents.Predator;
import agents.Prey;

public class DirectionalState extends State {

	private static final int LEFT = 1;
	private static final int DOWN = 2;
	private static final int RIGHT = 3;
	private static final int UP = 4;

	public DirectionalState(ArrayList<Predator> predators, Prey prey) {
		super(predators, prey);
		this.toRelative();
	}

	public DirectionalState(ArrayList<Pair<Integer, Integer>> positions_) {
		super(positions_);
		// TODO Auto-generated constructor stub
	}

	private void toRelative() {
		dim = dim - 1;
		Pair<Integer, Integer> prey = positions.get(dim);
		positions.remove(dim);

		for (int i = 0; i < dim; i++) {
			positions.set(i, getRelPos(positions.get(i), prey));
		}
	}

	private Pair<Integer, Integer> getRelPos(Pair<Integer, Integer> src,
			Pair<Integer, Integer> tar) {
		Pair<Integer, Integer> rel = new Pair<Integer, Integer>(0, 0);

		rel.a = getDirection(src, tar);

		rel.b = getManhattanDist(src, tar);

		return rel;
	}

	private Integer getManhattanDist(Pair<Integer, Integer> src,
			Pair<Integer, Integer> tar) {
		return Math.abs(src.a - tar.a) + Math.abs(src.b - tar.b);
	}

	private int getDirection(Pair<Integer, Integer> src,
			Pair<Integer, Integer> tar) {

		if ((tar.a - src.a) <= (tar.b - src.b)
				&& (tar.b - src.b) < -(tar.a - src.a))
			return LEFT;
		else if ((tar.a - src.a) >= (tar.b - src.b)
				&& (tar.b - src.b) < -(tar.a - src.a))
			return DOWN;
		else if ((tar.a - src.a) >= (tar.b - src.b)
				&& (tar.b - src.b) > -(tar.a - src.a))
			return RIGHT;
		else if ((tar.a - src.a) <= (tar.b - src.b)
				&& (tar.b - src.b) < -(tar.a - src.a))
			return UP;

		return -1;
	}

}
