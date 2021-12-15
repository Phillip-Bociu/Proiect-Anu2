package networking;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ProjectilePacket implements Serializable {
	
	private static final long serialVersionUID = 8431733858899598040L;
	
	public float x, y;
	public float dirX, dirY;
	public float speed;
	public int damage;
	
	public ProjectilePacket()
	{
		super();
		this.x = 0;
		this.y = 0;
		this.dirX = 0;
		this.dirY = 0;
		this.speed = 0;
		this.damage = 0;	
	}
	
	public ProjectilePacket(float x, float y, float dirX, float dirY, float speed, int damage)
	{
		super();
		this.x = x;
		this.y = y;
		this.dirX = dirX;
		this.dirY = dirY;
		this.speed = speed;
		this.damage = damage;
	}

	public void readFromBytes(byte[] packet) throws ClassNotFoundException, IOException
	{
		 try (ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		         ObjectInputStream in = new ObjectInputStream(bis)) {
			 	
			 	ProjectilePacket p = ((ProjectilePacket)(in.readObject())); 
		        this.x = p.x;
		        this.y = p.y;
		        this.dirX = p.dirX;
		        this.dirY = p.dirY;
		        this.speed = p.speed;
		        this.damage = p.damage;
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
