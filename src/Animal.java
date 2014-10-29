public class Animal{
	public final static int DIR_WAIT 	= 0;
	public final static int DIR_NORTH 	= 1;
	public final static int DIR_SOUTH 	= 2;
	public final static int DIR_EAST 	= 3;
	public final static int DIR_WEST 	= 4;
	
	private int locX;
	private int locY;
	
	private int dirX[] = {0,-1,+1, 0, 0};
	private int dirY[] = {0, 0, 0,+1,-1};

	public Animal(int locX_, int locY_) {
		locX = locX_;
		locY = locY_;
	}
	
	public int getX() {
		return locX;
	}
	
	public int getY() {
		return locY;
	}
	
	public void move(int deltaX, int deltaY) {
		locX = (deltaX + 11) % 11;
		locY = (deltaY + 11) % 11;
	}
	
	public void move(int DIR) {
		move(dirX[DIR], dirY[DIR]);
	}
}
