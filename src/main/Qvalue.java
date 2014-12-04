package main;

import static agents.Agent.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.xml.stream.events.EndDocument;

public class Qvalue {

	double[] predatorQvalue;
	double[] preyQvalue;
	int s;
	double initValue;
	static int NUM;

	public static int actionEncoder(ArrayList<Integer> aPredators) {
		int idx = 0;
		int pow = 1;
		for (Integer aPredator : aPredators) {
			idx += aPredator * pow;
			pow *= DIR_NUM;
		}
		return idx;
	}

	public static ArrayList<Integer> actionDecoder(int idx) {
		ArrayList<Integer> aPredators = new ArrayList<Integer>();
		for (int i = 0; i < NUM; i++) {
			aPredators.add(idx % DIR_NUM);
			idx /= DIR_NUM;
		}
		return aPredators;
	}

	public Qvalue(double initValue_, int NUM_) {
		initValue = initValue_;
		NUM = NUM_;

		s = (int) Math.pow(DIR_NUM, NUM + 1);
		predatorQvalue = new double[s];
		preyQvalue = new double[DIR_NUM];
		Arrays.fill(predatorQvalue, initValue);
		Arrays.fill(preyQvalue, initValue);
	}

	public Qvalue copy() {
		Qvalue nv = new Qvalue(initValue, NUM);
		return nv;
	}

	public void setPredatorValue(ArrayList<Integer> aPredators, double value) {
		int idx = actionEncoder(aPredators);
		predatorQvalue[idx] = value;
	}

	public void setPreyValue(int aPrey, double value) {
		preyQvalue[aPrey] = value;
	}
	
	public double getPredatorValue(ArrayList<Integer> aPredators) {
		int idx = actionEncoder(aPredators);
		return predatorQvalue[idx];
	}
	
	public double getPreyValue(int aPrey) {
		return preyQvalue[aPrey];
	}
	
	public double getMaxPredatorValue() {
		double maxValue = -Double.MAX_VALUE;
		for (int i = 0; i < predatorQvalue.length; i++) {
			if (maxValue < predatorQvalue[i]) {
				maxValue = predatorQvalue[i];
			}
		}
		return maxValue;
	}
	
	public double getMaxPreyValue() {
		double maxValue = -Double.MAX_VALUE;
		for (int i = 0; i < preyQvalue.length; i++) {
			if (maxValue < preyQvalue[i]) {
				maxValue = preyQvalue[i];
			}
		}
		return maxValue;
	}

	public ArrayList<Integer> choosePredatorActions(double epsilon) {
		ArrayList<Integer> maxActionIdx = new ArrayList<Integer>();
		double maxValue = -Double.MAX_VALUE;
		Random rnd = new Random();
		for (int i = 0; i < predatorQvalue.length; i++) {
			if (predatorQvalue[i] > maxValue) {
				maxActionIdx.clear();
				maxValue = predatorQvalue[i];
			}
			if (predatorQvalue[i] == maxValue) {
				maxActionIdx.add(i);
			}
		}

		ArrayList<Integer> aPredators = actionDecoder(maxActionIdx.get(rnd
				.nextInt(maxActionIdx.size())));
		for (int i = 0; i < aPredators.size(); i++) {
			if (rnd.nextDouble() < epsilon) {
				aPredators.set(i, rnd.nextInt(DIR_NUM));
			}
		}
		
		return aPredators;
	}

	public int choosePreyActions(double epsilon) {
		ArrayList<Integer> maxActionIdx = new ArrayList<Integer>();
		double maxValue = -Double.MAX_VALUE;
		Random rnd = new Random();
		for (int i = 0; i < preyQvalue.length; i++) {
			if (preyQvalue[i] > maxValue) {
				maxActionIdx.clear();
				maxValue = preyQvalue[i];
			}
			if (preyQvalue[i] == maxValue) {
				maxActionIdx.add(i);
			}
		}

		int aPrey = maxActionIdx.get(rnd.nextInt(maxActionIdx.size()));
		if (rnd.nextDouble() < epsilon) {
			aPrey = rnd.nextInt(DIR_NUM);
		}
		
		return aPrey;
	}
}
