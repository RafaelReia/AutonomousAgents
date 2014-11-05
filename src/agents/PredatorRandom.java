package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorRandom extends Predator {

	public PredatorRandom(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorRandom(Agent predator) {
		super(predator);
	}


	public int planMoveRandom() {
		return (int) Math.floor(Math.random() * DIR_NUM);
	}

	
	
}
