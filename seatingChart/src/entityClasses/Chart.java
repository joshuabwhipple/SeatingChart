package entityClasses;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Chart {

	/*-*
	
	Attributes 
	
	*/
	
	private int rows;
	private int seatsPerRow;
	private int totalSeats;
	private ArrayList<Member> members;
	private File saveLocation;
	private boolean customTypes;
	private ArrayList<Pair<String, Color>> types;
	
	/*-*
	 
	Constructors
	 
	*/
	
	public Chart(String rows, String totalSeats, String memberList) throws StringIndexOutOfBoundsException {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		members = new ArrayList<Member>();
		readMembers(memberList);
		this.saveLocation = null;
		customTypes = false;
		readTypes(null);
		setColors();
	}
	
	public Chart(String rows, String totalSeats, String memberList, File saveLocation) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		members = new ArrayList<Member>();
		readMembers(memberList);
		this.saveLocation = saveLocation;
		customTypes = false;
		readTypes(null);
		setColors();
	}
	
	public Chart(String rows, String totalSeats, ArrayList<Member> members, File saveLocation) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		this.members = members;
		this.saveLocation = saveLocation;
		customTypes = false;
		readTypes(null);
		setColors();
	}
	
	public Chart(String rows, String totalSeats, String memberList, String types) throws StringIndexOutOfBoundsException {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		members = new ArrayList<Member>();
		readMembers(memberList);
		this.saveLocation = null;
		customTypes = true;
		this.types = new ArrayList<>();
		readTypes(types);
		setColors();
	}
	
	public Chart(String rows, String totalSeats, String memberList, File saveLocation, String types) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		members = new ArrayList<Member>();
		readMembers(memberList);
		this.saveLocation = saveLocation;
		customTypes = true;
		this.types = new ArrayList<>();
		readTypes(types);
		setColors();
	}
	
	public Chart(String rows, String totalSeats, ArrayList<Member> members, File saveLocation, String types) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		this.members = members;
		this.saveLocation = saveLocation;
		customTypes = true;
		this.types = new ArrayList<>();
		readTypes(types);
		setColors();
	}
	
	/*-*
	
	Methods
	 
	*/
	
	private void readMembers(String memberList) throws StringIndexOutOfBoundsException {
		Scanner memberReader = new Scanner(memberList);
		while(memberReader.hasNextLine()) {
			String data = memberReader.nextLine();
			int commaIndex1 = data.indexOf(',');
			int commaIndex2 = data.substring(commaIndex1 + 1).indexOf(',') + commaIndex1 + 1;
			String firstName = data.substring(0, commaIndex1).strip();
			String lastName = data.substring(commaIndex1 + 1, commaIndex2).strip();
			String part = data.substring(commaIndex2 + 1).strip();
			Member readMember = new Member(firstName, lastName, part);
			members.add(readMember);
		}
		memberReader.close();
		
	}
	
	private void readTypes(String typesList) {
		if (typesList != null) {
			Scanner typeReader = new Scanner(typesList);
			while (typeReader.hasNextLine()) {
				String data = typeReader.nextLine();
				int commaIndex = data.indexOf(',');
				String typeName = data.substring(0, commaIndex).strip();
				Color typeColor = Color.WHITE;
				try {
					typeColor = Color.web(data.substring(commaIndex+1).strip());
				} catch (IllegalArgumentException e) {
					Alert unknownColor = new Alert(Alert.AlertType.ERROR);
					unknownColor.setTitle("Unknown Color");
					unknownColor.setHeaderText("Custom type " + typeName + " has an unknown color. Defaulting color to white.");
					unknownColor.setContentText("You can try a new color with the \"Edit Types\" button in the editor.");
					unknownColor.showAndWait();
				}
				Pair<String, Color> newType = new Pair<>(typeName, typeColor);
				types.add(newType);
			}
			typeReader.close();
		} else {
			types = new ArrayList<>();
			types.add(new Pair<>("Soprano", Color.LIGHTBLUE));
			types.add(new Pair<>("Soprano 1", Color.LIGHTBLUE));
			types.add(new Pair<>("Soprano 2", Color.LIGHTSKYBLUE));
			types.add(new Pair<>("Alto", Color.LIGHTSEAGREEN));
			types.add(new Pair<>("Alto 1", Color.LIGHTSEAGREEN));
			types.add(new Pair<>("Alto 2", Color.LIGHTGREEN));
			types.add(new Pair<>("Tenor", Color.LIGHTCORAL));
			types.add(new Pair<>("Tenor 1", Color.LIGHTCORAL));
			types.add(new Pair<>("Tenor 2", Color.LIGHTSALMON));
			types.add(new Pair<>("Baritone", Color.LIGHTGRAY));
			types.add(new Pair<>("Bass", Color.LIGHTSTEELBLUE));
			types.add(new Pair<>("Bass 1", Color.LIGHTSTEELBLUE));
			types.add(new Pair<>("Bass 2", Color.STEELBLUE));
		}
	}
	
	public void setColors() {
		for (int i = 0; i < members.size(); ++i) {
			Member current = members.get(i);
			for (int j = 0; j < types.size(); ++j) {
				if (current.getType().equals(types.get(j).getKey())) {
					current.setColor(types.get(j).getValue());
					break;
				}
			}
		}
	}
	
	public void setRows(String rows) {
		this.rows = Integer.parseInt(rows);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);

	}
	
	public void setTotalSeats(String totalSeats) {
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
	}
	
	public void setMembers(String memberList) {
		this.members = new ArrayList<Member>();
		readMembers(memberList);
	}
	
	public void setMembers(ArrayList<Member> members) {
		this.members = members;
	}
	
	public void setSaveLocation(File saveLocation) {
		this.saveLocation = saveLocation;
	}
	
	public void setTypes(ArrayList<Pair<String, Color>> types) {
		if (types == null) {
			customTypes = false;
			readTypes(null);
		} else {
			customTypes = true;
			this.types = types;
		}
		setColors();
	}
	
	public void setTypes(String typesList) {
		customTypes = true;
		readTypes(typesList);
		setColors();
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getSeatsPerRow() {
		return this.seatsPerRow;
	}	
	
	public int getTotalSeats() {
		return this.totalSeats;
	}	
	
	public ArrayList<Member> getMembers() {
		return this.members;
	}
	
	public File getSaveLocation() {
		return this.saveLocation;
	}
	
	public boolean getCustomTypes() {
		return this.customTypes;
	}
	
	public ArrayList<Pair<String, Color>> getTypes() {
		return this.types;
	}
}
