package networking;

import java.net.InetAddress;
import java.net.SocketException;

import map.Map;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread
{
	
	private DatagramSocket socket;
	public InetAddress ipAddress;
	public int port;
	
	public Server()
	{
		try {
			
			this.socket = new DatagramSocket(1331);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
			ipAddress = packet.getAddress();
			port = packet.getPort();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while(true)
		{
			
			try {
				synchronized(Map.lastSentPacket)
				{					
					packet.setData(Map.lastSentPacket.toBytes());
				}
				socket.send(packet);
				
				socket.receive(packet);
				synchronized(Map.lastReceivedPacket)
				{
					Map.lastReceivedPacket.readFromBytes(packet.getData());
				}
				
			}catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	
	public void sendData(byte[] data, InetAddress ipAddress, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
