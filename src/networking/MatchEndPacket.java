package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MatchEndPacket extends Packet{

	private static final long serialVersionUID = 5908469872266240674L;
	
	public String winnerName;
	public String loserName;
	
	public MatchEndPacket()
	{
		this.packetID = PacketID.MatchEnd;
		this.winnerName = null;
		this.loserName = null;
	}
	
	public MatchEndPacket(String winnerName, String loserName)
	{
		this.winnerName = winnerName;
		this.loserName = loserName;
	}

	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
			 	
			 MatchEndPacket p = ((MatchEndPacket)(in.readObject())); 
			 if(p.packetID == PacketID.MatchEnd)
			 {
				this.packetID = PacketID.MatchEnd;
				this.winnerName = String.copyValueOf(p.winnerName.toCharArray());
				this.loserName = String.copyValueOf(p.loserName.toCharArray());
			 }
		 
		 } catch(EOFException e)
		 {
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
