package lab6server;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class TextGui implements ActionListener {

	private static int PORT_NUMBER = 31415; // must match number in Server.java
	private JFrame frame;
	private JTextField textField;
	private JButton btnSubmit; 
	private PrintWriter out;
    private BufferedReader in;
    private Socket socket; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextGui window = new TextGui();
					window.frame.setVisible(true);
					
					try {
						System.out.println("Trying to connect");
						window.connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TextGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextField();
		frame.getContentPane().add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		btnSubmit = new JButton("New button");
		btnSubmit.addActionListener(this);
		
		frame.getContentPane().add(btnSubmit, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSubmit) {
			String text = textField.getText();
			
	        // Speaking to the server
			out.println(text);
			
			try {
				String s = in.readLine(); // do separately
				System.out.println("From server: " + s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	public void connect() throws IOException {
		System.out.println("Connecting...");
		socket = null;
        out = null;
        in = null;

        try {
            socket = new Socket("localhost", PORT_NUMBER);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            System.exit(1);
        }

        System.out.println("Made socket");

//        out.close();
//        in.close();
//        socket.close();
        
        System.out.println("End connect");
	}

}
