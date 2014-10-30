public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicEnvironment env = new BasicEnvironment(new Predator(0, 0), new Prey(5, 5));
		env.run();
	}

}
