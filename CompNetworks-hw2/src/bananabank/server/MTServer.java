package bananabank.server;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;


public class MTServer {
	private static final int PORT = 2000;
	
	public static void main(String args[]) throws IOException {
		
		ServerSocket ss = new ServerSocket(PORT);
		BananaBank bank = new BananaBank("accounts.txt");
		ArrayList<WorkerThread> threads = new ArrayList<WorkerThread>();
		while (!ss.isClosed()) {
			System.out.println("Waiting for connection");
			try {
			Socket cs = ss.accept();
			System.out.println("SERVER: client connected.");
			WorkerThread thread = new WorkerThread(cs, ss, bank, threads);
			threads.add(thread);
			thread.start();
			} catch(SocketException e) {}
			
		}
		System.out.println("SERVER: ss closed");
		
		
		
	}

}
