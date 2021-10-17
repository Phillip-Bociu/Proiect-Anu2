package main;
import java.io.IOException;

import networking.Client;
import networking.Server;

public class TestMain {
	
	
	
	public static void main(String[] args)
	{


		Server s = new Server();
		s.start();

		try {
			Thread.sleep(500000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
}
