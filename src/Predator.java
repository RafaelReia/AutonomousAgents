import java.util.ArrayList;
import java.util.Iterator;

public class Predator extends Agent {

	public Predator(int locX_, int locY_) {
		super(locX_, locY_);
	}
	
	public Predator(Agent predator) {
		super(predator);
	}
	
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
	
	public int planMoveRandom() {
		return (int) Math.floor(Math.random() * DIR_NUM);
	}

	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
