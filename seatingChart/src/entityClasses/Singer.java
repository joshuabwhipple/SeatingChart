package entityClasses;

public class Singer {

	/*-*
	
	Attributes 
	
	*/
	
	private String name;
	private String part;
	
	/*-*
	
	Constructors 
	
	*/
	
	public Singer(String name, String part) {
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
	
	public String getName() {
		return name;
	}
	
	public String getPart() {
		return part;
	}
	
}
