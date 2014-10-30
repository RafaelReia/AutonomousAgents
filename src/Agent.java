import java.util.ArrayList;

public class Agent{
	public final static int DIR_WAIT 	= 0;
	public final static int DIR_NORTH 	= 1;
	public final static int DIR_SOUTH 	= 2;
	public final static int DIR_EAST 	= 3;
	public final static int DIR_WEST 	= 4;
	public final static int DIR_NUM		= 5;
	
	private int locX;
	private int locY;
	
	private int dirX[] = {0,-1,+1, 0, 0};
	private int dirY[] = {0, 0, 0,+1,-1};

	public Agent(int locX_, int locY_) {
		locX = locX_;
		locY = locY_;
	}
	
	public Agent(Agent agent) {
		this.locX = agent.locX;
		this.locY = agent.locY;
	}
	
	public int getX() {
		return locX;
	}
	
	public int getY() {
		return locY;
	}
	
	public int getManhattanDist(Agent agent) {
		return Math.abs(locX - agent.locX) + Math.abs(locY - agent.locY);
	}
	
	public boolean equals(Agent agent) {
		return getManhattanDist(agent) == 0;
	}
	
	public void move(int deltaX, int deltaY) {
		locX = (deltaX + 11) % 11;
		locY = (deltaY + 11) % 11;
	}
	
	public void move(int DIR) {
		move(dirX[DIR], dirY[DIR]);
	}
	
	public ArrayList<Integer> tryMove(ArrayList<Agent> agents) {
		ArrayList<Integer> dir = new ArrayList<Integer>();
		for (int i = 1; i < DIR_NUM; i++) {
			Agent prob = new Agent(this);
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
}
