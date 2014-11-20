package agents;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Iterator;

import main.BasicEnvironment;
import main.PolicyEvaluation;
import main.PolicyIteration;
import static main.BasicEnvironment.WORLDSIZE;

public class PredatorPI extends Predator {

	public PredatorPI(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public PredatorPI(Agent predator) {
		super(predator);
	}

	private void printPolicy(int[][] policy) {
		for (int x = 0; x < WORLDSIZE; x++) {
			for (int y = 0; y < WORLDSIZE; y++) {
				switch (policy[x][y]) {
				case 0:
					System.out.print(0 + " ");
					break;
				case 1:
					System.out.print("^ ");
					break;
				case 2:
					System.out.print("v ");
					break;
				case 3:
					System.out.print("> ");
					break;
				case 4:
					System.out.print("< ");
					break;
				}
				
			}
			System.out.println();
		}
	}

	public int planMovePI(int[][] policy) {
		return policy[getX()][getY()];
	}	
}