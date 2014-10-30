import java.util.ArrayList;

public class Prey extends Agent {

	public Prey(int locX_, int locY_) {
		super(locX_, locY_);
		// TODO Auto-generated constructor stub
	}
	
	public int planMove(ArrayList<Agent> predators) {
		ArrayList<Integer> dir_cand = tryMove(predators);
		double p_delta  = 0.2 / dir_cand.size();
		double p_bound = 0;
		double p = Math.random();
		
		for (int dir : dir_cand) {
			p_bound += p_delta;
			if (p < p_bound) {
				move(dir);
			}
		}
		
		return DIR_WAIT;
	}
}