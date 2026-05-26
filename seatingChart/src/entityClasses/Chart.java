package entityClasses;

import java.util.ArrayList;

public class Chart {

	/*-*
	
	Attributes 
	
	*/
	
	private int rows;
	private int seatsPerRow;
	private int totalSeats;
	private ArrayList<Member> members;
	
	/*-*
	 
	Constructors
	 
	*/
	
	public Chart(String rows, String totalSeats, String memberList) {
		this.rows = Integer.parseInt(rows);
		this.totalSeats = Integer.parseInt(totalSeats);
		this.seatsPerRow = Math.ceilDiv(this.totalSeats, this.rows);
		members = new ArrayList<Member>();
		readList(memberList);
	}
	
	/*-*
	
	Methods
	 
	*/
	
	private void readList(String memberList) {
		int commaIndex = memberList.indexOf(',');
		int newLineIndex = memberList.indexOf('\n');
		if (newLineIndex >= 0) {
			String name = memberList.substring(0, commaIndex).strip();
			String part = memberList.substring(commaIndex + 1, newLineIndex).strip();
			System.out.println(name + " " + part);
			Member readMember = new Member(name, part);
			members.add(readMember);
			readList(memberList.substring(newLineIndex+1));
		} else {
			String name = memberList.substring(0, commaIndex).strip();
			String part = memberList.substring(commaIndex + 1, memberList.length()).strip();
			System.out.println(name + " " + part);
			Member readMember = new Member(name, part);
			members.add(readMember);
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
	
	public void setmembers(String memberList) {
		this.members = new ArrayList<Member>();
		readList(memberList);
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
}
