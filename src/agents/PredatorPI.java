package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPI extends Predator {

	public PredatorPI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPI(Agent predator) {
		super(predator);
	}


	public int planMovePI(ArrayList<Prey> preys) {
		return 0;
	}

	
}
