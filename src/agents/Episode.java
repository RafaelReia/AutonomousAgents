package agents;

import java.util.ArrayList;

public class Episode {
	ArrayList<Predator> sPredators;
	ArrayList<Prey> sPreys;
	ArrayList<Integer> aPredators;
	ArrayList<Integer> aPreys;
	
	public Episode() {
		sPredators = new ArrayList<Predator>();
		sPreys = new ArrayList<Prey>();
		aPredators = new ArrayList<Integer>();
		aPreys = new ArrayList<Integer>();
	}
	
	public void clear() {
		sPredators = new ArrayList<Predator>();
		sPreys = new ArrayList<Prey>();
		aPredators = new ArrayList<Integer>();
		aPreys = new ArrayList<Integer>();
	}
	
	public int length() {
		return sPredators.size();
	}
	
	public void append(Predator predator, Prey prey, int aPredator, int aPrey) {
		sPredators.add(new Predator(predator));
		sPreys.add(new Prey(prey));
		aPredators.add(aPredator);
		aPreys.add(aPrey);
	}
	
	public Predator getPredator(int idx) {
		return new Predator(sPredators.get(idx));
	}
	
	public Prey getPrey(int idx) {
		return new Prey(sPreys.get(idx));
	}
	
	public int getPredatorAction(int idx) {
		return aPredators.get(idx);
	}
	
	public int getPreyAction(int idx) {
		return aPreys.get(idx);
	}
}
