package networkingwarcardgame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Handles the AI program that can play War. This essentially creates a GamePanel and calls its step() function at time intervals.
 * @author David Zhang
 */
public class AIApplication extends JFrame implements ActionListener {
	
	public static void main(String[] args) {
		AIApplication app = new AIApplication(); // a JFrame

		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ActionListener stepHandler = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.step(); // deal with passing data...
			}
		};
		
		// Start timer that calls step appropriately
		Timer timer = new Timer(1000, stepHandler); // ms, listener
		timer.setInitialDelay(1900); // ms before first fire
		timer.start();
	}
	
	public void step() {
		gamePanel.step();
	}

	public AIApplication() {
		// Set frame dimensions
		maxX = 900;
		maxY = 750;
		setSize(maxX, maxY);
		lblPlayerName = new JLabel("AI"); // AI's name
		
		// Initialize other variables
		existingPlayerPanels = new ArrayList<PlayerPanel>(); 

		/** Handle panels*/
		ultimatePanel = new JPanel(new BorderLayout());

		// Top menu
		JPanel topMenuPanel = new JPanel();
		ultimatePanel.add(topMenuPanel, BorderLayout.NORTH);
		topMenuPanel.setLayout(new GridLayout(0, 1, 0, 0));

		// War label at top
		JLabel lblWar = new JLabel("War");
		lblWar.setHorizontalAlignment(SwingConstants.CENTER);
		lblWar.setFont(new Font("Lucida Calligraphy", Font.BOLD, 26));
		topMenuPanel.add(lblWar);

		// Actual game: gamePanel
		gamePanel = new GamePanel(lblPlayerName.getText(), this);
		
		// Card layout prep
		ultimatePanel.add(gamePanel, BorderLayout.CENTER);

		setContentPane(ultimatePanel);
		
		startGame();
	}

	// Not really used by AI, but necessary for gamePanel
	public void actionPerformed(ActionEvent e) {
//		p.println("test");
////		gamePanel.step();
	}

	// Initialize gamePanel components and start the game
	private void startGame() {
		int numCards = 10; // Num cards that AI plays with
		gamePanel.setNumCards(numCards);
		gamePanel.setPlayerName(lblPlayerName.getText());
		gamePanel.initializeDecksAndPlayers();
	}    

	/* Instance fields */
	private Printer p = new Printer();
	private int maxX, maxY;
	
	// Panels & Components
	private JPanel ultimatePanel;
	private GamePanel gamePanel; // essential
	private JPanel mainAreaPanel; 
	private ArrayList<PlayerPanel> existingPlayerPanels;

	private JLabel lblPlayerName;
}
