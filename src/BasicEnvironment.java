import java.util.ArrayList;
import java.util.Arrays;

public class BasicEnvironment {

	//private Cell[][] environment = new Cell[11][11];
	//private ArrayList<Animal> agents = new ArrayList<Animal>();
	ArrayList<Predator> predators = new ArrayList<Predator>();
	ArrayList<Prey> preys = new ArrayList<Prey>();
	
	
	public BasicEnvironment(Predator predator_, Prey prey_) {
		this(new ArrayList<Predator>(Arrays.asList(predator_)), new ArrayList<Prey>(Arrays.asList(prey_)));
	}
	
	public BasicEnvironment(ArrayList<Predator> predators_, ArrayList<Prey> preys_) {
		if (predators == null || preys == null) {
			System.out.println("It's null");
			return;
		}
		
		predators.clear();
		preys.clear();
		
		System.out.print(0);
		
		for (Prey prey : preys_) {
			preys.add(new Prey(prey));
			System.out.print(" ");
			prey.print();
		}
		
		for (Predator predator : predators_) {
			predators.add(new Predator(predator));
			System.out.print(" ");
			predator.print();
		}
		
		System.out.println();
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
				int dir = predator.planMoveRandom();
				predator.move(dir, preys);
				System.out.print(" ");
				predator.print();
			}

			System.out.println();
		}
		
		return time;
	}

	public void printEnvrionment() {
//		System.out.print("  ");
//		for (int i = 0; i < 10; i++) {
//			System.out.print(i + " ");
//		}
//		System.out.println(10);
//		for(int i = 0; i < 11; i++){
//			System.out.print(i + " ");
//			for(int j = 0; j<11; j++){
//				if(environment[i][j] != null)
//					System.out.print(environment[i][j].getAnimal().toString() + " ");
//				else{
//					System.out.print("  ");
//				}
//			}
//			System.out.println();
//		}
	}

}
