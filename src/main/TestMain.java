package main;
import java.io.IOException;

import networking.Client;
import networking.Server;

public class TestMain {
	
	
	
	public static void main(String[] args)
	{


		Server s = new Server();
		s.start();

		Client c = new Client("127.0.0.1");
		c.start();
		String packet = new String("ping");
		System.out.println(packet.length());
		c.sendData(packet.getBytes());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
}
