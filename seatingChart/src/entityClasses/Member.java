package entityClasses;

public class Member {

	/*-*
	
	Attributes 
	
	*/
	
	private String name;
	private String part;
	private int x = -1;
	private int y = -1;
	
	/*-*
	
	Constructors 
	
	*/
	
	public Member(String name, String part) {
		this.name = name;
		this.part = part;
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
		this.x = x;
		this.y = y;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPart() {
		return part;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int[] getCoordinates() {
		int[] coords = {x, y};
		return coords;
	}
	
}
