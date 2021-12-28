package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginConfirmationPacket extends Packet {
	private static final long serialVersionUID = 1644252374114587571L;

	public int ID;
	public int elo;
	
	public LoginConfirmationPacket(int ID, int elo)
	{
		this.packetID = PacketID.LoginConfirmation;
		this.ID = ID;
		this.elo = elo;
	}
	
	public LoginConfirmationPacket()
	{
		this.packetID = PacketID.LoginConfirmation;
		this.ID = -1;
		this.elo = 0;
	}
	
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == this.packetID)
		        {
		        	LoginConfirmationPacket p = (LoginConfirmationPacket)pk;
		        	this.ID = p.ID;
		        	this.elo = p.elo;
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
