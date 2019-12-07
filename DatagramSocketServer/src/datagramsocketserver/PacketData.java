
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney


import java.io.Serializable;
import java.util.Arrays;


public class PacketData implements Serializable {

	public int seq;
	
	public byte[] data;
	
	public boolean last;

	public PacketData(int seq, byte[] data, boolean last) {
		super();
		this.seq = seq;
		this.data = data;
		this.last = last;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return "UDPPacket [seq=" + seq + ", data=" + Arrays.toString(data)
				+ ", last=" + last + "]";
	}
	
}
