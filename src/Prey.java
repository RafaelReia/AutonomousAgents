import java.util.List;
import java.util.Random;

public class Prey extends Animal {
	//BasicEnvironment be;
	private Cell cell;
	public Prey(BasicEnvironment env) {
	//receive basic environment to acess the "board"
	super(env);
	}
	
	Cell move(){
		Random _r = new Random();
		
		if(_r.nextDouble()<0.8){
			List<Cell> steps = getPossibleSteps();
			return steps.get(_r.nextInt(steps.size()));
			
		}
		return cell;
	}

	private List<Cell> getPossibleSteps() {
		
		
		
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "p";
	}
	
	
}