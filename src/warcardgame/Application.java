package warcardgame;

import static warcardgame.MenuState.GAME_RUNNING;
import static warcardgame.MenuState.GAME_SETUP;
import static warcardgame.MenuState.MAIN_MENU;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author David Zhang
 */
 
public class Application extends JFrame implements ActionListener {
    public Application() {
        // Set frame dimensions
        maxX = 900;
        maxY = 600;
        setSize(maxX, maxY);
        
        // Initialize other variables
        menuState = MAIN_MENU;
        ultimatePanel = new JPanel(new BorderLayout());
        
        // Top menu
        JPanel topMenuPanel = new JPanel();
        ultimatePanel.add(topMenuPanel, BorderLayout.NORTH);
        topMenuPanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        JLabel lblWar = new JLabel("War");
        lblWar.setHorizontalAlignment(SwingConstants.CENTER);
        lblWar.setFont(new Font("Lucida Grande", Font.BOLD, 26));
        topMenuPanel.add(lblWar);
        
        // Main area
        mainAreaPanel = new JPanel();
        mainAreaPanel.setLayout(new CardLayout());
        
        // First card panel: newGamePanel - i.e., a panel within the mainAreaPanel cardLayout
        firstPanel = new JPanel();
        mainAreaPanel.add(firstPanel, FIRST_PANEL);
        
        btnNewGame = new JButton("New game");
        btnNewGame.addActionListener(this);
        
        firstPanel.add(btnNewGame);
        
        btnManagePlayers = new JButton("Manage players");
        firstPanel.add(btnManagePlayers);
        
        // Second card panel: setupPanel
        setupPanel = new JPanel();
        mainAreaPanel.add(setupPanel, "SETUP_PANEL");
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.PAGE_AXIS));
        
        setNumCardsPanel = new JPanel();
        setupPanel.add(setNumCardsPanel);
        
        lblSetNumCards = new JLabel("Number of cards to be used: ");
        setNumCardsPanel.add(lblSetNumCards);
        
        String[] comboOptions = { "2","4","6","8","10","12","14","16","18","20","22","24","26" };
        
        comboBoxNumCards = new JComboBox(comboOptions);
        comboBoxNumCards.setSelectedIndex(12);
        setNumCardsPanel.add(comboBoxNumCards);
        
        startPanel = new JPanel();
        setupPanel.add(startPanel);
        
        btnStart = new JButton("Start!");
        btnStart.addActionListener(this);
        startPanel.add(btnStart);
        
        // Third card panel: gamePanel
        gamePanel = new Game();
        mainAreaPanel.add(gamePanel, GAME_PANEL);

        // Card layout prep
        ultimatePanel.add(mainAreaPanel, BorderLayout.CENTER);
        layout = (CardLayout)(mainAreaPanel.getLayout());
        layout.show(mainAreaPanel, "newGamePanel");

        // Deal with listeners
        gamePanel.addMouseListener(new MyMouseListener());
        // addKeyListener(new MyKeyListener());
        
        setContentPane(ultimatePanel);
    }
    
    public static void main(String[] args) {
        Application app = new Application();
        
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
    	switch (menuState) {
    	case MAIN_MENU:
    		if (e.getSource() == btnNewGame) {
    			menuState = GAME_SETUP;
    			layout.next(mainAreaPanel); // to setupPanel
    			
    		} else if (e.getSource() == btnManagePlayers) {
    			// HERE
    		}
    		break;
    	case GAME_SETUP:
    		if (e.getSource() == btnStart) {
    			menuState = GAME_RUNNING;
    			layout.next(mainAreaPanel); // to gamePanel
                startGame(); // initialize other members of Game class
    		}
    		break;
    	case GAME_RUNNING:
//            if (e.getSource() == ) {
//                
//            }
    		break;
    	}
    }
    
    private class MyMouseListener implements MouseListener {
    	@Override
        public void mouseClicked(MouseEvent e) {
            switch (menuState) {
            case GAME_RUNNING:
                System.out.println("mouseClicked");

                gamePanel.step();
                break;
            }
        }

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
    }

    private void startGame() {
        int numCards = Integer.parseInt((String) comboBoxNumCards.getSelectedItem()); // guaranteed to pass b/c of combo box
        gamePanel.setNumCards(numCards);
        gamePanel.initializeDecksAndPlayers();
    }    
    
    /* Instance fields */
    private MenuState menuState;
    private int maxX;
    private int maxY;

    // Panels
    private JPanel ultimatePanel;
    private Game gamePanel; // essential
    private CardLayout layout; // used with mainAreaPanel
    private JPanel mainAreaPanel, firstPanel, setupPanel; 
    
    // Components
    private JButton btnNewGame;
    private JPanel setNumCardsPanel;
    private JLabel lblSetNumCards;
    private JComboBox comboBoxNumCards;
    private JPanel startPanel;
    private JButton btnStart;
    private JButton btnManagePlayers;
    
    // CardLayout panel keys
    private static final String FIRST_PANEL = "FIRST_PANEL";
    private static final String SETUP_PANEL = "SETUP_PANEL";
    private static final String GAME_PANEL = "GAME_PANEL";
}