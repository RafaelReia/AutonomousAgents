package main;

import java.io.IOException;
import java.util.ArrayList;

import org.jfree.data.time.TimeSeries;

import agents.Agent;
import agents.Predator;
import agents.Prey;

public class NewEnvironment extends BasicEnvironment {

	public NewEnvironment(ArrayList<Predator> predators_, Prey prey_) {
		super(predators_, prey_);
	}

	public Pair<Integer, Integer> getReward(ArrayList<Predator> predators, Prey prey) {
		for (int i = 0; i < predators.size() - 1; i++)
			for (int j = i + 1; j < predators.size(); j++)
				if (predators.get(i).equals(predators.get(j))) {
					Agent.setClash();
					return new Pair<Integer,Integer>(-10,10);
				}
		
		for (int i = 0; i < predators.size(); i++) {
			if (predators.get(i).equals(prey)) {
				Agent.setCaught();
				return new Pair<Integer,Integer>(10,-10);
			}
		}
		
		return new Pair<Integer,Integer>(0,0);
	}
	public double getRewardMiniMax(Agent nowPlayer, Agent nowOpponent,
			int aPlayer, int aOpponent) {
		if(nowPlayer instanceof Predator)
			return getRewardMiniMax((Predator)nowPlayer, (Prey)nowOpponent, aPlayer, aOpponent);
		return getRewardMiniMax((Prey)nowPlayer, (Predator)nowOpponent, aPlayer, aOpponent);

	}
	public double getRewardMiniMax(Predator predator, Prey prey, int aPredator, int aPrey){
		Prey dummyPrey = new Prey(prey);
		Predator dummyPredator = new Predator(predator);
		dummyPredator.move(aPredator);
		dummyPrey.move(aPrey);

		if (dummyPredator.equals(dummyPrey)) {
			/*System.err.println("Reward!! ");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Agent.setCaught(); //XXX */
			return 10;
		}
		return 0;
	}
	public double getRewardMiniMax(Prey prey, Predator predator, int aPrey, int aPredator){
		Prey dummyPrey = new Prey(prey);
		Predator dummyPredator = new Predator(predator);
		dummyPredator.move(aPredator);
		dummyPrey.move(aPrey);
		if (dummyPredator.equals(dummyPrey)) {
			/*System.err.println("Reward!! PREY");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Agent.setCaught(); //XXX */
			return -10;
		}
		return 0;
	}
	
	
	public Pair<Integer, Integer> getReward(ArrayList<Predator> predators, Prey prey, ArrayList<Integer> aPredators, int aPrey) {
		ArrayList<Predator> dummyPredators = new ArrayList<Predator>();
		Prey dummyPrey;
		
		for (int i = 0; i < predators.size(); i++) {
			dummyPredators.add(new Predator(predators.get(i)));
		}
		dummyPrey = new Prey(prey);
		
		for (int i = 0; i < predators.size(); i++) {
			predators.get(i).move(aPredators.get(i));
		}
		dummyPrey.move(aPrey);
		
		return getReward(dummyPredators, dummyPrey);
	}
	
	public void testEnd(ArrayList<Predator> predators, Prey prey) {
		for (int i = 0; i < predators.size() - 1; i++)
			for (int j = i + 1; j < predators.size(); j++)
				if (predators.get(i).equals(predators.get(j))) {
					Agent.setClash();
					return;
				}
		
		for (int i = 0; i < predators.size(); i++) {
			if (predators.get(i).equals(prey)) {
				Agent.setCaught();
				return;
			}
		}
		return;
	}
	
	public void testEnd(ArrayList<Predator> predators, Prey prey, ArrayList<Integer> aPredators, int aPrey) {
		ArrayList<Predator> dummyPredators = new ArrayList<Predator>();
		Prey dummyPrey;
		
		for (int i = 0; i < predators.size(); i++) {
			dummyPredators.add(new Predator(predators.get(i)));
		}
		dummyPrey = new Prey(prey);
		
		for (int i = 0; i < predators.size(); i++) {
			predators.get(i).move(aPredators.get(i));
		}
		dummyPrey.move(aPrey);
		
		testEnd(dummyPredators, dummyPrey);
		return;
	}
	
	public int run() {
		int times = 0;
		while (!Agent.isCaught()) {
			times++;
			ArrayList<Integer> aPredators = new ArrayList<Integer>();
			int aPrey;
			
			while (true) {
				for (int i = 0; i < predators.size(); i++) {
					aPredators.add((int)(Math.random() * 5));
				}
				aPrey = (int)(Math.random() * 5);
				aPrey = prey.actionAfterTrip(aPrey);
				
				if (getReward(predators, prey, aPredators, aPrey).a >= 0) {
					break;
				}
			}
			
			for (int i = 0; i < predators.size(); i++) {
				predators.get(i).move(aPredators.get(i));
			}
			prey.move(aPrey);
			
			testEnd(predators, prey);
		}
		return times;
	}
}
