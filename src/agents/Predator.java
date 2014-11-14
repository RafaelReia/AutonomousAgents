package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import main.BasicEnvironment;
import static main.BasicEnvironment.WORLDSIZE;

public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
	}

	public Predator(Agent predator) {
		super(predator);
	}

	Integer maxArg = 0; // This is the ouput variable for maxAction

	// Helper function which loops over actions to look for action
	// with highest output according to value iteration.
	// maxValue is that value, maxArg is the corresponding action
	public double maxAction(double[][][][] values, int posX, int posY,
			int px, int py, Prey prey, BasicEnvironment env) {

		double highestOutput = -Double.MAX_VALUE;
		ArrayList<Integer> bestDirection = new ArrayList<Integer>();
		double gamma = 0.9; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			
			
			double output = 0.0;
			
			for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
				Prey dummyPrey = new Prey(px, py);
				Predator dummyPredator = new Predator(posX, posY);
				double actionReward = env.getReward(dummyPredator,prey,a,aPrey);
				dummyPredator.move(a);
				dummyPrey.move(aPrey);
				
				double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
				output += prey.prob(env.getPredators() ,aPrey)*(actionReward + gamma * value);
			}
			if (output >= highestOutput) {
				highestOutput = output;
				bestDirection.add(a);
			}

		}
		
		//if(highestOutput>0)
			//System.out.println("ITS GREATER THAT HOOOHHOHOHOHOHOHOH!!!! " + highestOutput);
		
		Random rdm = new Random();
		maxArg = bestDirection.get(rdm.nextInt(bestDirection.size()));

		return highestOutput;
	}

	private double prob(int a) {
		return 0.2;
	}

	/*
	 * Helper function which loops over actions to and sum the values of all
	 * possible actions from that state. Output is the sum.
	 */
	public double sumActions(double[][][][] values, int posX, int posY,
			int px, int py, Prey prey, BasicEnvironment env) {

		double output = 0.0;
		int bestDirection = 0;
		double gamma = 0.8; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			
			for (int aPrey = 0; aPrey < DIR_NUM; aPrey++) {
				Predator dummyPredator = new Predator(posX, posY);
				Prey dummyPrey = new Prey(px, py);
				
				double actionReward = env.getReward(dummyPredator,dummyPrey,a,aPrey);

				dummyPredator.move(a);
				dummyPrey.move(aPrey);
				
				double value = values[dummyPredator.getX()][dummyPredator.getY()][dummyPrey.getX()][dummyPrey.getY()];
				output += this.prob(a) * prey.prob(env.getPredators() ,aPrey)*(actionReward + gamma * value);
				//if(output >0)
					//System.out.println("SOMETHING BIIIIIIIIG!!! "+output);
				//System.out.println("Predator Prob " + this.prob(a) + "  Prey Prob" + prey.prob(env.getPredators() ,aPrey) + " Action Re" + actionReward);
			}

		}
		
		//if(output>0)
			//System.out.println("ITS GREATER THAT ZEROOOO!!!! " + output);
		maxArg = bestDirection;

		return output;
	}

	
	/*
	 * Helper function which loops over actions to and sum the values of all
	 * possible actions from that state. Output is the sum.
	 *
	public double sumActions2(double[][] values, int posX, int posY,
			ArrayList<Prey> preys) {

		double Output = 0.0;
		int bestDirection = 0;
		double gamma = 0.5; // TODO

		for (int a = 0; a < DIR_NUM; a++) {

			// Calculate reward of taking action a to go to s'
			// Calculate value of s'
			Predator dummyP = new Predator(posX, posY);
			double actionReward = dummyP.getReward(a, preys);

			double value = values[dummyP.getX()][dummyP.getY()];
			Output += (actionReward + gamma * value);

		}
		maxArg = bestDirection;

		return Output;
	}*/
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


	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
