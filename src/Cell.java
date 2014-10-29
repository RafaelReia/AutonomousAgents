
public class Cell {

	private Location location;
	private Animal animal = null;
	
	public Cell(int x, int y){
		location = new Location(x,y);
	}
	
	public void setAnimal(Animal animal){
		this.animal = animal;
	}

	public Location getLocation(){
		return location;
	}


	public Animal getAnimal() {
		return animal;
	}
	
}
