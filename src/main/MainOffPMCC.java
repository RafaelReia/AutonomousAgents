package main;


import org.jfree.ui.RefineryUtilities;
import agents.Predator;
import agents.Prey;

public class MainOffPMCC {

	public static void main(String[] args) {
		
		OffPMCC env = new OffPMCC(new Predator(0, 0),
				new Prey(5, 5));
		double[][] parameterSettings = {{0.5,0.9,10}};
		env.run(parameterSettings);
		// Show chart just generated by QLearning
		final Chart c = new Chart("QLearning",parameterSettings);
        c.pack();
        RefineryUtilities.centerFrameOnScreen(c);
        c.setVisible(true);
	}
}
