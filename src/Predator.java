import java.util.ArrayList;
import java.util.Iterator;

public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
		// TODO Auto-generated constructor stub
	}
	
	public int planMoveRandom() {
		return (int) Math.floor(Math.random() * DIR_NUM);
	}
	
	public int move(int DIR, ArrayList<Agent> preys) {
		move(DIR);
		
		int reward = 0;
		for (Iterator<Agent> iterator = preys.iterator(); iterator.hasNext();) {
			if (this.equals(iterator.next())) {
				reward += 10;
				iterator.remove();
			}
		}
		
		return reward;
	}
}
