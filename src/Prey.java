import java.util.ArrayList;

public class Prey extends Agent {

	public Prey(int locX_, int locY_) {
		super(locX_, locY_);
	}
	
	public Prey(Agent prey) {
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

	@Override
	public void print() {
		System.out.print("Prey(" + getX() + "," + getY() + ")");
	}
}