package networkingwarcardgame;

import static networkingwarcardgame.MenuState.GAME_RUNNING;
import static networkingwarcardgame.MenuState.GAME_SETUP;
import static networkingwarcardgame.MenuState.MAIN_MENU;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author David Zhang
 */

public class Application extends JFrame implements ActionListener, Client {
    public Application(int portNumber, String aHostName) {
    	// Connection stuff
    	portNum = portNumber;
    	hostName = aHostName;
    	connect();
    	
        // Set frame dimensions
        maxX = 900;
        maxY = 750;
        setSize(maxX, maxY);
        
        // Set actions on exiting the entire application
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
            	p.println("Clean up right before closing");
                writeData(); // save data
                sendToServer(Message.CLIENT_EXITED); // notify server that a client has left
                
                // Close connection
                try {
                	out.close();
                	in.close();
                	socket.close();
                } catch (Exception e) {
                	p.println("Did not close connection");
                }
            }
        });

        // Initialize other variables
        menuState = MAIN_MENU;
        existingPlayerPanels = new ArrayList<PlayerPanel>(); 
        chosenPlayerIndex = 0;
        
        // Read in data, if any
        readData();
        
        /** Handle panels*/
        ultimatePanel = new JPanel(new BorderLayout());
        
        // Top menu
        JPanel topMenuPanel = new JPanel();
        ultimatePanel.add(topMenuPanel, BorderLayout.NORTH);
        topMenuPanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        JLabel lblWar = new JLabel("War");
        lblWar.setHorizontalAlignment(SwingConstants.CENTER);
        lblWar.setFont(new Font("Lucida Calligraphy", Font.BOLD, 26));
        topMenuPanel.add(lblWar);
        
        // Main area
        mainAreaPanel = new JPanel();
        mainAreaPanel.setLayout(new CardLayout());
        
        // First card panel: newGamePanel - i.e., a panel within the mainAreaPanel cardLayout
        firstPanel = new JPanel();
        mainAreaPanel.add(firstPanel, FIRST_PANEL);
        firstPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        firstBoxPanel = new JPanel();
        firstPanel.add(firstBoxPanel);
        firstBoxPanel.setLayout(new BoxLayout(firstBoxPanel, BoxLayout.Y_AXIS));
        
        firstButtonsPanel = new JPanel();
        firstBoxPanel.add(firstButtonsPanel);
        firstButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        btnNewGame = new JButton("New game");
        btnNewGame.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        firstButtonsPanel.add(btnNewGame);
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewGame.addActionListener(this);
        
        btnManagePlayers = new JButton("Manage players");
        btnManagePlayers.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        firstButtonsPanel.add(btnManagePlayers);
        btnManagePlayers.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnManagePlayers.addActionListener(this);
        
        firstLabelsPanel = new JPanel();
        firstBoxPanel.add(firstLabelsPanel);
        firstLabelsPanel.setLayout(new BoxLayout(firstLabelsPanel, BoxLayout.Y_AXIS));
        
        lblSelectedPlayer = new JLabel("Selected player: ");
        lblSelectedPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSelectedPlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        firstLabelsPanel.add(lblSelectedPlayer);
        
        lblPlayerName = new JLabel();
        lblPlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPlayerName.setText(getDefaultPlayerName());
        lblPlayerName.setFont(new Font("Lucida Grande", Font.BOLD, 21));
        firstLabelsPanel.add(lblPlayerName);
        
        // Second card panel: setupPanel
        setupPanel = new JPanel();
        mainAreaPanel.add(setupPanel, "SETUP_PANEL");
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.PAGE_AXIS));
        
        setNumCardsPanel = new JPanel();
        setupPanel.add(setNumCardsPanel);
        
        lblSetNumCards = new JLabel("Number of cards to be used: ");
        setNumCardsPanel.add(lblSetNumCards);
        
        String[] comboOptions = { "2","4","6","8","10","12","14","16","18","20","22","24","26","28","30","32","34","36","38","40","42","44","46","48","50","52" };
        
        comboBoxNumCards = new JComboBox(comboOptions);
        comboBoxNumCards.setSelectedIndex(15);
        setNumCardsPanel.add(comboBoxNumCards);
        
        startPanel = new JPanel();
        setupPanel.add(startPanel);
        
        btnStart = new JButton("Start!");
        btnStart.setFont(new Font("Lucida Grande", Font.PLAIN, 64));
        btnStart.addActionListener(this);
        startPanel.add(btnStart);
        
        // Third card panel: gamePanel
        gamePanel = new GamePanel(lblPlayerName.getText(), this); // recreated for each new game
        mainAreaPanel.add(gamePanel, GAME_PANEL);

        // Card layout prep
        ultimatePanel.add(mainAreaPanel, BorderLayout.CENTER);
        layout = (CardLayout)(mainAreaPanel.getLayout());
        layout.show(mainAreaPanel, "newGamePanel");
        
                // Fourth card panel: panel for players
                playerControlsPanel = new JPanel();
                mainAreaPanel.add(playerControlsPanel, PLAYER_CONTROLS_PANEL);
                playerControlsPanel.setLayout(new BoxLayout(playerControlsPanel, BoxLayout.Y_AXIS));
                
                playerTopPanel = new JPanel();
                playerControlsPanel.add(playerTopPanel);
                
                playerTopBoxPanel = new JPanel();
                playerTopPanel.add(playerTopBoxPanel);
                playerTopBoxPanel.setLayout(new BoxLayout(playerTopBoxPanel, BoxLayout.Y_AXIS));
                
                lblManagePlayers = new JLabel("Manage Players");
                lblManagePlayers.setAlignmentX(Component.CENTER_ALIGNMENT);
                playerTopBoxPanel.add(lblManagePlayers);
                lblManagePlayers.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 25));
                
                btnCreatePlayer = new JButton("Create player");
                btnCreatePlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
                btnCreatePlayer.addActionListener(this);
                btnCreatePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
                playerTopBoxPanel.add(btnCreatePlayer);
                
                playerIndexPanel = new JPanel();
                playerControlsPanel.add(playerIndexPanel);
                playerIndexPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                
                donePanel = new JPanel();
                playerControlsPanel.add(donePanel);
                
                btnDone = new JButton("Done");
                btnDone.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
                btnDone.addActionListener(this);
                donePanel.add(btnDone);
        
        // Deal with listeners
        gamePanel.addMouseListener(new MyMouseListener());
        // addKeyListener(new MyKeyListener());
        
        // Fill the index of players! (Uses read-in player data)
        for (int i=0; i<existingPlayerPanels.size(); i++) {
            playerIndexPanel.add(existingPlayerPanels.get(i));
        }
        
        // Deal with menu
        prepareMenu();

        setContentPane(ultimatePanel);
        setJMenuBar(menuBar);
    }
    
    private void prepareMenu() {
    	menuBar = new JMenuBar();

        // First menu
        menu = new JMenu("File");
        menuBar.add(menu);

        // "Help" Submenu
        submenu = new JMenu("Help");

        menuItemAbout = new JMenuItem("About");
        menuItemAbout.addActionListener(this);
        submenu.add(menuItemAbout);

        menu.add(submenu);
        
        menu.addSeparator();

        // Save and exit menu items
        menuItemSave = new JMenuItem("Save player data");
        menuItemSave.addActionListener(this);
        menu.add(menuItemSave);

        menuItemExit = new JMenuItem("Exit application (saves automatically)");
        menuItemExit.addActionListener(this);
        menu.add(menuItemExit);
    }
    
    // Returns the default player name upon start up
    private String getDefaultPlayerName() {
        if (!existingPlayerPanels.isEmpty()) {
            return existingPlayerPanels.get(chosenPlayerIndex).getName();
        } else {
            return NO_SELECTED_PLAYER;
        }
    }
    
    private void readData() {
    	try {
    		reader = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME));

    		// Read here
            Player p = null;
            while ((p = (Player) reader.readObject()) != null) {
                existingPlayerPanels.add(new PlayerPanel(p, this));
            }
    		reader.close();
    	} catch (EOFException e) {
    		p.println("End of file reading");
    	} catch (IOException e) {
    		p.println("No file to read");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void writeData() {
        try {
            writer = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_NAME));

            // Write here
            for (PlayerPanel panel: existingPlayerPanels) {
                writer.writeObject(panel.getPlayer());
            }
            
            writer.close();
        } catch (Exception e) {
        	e.printStackTrace();
//            p.println("blahhh no game.sav file.. but shouldn't that be fine? it should just create it then.");
        }
    }
    
    public static void main(String[] args) {
    	// Get connection info
    	// Port num
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
		
		// Host name
		String hostName = "localhost";
		System.out.print("Enter host name (default: localhost): ");
		String nextLine = in.nextLine();
		if (nextLine.equals("")) {
			System.out.println("Using default host name");
			hostName = "localhost";
		} else {
			hostName = nextLine;
		}
    	
        Application app = new Application(portNum, hostName); // a JFrame
        
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        p.println("action command: " + e.getActionCommand());
        switch (menuState) {
        case MAIN_MENU:
        	if (e.getSource() == btnNewGame) {
        		if (lblPlayerName.getText().equals(NO_SELECTED_PLAYER)) {
                    JOptionPane.showMessageDialog(this, "You must have a selected player.", "Notification", JOptionPane.INFORMATION_MESSAGE);
                } else {
        			changeMenuState(GAME_SETUP);
        			layout.show(mainAreaPanel, SETUP_PANEL); // to setupPanel
                }
        	} else if (e.getSource() == btnManagePlayers) {
        		refreshPlayers();
        		layout.show(mainAreaPanel, PLAYER_CONTROLS_PANEL); // to playersPanel
        	} else if (e.getSource() == btnDone) {
        		layout.show(mainAreaPanel, FIRST_PANEL);
        	} else if (e.getSource() == btnCreatePlayer) {
        		// Create a new player
        		String name = JOptionPane.showInputDialog("Enter a name: "); // Returns "null" if canceled
        		if (name != null && !name.trim().equals("")) {
        			Player newPlayer = new Player(name, new Deck());
        			existingPlayerPanels.add(new PlayerPanel(newPlayer, this));

        			playerIndexPanel.add(existingPlayerPanels.get(existingPlayerPanels.size()-1));
        			playerIndexPanel.updateUI();
        		}
        	} else if (e.getActionCommand().contains("Selected player")) {
        		// Select a player
        		String tmp = e.getActionCommand();
        		String pName = tmp.substring(tmp.indexOf(":")+2);

        		chosenPlayerIndex = findPlayerIndex(pName);
                lblPlayerName.setText(pName); // or existingPlayerPanels.get(chosenPlayerIndex).getName()
        		
                JOptionPane.showMessageDialog(this, "Player selected", "Notification", JOptionPane.PLAIN_MESSAGE);
        	} 
        	break;
        case GAME_SETUP:
        	if (e.getSource() == btnStart) {
        		changeMenuState(GAME_RUNNING);
        		layout.show(mainAreaPanel, GAME_PANEL); // to gamePanel
        		startGame(); // initialize other members of Game class
        	}
        	break;
        case GAME_RUNNING:
        	if (e.getActionCommand() == "Exit game") { // ActionCommand is determined by JButton's text
        		// Return to main menu
        		menuState = MAIN_MENU;
        		layout.show(mainAreaPanel, FIRST_PANEL);
        	} else if (e.getActionCommand() == "Player 1 victory") { // from GamePanel
        		// After execution, GamePanel should still be running. But 
        		existingPlayerPanels.get(chosenPlayerIndex).addOneWin();
        	} else if (e.getActionCommand() == "Player 2 victory") { // from GamePanel
        		existingPlayerPanels.get(chosenPlayerIndex).addOneLoss();
        	} // need to guarantee changed menustate? I guess not. 
        	break;
        }
        
        if (e.getSource() == menuItemAbout) {
    		aboutBox = new AboutBox(null);
    		aboutBox.setLocationRelativeTo(this);
    		aboutBox.setVisible(true);
    	} else if (e.getSource() == menuItemSave) {
    		writeData();
    		JOptionPane.showMessageDialog(this, "Data saved", "Notification", JOptionPane.INFORMATION_MESSAGE);
    	} else if (e.getSource() == menuItemExit) {
    		// Fire the closing event! Leads to windowClosing from WindowListener so it DOES save
    		WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
    	}
    }
    
    // Returns the index of the player in existingPlayerPanels based on name
    private int findPlayerIndex(String name) {
    	for (int i=0; i<existingPlayerPanels.size(); i++)
    		if (name.equals(existingPlayerPanels.get(i).getName()))
    			return i;

    	return -1;
    }

    // Refresh all playerPanels in the manage players panel
    private void refreshPlayers() {
    	for (PlayerPanel panel : existingPlayerPanels) {
    		panel.refresh();
    	}
    }

    private void changeMenuState(MenuState state) {
    	if (menuState != state) {
    		menuState = state;
    		p.println("Menu state changed to "+state);
    	}
    }

    private class MyMouseListener implements MouseListener {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	}

    	@Override
    	public void mouseEntered(MouseEvent arg0) {
    	}

    	@Override
    	public void mouseExited(MouseEvent arg0) {
    	}

    	@Override
    	public void mousePressed(MouseEvent arg0) {
    		switch (menuState) {
    		case GAME_RUNNING:
    			gamePanel.step();
    			break;
    		default:
    			break;
    		}
    	}

    	@Override
    	public void mouseReleased(MouseEvent arg0) {
    	}
    }

    private void startGame() {
        int numCards = Integer.parseInt((String) comboBoxNumCards.getSelectedItem()); // guaranteed to pass b/c of combo box
        gamePanel.setNumCards(numCards);
        gamePanel.setPlayerName(lblPlayerName.getText());
        gamePanel.initializeDecksAndPlayers();
    }
    
    // Client methods
    @Override
	public void connect() {
//    	p.println("host name: "+hostName);
//    	p.println("port num: "+portNum);
    	
		System.out.println("Connecting...");
		socket = null;
        out = null;
        in = null;

        try {
            socket = new Socket(hostName, portNum);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            System.exit(1);
        }

        System.out.println("Done connecting");
	}
    
	@Override
	public void sendToServer(String msg) {
		try {
			out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listenToServer() {
		try {
			String msg = in.readLine();
			processMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void processMessage(String msg) {
		// Decide action based on message from server
		if (msg.equals(Message.TEST_CLIENT)) {
			System.out.println("Client test passed.");
		}		
	}
    
    /* Instance fields */
    private Printer p = new Printer();
    private int maxX, maxY;
    private int chosenPlayerIndex;
    private MenuState menuState;
    
    // CardLayout panel keys
    private static final String FIRST_PANEL = "FIRST_PANEL";
    private static final String SETUP_PANEL = "SETUP_PANEL";
    private static final String GAME_PANEL = "GAME_PANEL";
    private static final String PLAYER_CONTROLS_PANEL = "PLAYER_CONTROLS_PANEL";

    // IO
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private static final String SAVE_FILE_NAME = "game.sav";
    private static final String NO_SELECTED_PLAYER = "None. Please create & select a player.";

    // Connection stuff
    private String hostName;
    private int portNum;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    // Menu stuff
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private JMenuItem menuItemAbout, menuItemSave, menuItemExit;
    
    private AboutBox aboutBox;
    
    // Panels & Components    
    private JPanel ultimatePanel;
    private GamePanel gamePanel; // essential
    private CardLayout layout; // used with mainAreaPanel
    private JPanel mainAreaPanel, firstPanel, setupPanel; 
    private JPanel playerControlsPanel;
    private JPanel playerTopBoxPanel;
    private JPanel donePanel;
    private JPanel playerIndexPanel;
    private ArrayList<PlayerPanel> existingPlayerPanels;
    
    private JButton btnNewGame;
    private JPanel setNumCardsPanel;
    private JLabel lblSetNumCards;
    private JComboBox comboBoxNumCards;
    private JPanel startPanel;
    private JButton btnStart;
    private JLabel lblManagePlayers;
    private JButton btnManagePlayers;
    private JButton btnDone;

    private JLabel lblSelectedPlayer;
    private JPanel firstBoxPanel;
    private JPanel firstButtonsPanel;
    private JLabel lblPlayerName;
    private JPanel firstLabelsPanel;
    private JButton btnCreatePlayer;
    private JPanel playerTopPanel;
}