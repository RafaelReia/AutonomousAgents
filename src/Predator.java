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
	
	public int planMoveVI()
	{
		/* Initialize array v arbitrarily (e.g., v(s) = 0 for all s ∈ S + )
			Repeat
				∆ ← 0
				For each s ∈ S:
					temp ← v(s)
					v(s) ← max a s p(s |s, a)[r(s, a, s ) + γv(s )]
					∆ ← max(∆, |temp − v(s)|)
				until ∆ < θ (a small positive number)
Output a deterministic policy, π, such that
π(s) = arg max a
s
p(s |s, a) r(s, a, s ) + γv(s )
		 * 
		 */
		 return 0;
	}
	
	public int planMovePI()
	{
		return 0;
	}
	
	public int planMovePE()
	{
		return 0;
	}
	
	@Override
	public void print() {
		System.out.print("Predator(" + getX() + "," + getY() + ")");
	}
}
