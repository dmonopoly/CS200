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
	}
	
	private void step() {
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

	// Called by timer from startGame()
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Exit game") {
			p.println("Reached");
			timer.stop();
			System.exit(0);
		}
		// Catch action from GamePanel for player 1 victory, but AIApplciation doesn't need to do anything for it
		else if (e.getActionCommand() == "Player 1 victory") { } 
		else if (e.getActionCommand() == "Player 2 victory") { } 
		else { // step() may blow up if unexpected action events fire since it's in an else, but all cases should be covered here
			step();
		}
	}

	// Initialize gamePanel components and start the game with the Timer!
	private void startGame() {
		int numCards = 2; // Total num cards that the AI and Computer play with
		gamePanel.setNumCards(numCards);
		gamePanel.setPlayerName(lblPlayerName.getText());
		gamePanel.initializeDecksAndPlayers();
		
		// Start timer that calls step appropriately
		timer = new Timer(1000, this); // ms, listener
		timer.setInitialDelay(1900); // ms before first fire
		timer.start();
	}    

	/* Instance fields */
	private Printer p = new Printer();
	private int maxX, maxY;
	Timer timer;
	
	// Panels & Components
	private JPanel ultimatePanel;
	private GamePanel gamePanel; // essential
	private JPanel mainAreaPanel; 
	private ArrayList<PlayerPanel> existingPlayerPanels;

	private JLabel lblPlayerName;
}
