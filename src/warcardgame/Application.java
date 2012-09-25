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
        
        // First card panel: newGamePanel - i.e., a panel within the mainAreaPanel cardLayout
        newGamePanel = new JPanel();
        mainAreaPanel.add(newGamePanel, "newGamePanel");
        
        btnNewGame = new JButton("New game");
        btnNewGame.addActionListener(this);
        
        newGamePanel.add(btnNewGame);
        
        // Second card panel: setupPanel
        setupPanel = new JPanel();
        mainAreaPanel.add(setupPanel, "setupPanel");
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.PAGE_AXIS));
        
        setNumCardsPanel = new JPanel();
        setupPanel.add(setNumCardsPanel);
        
        lblSetNumCards = new JLabel("Number of cards per player: ");
        setNumCardsPanel.add(lblSetNumCards);
        
        String[] comboOptions = { "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26" };
        
        comboBoxNumCards = new JComboBox(comboOptions);
        comboBoxNumCards.setSelectedIndex(25);
        setNumCardsPanel.add(comboBoxNumCards);
        
        startPanel = new JPanel();
        setupPanel.add(startPanel);
        
        btnStart = new JButton("Start!");
        btnStart.addActionListener(this);
        startPanel.add(btnStart);
        
        // Third card panel: gamePanel
        gamePanel = new Game();
        mainAreaPanel.add(gamePanel, "gamePanel");

        // Card layout prep
        mainAreaPanel.setLayout(new CardLayout());
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
    			break;
    		}
    	case GAME_SETUP:
    		if (e.getSource() == btnStart) {
    			menuState = GAME_RUNNING;
    			layout.next(mainAreaPanel); // to gamePanel
    			break;
    		}
    	case GAME_RUNNING:
//            if (e.getSource() == ) {
//                
//            }
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
    
//    private class MyKeyListener implements KeyListener {
//        public void keyPressed(KeyEvent e) {
//            if (menuState == GAME_RUNNING) {
//            	System.out.println("keystroke detected in game running");
//                switch (e.getKeyCode()) {
//                    case KeyEvent.VK_SPACE:
//                        System.out.println("space bar hit");
//                        gamePanel.step();
//                        break;
//                }
//            }
//        }
//        
//        public void keyTyped(KeyEvent e) { }
//        public void keyReleased(KeyEvent e) { }
//        
//    }
    
    /* Instance fields */
    private MenuState menuState;
    private int maxX;
    private int maxY;

    // Panels
    private JPanel ultimatePanel;
    private Game gamePanel; // essential
    private CardLayout layout; // used with mainAreaPanel
    private JPanel mainAreaPanel, newGamePanel, setupPanel; 
    
    // Components
    private JButton btnNewGame;
    private JPanel setNumCardsPanel;
    private JLabel lblSetNumCards;
    private JComboBox comboBoxNumCards;
    private JPanel startPanel;
    private JButton btnStart;
    
    
}