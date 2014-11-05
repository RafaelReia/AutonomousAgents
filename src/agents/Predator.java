package agents;
import java.util.ArrayList;
import java.util.Iterator;

import static main.BasicEnvironment.WORLDSIZE;


public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
	}
	
	public Predator(Agent predator) {
		super(predator);
	}
	
	// Helper function which loops over actions to look for action
	// with highest output according to value iteration.
	// maxValue is that value, maxArg is the corresponding action
	public void maxAction(double[][] values, int posX, int posY, Double maxValue, Integer maxArg, ArrayList<Prey> preys)
	{
		
		double highestOutput = 0.0;
		int bestDireciton;
		
		for (int a = 0; a < DIR_NUM; a++)
		{
			
			
			// Calculate reward of taking action a to go to s'
			double actionReward = getReward(a,posX,posY, preys);
			// Calculate value of s'
			double value = values[dirX[a]][dirY[a]];
			double output = actionReward + gamma * value;
			
			// Save output if higher than of previous actions
			if (output > highestOutput)
			{
				highestOutput = output;
				bestDirection = a;
			}
			
		}
		
		maxValue = highestOutput;
		maxArg = bestDirection;
	}
	
	public int move(int DIR, ArrayList<Prey> preys) {
		move(DIR);
		
		int reward = 0;
		for (Iterator<Prey> iterator = preys.iterator(); iterator.hasNext();) {
			if (this.equals(iterator.next())) {
				reward += 10;
				iterator.remove();
			}
		}
		
		return reward;
	}
	
	// Get reward for a certain field, without really moving into that direction
	public int getReward(int DIR, int posX, int posY, ArrayList<Prey> preys) {
		// Create dummy predator with same location as current predator
		Predator dummy = new Predator(posX, posY);
		dummy.move(DIR);
		
		// Move dummy predator
		int reward = 0;
		for (Iterator<Prey> iterator = preys.iterator(); iterator.hasNext();) {
			if (this.equals(iterator.next())) {
				reward += 10;
			}
		}
		return reward;
	}
	
	public int planMoveRandom() {
		return (int) Math.floor(Math.random() * DIR_NUM);
	}
	
	public int planMoveVI(ArrayList<Prey> preys)
	{
		
		// Create array which stores values for all fields in the grid
		// values array is initialized: v=0 for every state
		double[][] values = new double[WORLDSIZE][WORLDSIZE];
		
		double delta = 0.0;
		double theta = 0.1;
		// Repeat untill values have converged
		while (delta < theta)
		{
			delta = 0.0;
			for (int x = 0; x < WORLDSIZE; x++)
			{
				for (int y = 0; y < WORLDSIZE; y++)
				{
					double temp = values[x][y];
					
					Double maxValue;
					Integer maxArg;
					maxAction(values, x, y, maxValue, maxArg, preys);
					values[x][y] = maxValue;
					// Updating delta, which is used for the convergence check
					delta = Math.max(delta, Math.abs(temp-values[x][y]));
				}
			}	
		}
		/* 
			Repeat
				∆ ← 0
				For each s ∈ S:
					temp ← v(s)
					v(s) ← max a s p(s |s, a)[r(s, a, s ) + γv(s )]
					∆ ← max(∆, |temp − v(s)|)
			until ∆ < θ (a small positive number)
			
			
			Output a deterministic policy, π, such that
			π(s) = arg max a SUM p(s |s, a) r(s, a, s ) + γv(s )
		 * 
		 */
		 
		 // Output best action given the current value field
		 Double maxValue;
		 Integer maxArg;
		 maxAction(values, maxValue, maxArg, preys);
		 return maxArg;
	}
	
	public int planMovePI()
	{
		return 0;
	}
	
	public int planMovePE()
	{
		return 0;
	}
	
	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
