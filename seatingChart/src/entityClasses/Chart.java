package entityClasses;

import java.util.ArrayList;

public class Chart {

	/*-*
	
	Attributes 
	
	*/
	
	private int rows;
	private int seatsPerRow;
	private int totalSeats;
	private ArrayList<Singer> singers;
	
	/*-*
	 
	Constructors
	 
	*/
	
	public Chart(String rows, String totalSeats, String singerList) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		singers = new ArrayList<Singer>();
		readList(singerList);
	}
	
	/*-*
	
	Methods
	 
	*/
	
	private void readList(String singerList) {
		int commaIndex = singerList.indexOf(',');
		int newLineIndex = singerList.indexOf('\n');
		if (newLineIndex >= 0) {
			String name = singerList.substring(0, commaIndex).strip();
			String part = singerList.substring(commaIndex + 1, newLineIndex).strip();
			System.out.println(name + " " + part);
			Singer readSinger = new Singer(name, part);
			singers.add(readSinger);
			readList(singerList.substring(newLineIndex+1));
		} else {
			String name = singerList.substring(0, commaIndex).strip();
			String part = singerList.substring(commaIndex + 1, singerList.length()).strip();
			System.out.println(name + " " + part);
			Singer readSinger = new Singer(name, part);
			singers.add(readSinger);
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
	
	public void setSingers(String singerList) {
		this.singers = new ArrayList<Singer>();
		readList(singerList);
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
	
	public ArrayList<Singer> getSingers() {
		return this.singers;
	}	
}
