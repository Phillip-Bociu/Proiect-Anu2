package networking;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import map.Map;

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
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		
		try {
			synchronized(Map.lastSentPacket)
			{					
				packet.setData(Map.lastSentPacket.toBytes());
			}
			socket.send(packet);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true)
		{
			try {
				socket.receive(packet);
				synchronized(Map.lastReceivedPacket)
				{
					Map.lastReceivedPacket.readFromBytes(packet.getData());
				}
				
				synchronized(Map.lastSentPacket)
				{					
					packet.setData(Map.lastSentPacket.toBytes());
				}
				socket.send(packet);
			}catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
