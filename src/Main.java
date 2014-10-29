import java.util.ArrayList;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Cell> predators = new ArrayList<Cell>();
		ArrayList<Cell> preys = new ArrayList<Cell>();
		predators.add(new Cell(0, 0));
		predators.add(new Cell(1, 3));
		predators.add(new Cell(1, 2));
		preys.add(new Cell(2, 3));
		BasicEnvironment env = new BasicEnvironment(predators, preys);
		env.printEnvrionment();
	}

}
