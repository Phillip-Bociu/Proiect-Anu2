package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.Serializable;

public class PlayerPacket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x, y, movingDir, numSteps;

	public PlayerPacket()
	{
		x = -1;
		y = -1;
		movingDir = -1;
		numSteps = -1;
	}
	
	public PlayerPacket(int x, int y, int movingDir, int numSteps) {
		super();
		this.x = x;
		this.y = y;
		this.movingDir = movingDir;
		this.numSteps = numSteps;
	}
	
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        PlayerPacket p = ((PlayerPacket)(in.readObject())); 
		        this.x = p.x;
		        this.y = p.y;
		        this.movingDir = p.movingDir;
		        this.numSteps = p.numSteps;
		    } 
	}
	
	public byte[] toBytes() throws IOException
	{	
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		         ObjectOutputStream out = new ObjectOutputStream(bos)) {
		        out.writeObject(this);
		        return bos.toByteArray();
		}
	}

}
