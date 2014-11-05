public class MainPE {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PolicyEvaluation env = new PolicyEvaluation(new Predator(0, 0), new Prey(5, 5));
		env.run();
	}

}
