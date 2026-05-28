package entityClasses;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Member {

	/*-*
	
	Attributes 
	
	*/
	
	private String firstName;
	private String lastName;
	private String type;
	private Pair<Integer, Integer> coords;
	private Color color = Color.WHITE;
	
	/*-*
	
	Constructors 
	
	*/
	
	public Member(String firstName, String lastName, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.coords = new Pair<>(-1, -1);
	}
	
	public Member(String firstName, String lastName, String type, String coords) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		Integer x = Integer.parseInt(coords.substring(0, coords.indexOf(",")));
		Integer y = Integer.parseInt(coords.substring(coords.indexOf(",") + 1));
		this.coords = new Pair<>(x, y);
	}
	
	/*-*
	
	Methods
	
	*/
	
	public void setFirstName(String name) {
		this.firstName = name;
	}
	
	public void setLastName(String name) {
		this.lastName = name;
	}
	
	public void setPart(String type) {
		this.type = type;
	}
	
	public void setCoordinates(int x, int y) {
		coords = new Pair<>(x, y);
	}
	
	public void setCoordinates(Pair<Integer, Integer> coords) {
		this.coords = coords;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getType() {
		return type;
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
	
	public Color getColor() {
		return color;
	}
	
}
