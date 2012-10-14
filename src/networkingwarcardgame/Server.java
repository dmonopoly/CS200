package networkingwarcardgame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import networkingwarcardgame.Message;

// Status: Just finished matching my serverclientpractice's Server
public class Server {
	
	public static void main(String[] args) {
		int portNum = 31415;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter port number (default: 31415): ");
		try {
			String nextLine = in.nextLine();
			portNum = Integer.parseInt(nextLine);
		} catch (Exception e) {
			System.out.println("Using default port number");
			portNum = 31415;
		}

		Server server = new Server(portNum);
	}

	public Server(int portNumber) {
		numClients = 0; // initial num clients is 0
		p.println("Port number: " + portNumber);
		try {
			ss = new ServerSocket(portNumber);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		while (true) {
			// Continuously check for a new client for which to create a thread
			try {
				s = ss.accept(); // Wait for a client (program halts here until connection occurs)
				numClients++;
				p.println("num clients: "+numClients);
				HandleAClient hac = new HandleAClient(s);
				new Thread(hac).start(); // Create the thread
			} catch (Exception e) {
				System.out.println("got an exception" + e.getMessage() );
			}
			System.out.println( "A client has connected" );
		}
	}
	
	// Internal class that handles each new thread for a client
	class HandleAClient implements Runnable {
		private volatile boolean running = true; // volatile = var modified by different threads
		// Prepare connection and communication variables
		Socket mySocket;
		PrintWriter pw; // To speak to a client
		BufferedReader br; // To listen to a client

		public HandleAClient(Socket s) {
			mySocket = s;
		}

		// Key method of Runnable; when this method ends, the thread stops
		public void run() {
			try {
				// Create the 2 streams for talking to the client
				pw = new PrintWriter(mySocket.getOutputStream(), true);
				br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			// This thread loops forever to receive a client message and echo it back
			while (running) {
				try {
					// Listen for interaction via protocol
					String message = br.readLine();
					processMessage(message);
					p.println("Processed message in client thread");
				} catch (Exception e) {
					p.println("Caught");
//					e.printStackTrace();
				}
			}
		}
		
		private void processMessage(String msg) {
			// Decide action based on message from client
			if (msg.equals(Message.TEST_SERVER)) {
				System.out.println("Server test passed. Testing client...");
				pw.println(Message.TEST_CLIENT);
			} else if (msg.equals(Message.CLIENT_EXITED)) {
				stopThread();
				numClients--;
				if (numClients == 0) {
					System.out.println("Number of clients is 0");
				}
			}
		}
		
		private void stopThread() {
			running = false;
		}
	}
	
	/** Instance fields */
	private Printer p = new Printer();
	private int numClients;
	
	// Connection fields
	private ServerSocket ss = null;
	private Socket s = null;
	
	private ArrayList<Player> players;
}
