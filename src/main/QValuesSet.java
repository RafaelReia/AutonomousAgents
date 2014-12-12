package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import agents.Predator;
import agents.Prey;

public class QValuesSet extends TreeMap<DirectionalState, Qvalue> {
	private static final long serialVersionUID = -8594401860750971868L;
	private final Qvalue defaultValues;
	
	public QValuesSet(double initValuePredator,double initValuePrey, int NUM) {
		defaultValues = new Qvalue(initValuePredator,initValuePrey, NUM);
	}
	
	/**
	 * Get the values for a state @state use this if we have the @state object
	 * 
	 * @param t
	 * @return
	 */
	public Qvalue get(DirectionalState t) {
		if (containsKey(t))
			return super.get(t);
		else {
			this.put(t, defaultValues.copy());
		}
		return super.get(t);
	}

	/**
	 * Get values for a state @state use this if we don't have the @state object
	 * 
	 * @param predators
	 * @param prey
	 * @return
	 */
	public Qvalue get(ArrayList<Predator> predators, Prey prey) {
		return get(new DirectionalState(predators, prey));
	}

	public void put(ArrayList<Predator> predators, Prey prey, Qvalue value) {
		put(new DirectionalState(predators, prey), value);
	}

	public void set(ArrayList<Predator> predators, Prey prey,
			ArrayList<Integer> aPredators, double value) {
		DirectionalState s = new DirectionalState(predators, prey);
		Qvalue v = this.get(s);
		v.setPredatorValue(aPredators, value);
		this.put(s, v);
	}
	
	public void set(ArrayList<Predator> predators, Prey prey,
			int aPrey, double value) {
		DirectionalState s = new DirectionalState(predators, prey);
		Qvalue v = this.get(s);
		v.setPreyValue(aPrey, value);
		this.put(s, v);
	}
	
	public void set(ArrayList<Predator> predators, Prey prey,
			ArrayList<Integer> aPredators, int aPrey, double predatorValue, double preyValue) {
		DirectionalState s = new DirectionalState(predators, prey);
		Qvalue v = this.get(s);
		v.setPredatorValue(aPredators, predatorValue);
		v.setPreyValue(aPrey, preyValue);
		this.put(s, v);
	}


}
