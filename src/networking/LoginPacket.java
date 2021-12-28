package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginPacket extends Packet{
	private static final long serialVersionUID = -8180373407242626182L;
	public String username;
	public String password;
	
	public LoginPacket(String username, String password)
	{
		this.packetID = PacketID.Login;
		this.username = username;
		this.password = password;
	}
	
	public LoginPacket()
	{
		this.packetID = PacketID.Login;
		this.username = null;
		this.password = null;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == PacketID.Login)
		        {
		        	LoginPacket p = (LoginPacket)pk;
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
