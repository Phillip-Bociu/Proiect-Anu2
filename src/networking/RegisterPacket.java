package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterPacket extends Packet{
	private static final long serialVersionUID = -8180373407242626182L;
	public String username;
	public String password;
	
	public RegisterPacket(String username, String password)
	{
		this.packetID = PacketID.Register;
		this.username = username;
		this.password = password;
	}
	
	public RegisterPacket()
	{
		this.packetID = PacketID.Register;
		this.username = null;
		this.password = null;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == PacketID.Register)
		        {
		        	RegisterPacket p = (RegisterPacket)pk;
		        	this.username = String.copyValueOf(p.username.toCharArray());
			        this.password = String.copyValueOf(p.password.toCharArray());		        	
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
