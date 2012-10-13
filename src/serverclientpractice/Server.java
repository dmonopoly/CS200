package serverclientpractice;

import static serverclientpractice.Message.TEST_SERVER;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static int PORT_NUMBER = 31415;
	private ServerSocket ss = null;
	private Socket s = null;
	
	public static void main( String[] args ) {
		// 1
		Server server = new Server();
	}
	
	public Server() {
		// Grab port number
		
		
		// 2
		try {
			ss = new ServerSocket(PORT_NUMBER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		while (true) {
			// Continuously check for a new client for which to create a thread
			try {
				s = ss.accept(); // Wait for a client

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
		// Prepare connection and communication variables
		Socket mySocket;
		PrintWriter pw; // To speak to a client
		BufferedReader br; // To listen to a client

		public HandleAClient(Socket s) {
			mySocket = s;
		}

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
			while (true) {
				try {
//					// Listen to the client
//					String message = br.readLine();
//					
//					message = "2. Server: I got your message: " + message;
//					System.out.println(message);
//
//					// Send back a response to the client
//					pw.println(message); // to client
					
					// Listen for interaction via protocol
					String message = br.readLine();
					processMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private void processMessage(String msg) {
			// Decide action based on message from client
			if (msg.equals(Message.TEST_SERVER)) {
				System.out.println("Server test passed. Testing client...");
				pw.println(Message.TEST_CLIENT);
			}
		}
	}
	
}
