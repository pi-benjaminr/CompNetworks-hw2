package bananabank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.StringTokenizer;


public class WorkerThread extends Thread {
	
	Socket clientSocket;
	ServerSocket ss;
	BananaBank bank;
	
	public WorkerThread(Socket cs, ServerSocket ss, BananaBank bank) {
		this.clientSocket = cs;
		this.ss = ss;
		this.bank = bank;
	}

	@Override
	public void run() {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());
			String line;
			while (!ss.isClosed() && (line = r.readLine()) != null) {
				System.out.println("THREAD: received: "+line);
				StringTokenizer st = new StringTokenizer(line);
				String wordOne = st.nextToken();
				if (wordOne.equals("SHUTDOWN") && clientSocket.getInetAddress().toString().equals("/127.0.0.1")) {
					System.out.println("SERVER: Shutting down...");
					
					Collection<Account> accts = bank.getAllAccounts();
					int total = 0;
					for (Account acct : accts) {
						total += acct.getBalance();
					}
					ps.println(total+"\n");
					ss.close();
					
				} else {
					int amt = Integer.parseInt(wordOne);
					int srcNum = Integer.parseInt(st.nextToken());
					int dstNum = Integer.parseInt(st.nextToken());
					Account src = this.bank.getAccount(srcNum);
					Account dst = this.bank.getAccount(dstNum);
					
					//determine order to avoid deadlocks
					Account first, second;
					first = this.bank.getAccount(Math.min(srcNum, dstNum));
					second = this.bank.getAccount(Math.max(srcNum, dstNum));
					
					synchronized(first) {
						synchronized(second) {
							if (src.getBalance() < amt) {
								ps.println("Failure: Insufficient Funds in Account #"+srcNum+"\n");
							} else {
								src.transferTo(amt, dst);
								ps.println("Success: Transferred $"+amt+" from "+src+" to "+dst+"\n");
							}
						}
					}
				}
				
			}
			r.close();
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
