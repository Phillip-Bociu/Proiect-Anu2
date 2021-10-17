package networking;

import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread
{
	
	private DatagramSocket socket;
	
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
		while(true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
				
			}catch (IOException e)
			{
				e.printStackTrace();
			}
	
			String message = new String(packet.getData());
			
			if(message.startsWith("ping"))
			{
				System.out.println("Client > " + message);
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
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
