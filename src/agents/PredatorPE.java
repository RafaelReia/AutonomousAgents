package agents;

import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPE extends Predator {

	public PredatorPE(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPE(Agent predator) {
		super(predator);
	}


	public int planMovePE(ArrayList<Prey> preys) {
		return 0;
	}

	
}
