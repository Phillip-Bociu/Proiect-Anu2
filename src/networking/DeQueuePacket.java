package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeQueuePacket extends Packet{

	private static final long serialVersionUID = 7550568291134647615L;
	
	public int ID;
	public DeQueuePacket(int ID)
	{
		this.packetID = PacketID.DeQueue;
		this.ID = ID;
	}
	
	public DeQueuePacket()
	{
		this.packetID = PacketID.DeQueue;
		this.ID = -1;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == PacketID.DeQueue)
		        {
		        	DeQueuePacket p = (DeQueuePacket)pk;
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
