package serverclientpractice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends JFrame implements ActionListener { // WindowListener override to close streams properly

	// Connection variables
	private static String HOST_NAME = "localhost"; // Prompt for in terminal
	private static int PORT_NUMBER = 31415;
	private PrintWriter out;
    private BufferedReader in;
    private Socket socket; 
	
	private JLabel label;
	private JButton btn;
	
	public static void main(String[] args) {
        Client app = new Client(); // a JFrame
        
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
	public Client() {
		// 3
		setSize(100, 100);
		
		JPanel ultimatePanel = preparePanels();
		setContentPane(ultimatePanel);
		connect();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn) {
			System.out.println("Speaking to the server");
//			// Speaking to the server
//			String msgToServer = "1. Hey, I'm communicating to the server.";
//			out.println(msgToServer);
//			
//			// Listening to the server
//			try {
//				String s = in.readLine();
//				System.out.println("3. I heard back from the server: " + s);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			
			// Now start interaction via a protocol
			System.out.println("Starting more interactions: ");
			sendToServer(Message.TEST_SERVER);
			listenToServer();
		}
	}
	
	private void sendToServer(String msg) {
		try {
			out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void listenToServer() {
		try {
			String msg = in.readLine();
			processMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processMessage(String msg) {
		// Decide action based on message from server
		if (msg.equals(Message.TEST_CLIENT)) {
			System.out.println("Client test passed.");
		}
	}
	
	// Prepare the view
	private JPanel preparePanels() {
		JPanel mainPanel = new JPanel();

		// Add stuff to mainPanel here
		label = new JLabel("Label");
		mainPanel.add(label);
		
		btn = new JButton("Do something");
		btn.addActionListener(this);
		mainPanel.add(btn);
		
		return mainPanel;
	}
	
	private void connect() {
		// 4
		System.out.println("Connecting...");
		socket = null;
        out = null;
        in = null;

        try {
            socket = new Socket(HOST_NAME, PORT_NUMBER);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            System.exit(1);
        }

        // Close elsewhere!
//        out.close();
//        in.close();
//        socket.close();
        
        System.out.println("Done connecting");
	}
}
