package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import main.BasicEnvironment;
import static main.BasicEnvironment.WORLDSIZE;

public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public Predator(Agent predator) {
		super(predator);
	}

	public int move(int DIR, Prey prey) {
		move(DIR);

		int reward = 0;

		if (this.equals(prey)) {
			reward += 10;


			prey.setCaught(true);
		}

		return reward;
	}

	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
