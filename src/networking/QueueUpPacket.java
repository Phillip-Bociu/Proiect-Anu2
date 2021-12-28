package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class QueueUpPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2565840473766722473L;
	public int ID;
	
	public QueueUpPacket(int ID)
	{
		this.packetID = PacketID.QueueUp;
		this.ID = ID;
	}
	
	public QueueUpPacket()
	{
		this.packetID = PacketID.QueueUp;
		this.ID = -1;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == this.packetID)
		        {
		        	QueueUpPacket p = (QueueUpPacket)pk;
		        	this.ID = p.ID;
		        }
		 } 
	}
	
	@Override
	public byte[] toBytes() throws IOException
	{	
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		         ObjectOutputStream out = new ObjectOutputStream(bos)) {
		        out.writeObject(this);
		        return bos.toByteArray();
		}
	}
}
