import java.util.ArrayList;
import java.util.Arrays;

public class PolicyIteration extends BasicEnvironment{
	
	// Constructur for one prey and one predator
	public PolicyIteration(Predator predator_, Prey prey_)
	{
		super(predator_, prey_);
	}
	
	// Constructor for multiple predators/preys
	public PolicyIteration(ArrayList<Predator> predators_, ArrayList<Prey> preys_)
	{
		super(predators_, preys_);
	}
	
	public int run() {
		int time = 0;
		while (preys.size() > 0) {
			time ++;
			System.out.print(time);
			
			for (Prey prey : preys) {
				int dir = prey.planMoveRandom(predators);
				prey.move(dir);
				System.out.print(" ");
				prey.print();
			}
			
			for (Predator predator : predators) {
				int dir = predator.planMovePI();
				predator.move(dir, preys);
				System.out.print(" ");
				predator.print();
			}

			System.out.println();
		}
		
		return time;
	}



}
