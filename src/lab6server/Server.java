package lab6server;

//Server.java
import java.io.*;
import java.net.*;

public class Server {
	private static int PORT_NUMBER = 31415;
	ServerSocket ss = null;
	Socket s = null;

	public Server() {
		try {
			ss = new ServerSocket(PORT_NUMBER);
		} catch( Exception e ) {
			e.printStackTrace();
			System.exit(0);
		}


		while ( true ) {
			try {
				s = ss.accept();

				HandleAClient hac = new HandleAClient( s );
				new Thread(hac).start();

			} catch (Exception e) {
				System.out.println("got an exception" + e.getMessage() );
			}

			System.out.println( "got a connection" );
		}
	}

	public static void main( String[] args ) {
		Server server = new Server();
	}

	class HandleAClient implements Runnable {
		Socket mySocket;
		PrintWriter pw;
		BufferedReader br;

		public HandleAClient( Socket s ) {
			mySocket = s;
		}

		public void run() {
			try {
				//Create the 2 streams for talking to the client
				pw = new PrintWriter( mySocket.getOutputStream(), true );
				br = new BufferedReader( new InputStreamReader( mySocket.getInputStream() ) );
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			System.out.println("got streams");

			//This thread loops forever to receive a client message and echo it back
			while( true ) {
				try {
					//Wait for the client to send a String and print it out
					String message = br.readLine(); // this gets what I type
					
					System.out.println("Client message: " + message );

					//Send back a response
					message = "Server echoes: " + message;

					pw.println( message ); // to client
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
}