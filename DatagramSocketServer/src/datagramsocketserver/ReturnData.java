
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney


import java.io.Serializable;


public class ReturnData implements Serializable{
	
	private int packet;

	public ReturnData(int packet) {
		super();
		this.packet = packet;
	}

	public int getPacket() {
		return packet;
	}

	public void setPacket(int packet) {
		this.packet = packet;
	}
	
	

}
