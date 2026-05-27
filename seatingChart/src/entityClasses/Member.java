package entityClasses;

import javafx.util.Pair;

public class Member {

	/*-*
	
	Attributes 
	
	*/
	
	private String name;
	private String part;
	private Pair<Integer, Integer> coords;
	
	/*-*
	
	Constructors 
	
	*/
	
	public Member(String name, String part) {
		this.name = name;
		this.part = part;
		this.coords = new Pair<>(-1, -1);
	}
	
	/*-*
	
	Methods
	
	*/
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPart(String part) {
		this.part = part;
	}
	
	public void setCoordinates(int x, int y) {
		coords = new Pair<>(x, y);
	}
	
	public void setCoordinates(Pair<Integer, Integer> coords) {
		this.coords = coords;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPart() {
		return part;
	}
	
	public int getX() {
		return coords.getKey();
	}
	
	public int getY() {
		return coords.getValue();
	}
	
	public Pair<Integer, Integer> getCoordinates() {
		return coords;
	}
	
}
