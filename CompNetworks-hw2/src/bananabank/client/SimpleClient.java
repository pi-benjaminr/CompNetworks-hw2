package bananabank.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SimpleClient {
	public static int port = 2000;
	
	public static void main(String args[]) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", port);
		System.out.println("CLIENT: Client is connected to the server");
		BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintStream ps = new PrintStream(s.getOutputStream());
		
		ps.println("10 11111 22222");
		String line = r.readLine();
		System.out.println("CLIENT: Received: " + line);
		
		ps.println("15 44444 66666");
		line = r.readLine();
		System.out.println("CLIENT: Received: " + line);
		
		ps.println("SHUTDOWN");
		line = r.readLine();
		System.out.println("CLIENT: Received: " + line);
		
		System.out.println("CLIENT: closing connection");
		ps.close();
		
	}

}
