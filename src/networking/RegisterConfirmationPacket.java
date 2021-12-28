package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterConfirmationPacket extends Packet{
	private static final long serialVersionUID = -8180373407242626182L;
	public boolean registered;
	
	public RegisterConfirmationPacket(boolean registered)
	{
		this.packetID = PacketID.RegisterConfirmation;
		this.registered = registered;
	}
	
	public RegisterConfirmationPacket()
	{
		this.packetID = PacketID.RegisterConfirmation;
		this.registered = false;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == PacketID.RegisterConfirmation)
		        {
		        	RegisterConfirmationPacket p = (RegisterConfirmationPacket)pk;
		        	this.registered = p.registered;
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
