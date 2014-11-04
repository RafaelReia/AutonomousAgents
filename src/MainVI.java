public class MainVI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ValueIteration env = new ValueIteration(new Predator(0, 0), new Prey(5, 5));
		env.run();
	}

}
