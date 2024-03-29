package agents;
import java.util.ArrayList;
import java.util.Arrays;

public class PreyMiniMaxQ extends Prey {

	public PreyMiniMaxQ(int locX_, int locY_) {
		super(locX_, locY_);
	}
	
	public PreyMiniMaxQ(Agent prey) {
		super(prey);
	}
	
	
	public ArrayList<Integer> tryMove(ArrayList<Predator> agents) {
		ArrayList<Integer> dir = new ArrayList<Integer>();
		for (int i = 1; i < DIR_NUM; i++) {
			Agent prob = new Predator(this);
			prob.move(i);
			boolean block = false;
			for (Agent agent : agents) {
				if (prob.equals(agent)) {
					block = true;
					break;
				}
			}
			if (!block) {
				dir.add(i);
			}
		}
		return dir;
	}
	
	public int planMoveRandom(ArrayList<Predator> predators) {
		ArrayList<Integer> dir_cand = tryMove(predators);
		double p_delta  = 0.2 / dir_cand.size();
		double p_bound = 0;
		double p = Math.random();
		
		for (int dir : dir_cand) {
			p_bound += p_delta;
			if (p < p_bound) {
				return dir;
			}
		}
		
		return DIR_WAIT;
	}
	
	public int planMoveRandom(Predator predator) {
		return planMoveRandom(new ArrayList<Predator>(Arrays.asList(predator)));
	}
	
	public double prob(ArrayList<Predator> predators, int aPrey) {
		ArrayList<Integer> dir_cand = tryMove(predators);
		double p_delta  = 0.2 / dir_cand.size();
		
		if (aPrey == DIR_WAIT)
			return 0.8;
		if (dir_cand.indexOf(aPrey) < 0) {
			return 0;
		} else {
			return p_delta;
		}
	}

}
