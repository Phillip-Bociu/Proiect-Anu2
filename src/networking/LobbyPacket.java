package networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LobbyPacket extends Packet{

	private static final long serialVersionUID = -7079118008582662752L;

	public String otherIp;
	
	public LobbyPacket()
	{
		this.packetID = null;
		this.otherIp = null;
	}
	
	public LobbyPacket(boolean isHost, String otherIp)
	{
		if(isHost)
			this.packetID = PacketID.Host;
		else
			this.packetID = PacketID.Connect;
		this.otherIp = String.copyValueOf(otherIp.toCharArray());
	}
		
	@Override
	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
		        Packet pk = ((Packet)(in.readObject())); 
		        if(pk.packetID == PacketID.Host || pk.packetID == PacketID.Connect)
		        {
		        	LobbyPacket p = (LobbyPacket)pk;	        	
		        	this.packetID = p.packetID;
		        	this.otherIp = String.copyValueOf(p.otherIp.toCharArray());
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
