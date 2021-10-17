package networking;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Client extends Thread
{
	
	private InetAddress ipAddress;
	private DatagramSocket socket;
	
	public Client(String ipAddress)
	{
		try {
			
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		byte[] data = new byte[1024];
		while(true)
		{
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
				
			}catch (IOException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("Server: " + new String(packet.getData()));
	
			
		}
	}
	
	
	public void sendData(byte[] data)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
