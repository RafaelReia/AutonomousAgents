package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import agents.Predator;
import agents.Prey;

public class QValuesSet extends TreeMap<State, LinkedList<Double>> {
	private static final long serialVersionUID = 7160174166607194695L;
	private double defaultValue;
	private final LinkedList<Double> constantList;

	public QValuesSet(double initValue, int NUM) {
		defaultValue = initValue;
		LinkedList<Double> c = new LinkedList<Double>();
		for (int i = 0;i<NUM;i++) {
			c.add(initValue);
		}
		constantList = new LinkedList<Double>(c);
	}

	/**
	 * Get the list of values for a state @state
	 * use this if we have the @state object
	 * @param t
	 * @return
	 */
	public LinkedList<Double> get(State t) {
		if (containsKey(t))
			return get(t);
		else{
			this.put(t, new LinkedList<Double>(constantList));
		}
		return get(t);
	}
	
	/**
	 * Get the list of values for a state @state
	 * use this if we don't have the @state object
	 * @param predators
	 * @param prey
	 * @return
	 */
	public LinkedList<Double> get(ArrayList<Predator> predators, Prey prey) {
		return get(new State(predators, prey));
	}
	
	/**
	 * set the value for the @action in the list with value @value
	 * @param t
	 * @param action
	 * @param value
	 */
	public void set(State t,int action, double value){
		if (containsKey(t))
			get(t).set(action, value);
		else{
			this.put(t, new LinkedList<Double>(constantList));
			get(t).set(action, value);
		}
	}

	public void set(ArrayList<Predator> predators, Prey prey, int action,
			double value) {
		set(new State(predators, prey),action, value);
	}

	public Double get(ArrayList<Predator> predators, Prey prey, int action) {
		return get(predators,prey).get(action);
	}

}
