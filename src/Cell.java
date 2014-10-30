
public class Cell {

	private Location location;
	private Agent agent = null;
	
	public Cell(int x, int y){
		location = new Location(x,y);
	}
	
	public void setAnimal(Agent agent){
		this.agent = agent;
	}

	public Location getLocation(){
		return location;
	}


	public Agent getAnimal() {
		return agent;
	}
	
}
