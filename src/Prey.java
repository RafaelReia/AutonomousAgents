public class Prey extends Animal {

	public Prey(int locX_, int locY_) {
		super(locX_, locY_);
		// TODO Auto-generated constructor stub
	}
	
	public void move() {
		double p = Math.random();
		if (p < 0.8) {
			move(DIR_WAIT);
		} else if (p < 0.85) {
			move(DIR_NORTH);
		} else if (p < 0.9) {
			move(DIR_SOUTH);
		} else if (p < 0.95) {
			move(DIR_EAST);
		} else {
			move(DIR_WEST);
		}
	}
}