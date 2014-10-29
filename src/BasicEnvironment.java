import java.util.ArrayList;

public class BasicEnvironment {

	private Cell[][] environment = new Cell[11][11];
	private ArrayList<Animal> agents = new ArrayList<Animal>();

	/**
	 * @param args
	 */


	public BasicEnvironment(ArrayList<Cell> predators, ArrayList<Cell> preys) {
		if (predators == null || preys == null) {
			System.out.println("It's null");
		} else {
			for (Cell object : predators) {
				environment[object.getLocation().getLocationX()][object
						.getLocation().getLocationY()] = object;
				object.setAnimal(new Predator(this));
			}
			for (Cell object : preys) {
				environment[object.getLocation().getLocationX()][object
						.getLocation().getLocationY()] = object;
				object.setAnimal(new Prey(this));
			}
		}

	}

	public void printEnvrionment() {
		System.out.print("  ");
		for (int i = 0; i < 10; i++) {
			System.out.print(i + " ");
		}
		System.out.println(10);
		for(int i = 0; i < 11; i++){
			System.out.print(i + " ");
			for(int j = 0; j<11; j++){
				if(environment[i][j] != null)
					System.out.print(environment[i][j].getAnimal().toString() + " ");
				else{
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}

}
