public abstract class Agent{
	public final static int DIR_WAIT 	= 0;
	public final static int DIR_NORTH 	= 1;
	public final static int DIR_SOUTH 	= 2;
	public final static int DIR_EAST 	= 3;
	public final static int DIR_WEST 	= 4;
	public final static int DIR_NUM		= 5;
	
	private int locX;
	private int locY;
	
	public final static int dirX[] = {0,-1,+1, 0, 0};
	public final static int dirY[] = {0, 0, 0,+1,-1};

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
		locX = (locX + deltaX + WORLDSIZE) % WORLDSIZE;
		locY = (locY + deltaY + WORLDSIZE) % WORLDSIZE;
	}
	
	public void move(int DIR) {
		move(dirX[DIR], dirY[DIR]);
	}
	
	abstract public void print();
}
