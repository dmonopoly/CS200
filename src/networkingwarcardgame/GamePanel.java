package networkingwarcardgame;

import static networkingwarcardgame.GameState.CONTINUE_WAR;
import static networkingwarcardgame.GameState.DRAW;
import static networkingwarcardgame.GameState.PLAYER_1_GAME_VICTORY;
import static networkingwarcardgame.GameState.PLAYER_1_WAR_VICTORY;
import static networkingwarcardgame.GameState.PLAYER_2_GAME_VICTORY;
import static networkingwarcardgame.GameState.PLAYER_2_WAR_VICTORY;
import static networkingwarcardgame.GameState.SHOULD_CLEAR_CARDS;
import static networkingwarcardgame.GameState.SHOULD_EXIT_GAME;
import static networkingwarcardgame.GameState.SHOULD_PLAY_CARDS;
import static networkingwarcardgame.GameState.SHOULD_PLAY_WAR_FACE_DOWN;
import static networkingwarcardgame.GameState.SHOULD_PLAY_WAR_FACE_UP;
import static networkingwarcardgame.GameState.SHOULD_TEST_WAR;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Handles the actual card game, displaying images of cards
 * and progressing wars.
 * @author David Zhang
 */
public class GamePanel extends JPanel implements ActionListener{
	public GamePanel(String playerName, ActionListener theListener) {
		listener = theListener;
        p1ActiveCards = new ArrayList<Card>();
        p2ActiveCards = new ArrayList<Card>();
        gameState = SHOULD_PLAY_CARDS;
        numCards = 26;
        player1Name = playerName;

        setupPanels();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExitGame) {
        	// passes to Application to change menuState
        	listener.actionPerformed(new ActionEvent((Object) btnExitGame, ActionEvent.ACTION_PERFORMED, "Exit game"));
            resetGame();
        }
    }

    public void setPlayerName(String s) {
    	player1Name = s;
    }

    // Called from outside this class to time initialization properly
    public void initializeDecksAndPlayers() {
        // Prepare decks for players
        Deck deck1 = new Deck(numCards); // First initialize one deck with all the cards
        deck1.shuffle();
        Deck deck2 = new Deck(deck1.split()); // Split deck 1 in half and give the other half to deck 2

        // Testing only
//         testViaSeedingDecks(deck1, deck2);
        
        // The players!
        player1 = new Player(player1Name, deck1);
        player2 = new Player("Computer", deck2);

        /** Fill in inner panels */
        // Stuff in the outer panel, surrounding the arena
        p2Label = new JLabel("Computer card count: "+player2.cardCount());
        
        p2StatsPanel.add(p2Label);
        p2DeckPanel.add(new Card("b")); // Back of card to represent deck
        
        p1Label = new JLabel(player1Name+" card count: "+player1.cardCount());
        
        p1DeckPanel.add(new Card("b")); // Back of card to represent deck
        p1StatsPanel.add(p1Label, BorderLayout.SOUTH);
    }

    // Seed the deck (testing only)
    private void testViaSeedingDecks(Deck deck1, Deck deck2) {
    	deck1.removeCards(0, deck1.size());

    	deck1.add(new Card("3s"));
    	deck1.add(new Card("5s"));
    	deck1.add(new Card("6h"));
    	deck1.add(new Card("js"));
    	deck1.add(new Card("as"));

    	deck2.removeCards(0, deck2.size());

//    	deck2.add(new Card("4s"));
    	deck2.add(new Card("5d"));
    	deck2.add(new Card("6s"));
    	deck2.add(new Card("jh"));
    	deck2.add(new Card("ah"));

    	p.println("Deck 1:");
    	deck1.printCards();
    	p.println("\nDeck 2:");
    	deck2.printCards();
    }    
    
    // private void testP2RunsOutInWar() {
    // }
    
    // @param theNumberOfCards - should be an even number so you can /2 evenly
    public void setNumCards(int theNumberOfCards) {
        numCards = theNumberOfCards;
    }

    // KEY method: Continues the game, like drawing the next card
    // 2 states: show cards & clear cards
    public void step() {
    	p.println(gameState);
    	switch(gameState) {
    	case SHOULD_PLAY_CARDS:
    		try {
	    		p1ActiveCards.add(player1.playCard());
	    		p2ActiveCards.add(player2.playCard());
	
	    		int p1Value = p1ActiveCards.get(0).getValue();
	    		int p2Value = p2ActiveCards.get(0).getValue();
	    		if (p1Value == p2Value) {
	    			// WAR!
                    // p.println("War mode turned on");
	    			announcement.setText(WAR_STRING);
	    			gameState = SHOULD_PLAY_WAR_FACE_DOWN;
	    		} else {
	    			gameState = SHOULD_CLEAR_CARDS;
	    			if (p1Value > p2Value) {
	    				makeP2GiveCardsToP1();
    	   				// p.println("p1 victory");
	    			} else {
	    				makeP1GiveCardsToP2();
    	   				// p.println("p2 victory");
	    			}
	    		}
    	   		// p.println("Played cards normally");
    		} catch (Exception e) { // This happens if a player is dead (index out of bounds in playCard())
    			GameState g = checkForDeadPlayer();
    			endWar(g); // This should not receive "CONTINUE_WAR"... the only case that endWar doesn't check for, and the case that should never happen here (we're not in a war)
    		}
	    	break;
    	case SHOULD_PLAY_WAR_FACE_UP:
    	case SHOULD_PLAY_WAR_FACE_DOWN:
    	case SHOULD_TEST_WAR:
    		GameState resultOfWar = playWar();
   		    // p.println("Started a WAR!");
    		if (resultOfWar != CONTINUE_WAR) {
    			endWar(resultOfWar); // post: gameState = SHOULD_CLEAR_CARDS
       			// p.println("Just ended a war");
    		}		
    		break;
    	case SHOULD_CLEAR_CARDS:
    		gameState = SHOULD_PLAY_CARDS;
    		announcement.setText(""); // reset action
    		p1ActiveCards.clear();
    		p2ActiveCards.clear();
       		// p.println("Cleared arena");
    		break;
    	case SHOULD_EXIT_GAME:
    		// Manually fire "Exit game" event
            p.println("About to exit");
    		btnExitGame.doClick();
    	} // end switch statement
    	updateArena(); // Updates the card shown in the arena based on active cards
    }

    /** Private methods */
    // Updates the area where cards are shown based on active cards
    // Either adds cards or removes them from the corresponding panel
    // centerNorthPanel = p2; centerSouthPanel = p1
    private void updateArena() {
    	boolean addCards = true;
    	// Deal with removing cards
    	if (p1ActiveCards.isEmpty()) {
    		addCards = false;
    		p1ArenaPanel.removeAll();
    	}
    	if (p2ActiveCards.isEmpty()) {
    		addCards = false;
    		p2ArenaPanel.removeAll();
    	}
    	
    	try { // Catch errors with number of cards, and by default, exit the game
    		if (addCards) {
    			// Deal with adding cards
    			for (int i=0; i<p1ActiveCards.size(); i++) {
    				p2ArenaPanel.add(p2ActiveCards.get(i)); // p2ActiveCards should be same size as p1ActiveCards
    				p1ArenaPanel.add(p1ActiveCards.get(i));
    			}
    		}
    	} catch (Exception e) {
    		// The game should exit by the next click
    		p.println("AHHH: "+e.getStackTrace());
    	}
    	
    	// Update stats
    	p2Label.setText("Computer card count: "+player2.cardCount());
    	p1Label.setText(player1Name+" card count: "+player1.cardCount());
    	
    	this.updateUI();
    }
    
    // Plays out HALF a war sequence in one through; this method is called twice if there is a one-round war.
    // returns the proper "War over" state
    // post: returns "CONTINUE_WAR" if the war should continue; otherwise, the proper ending war state
    private GameState playWar() {
		// If either player has 0 cards, we'd have an error - just check for the dead player now!
		if (player1.cardCount() == 0 || player2.cardCount() == 0)
			return checkForDeadPlayer();
    	
		// Otherwise, consider playing the next War card - face up or down
    	switch(gameState) {
    	case SHOULD_PLAY_WAR_FACE_DOWN:
    		gameState = SHOULD_PLAY_WAR_FACE_UP;
    		p1ActiveCards.add(player1.playFaceDownCard());
    		p2ActiveCards.add(player2.playFaceDownCard());
    		return CONTINUE_WAR;
    		//        	case SHOULD_PLAY_WAR_FACE_UP:
    	default: //SHOULD_PLAY_WAR_FACE_UP + SHOULD_TEST_WAR
    		gameState = SHOULD_TEST_WAR;

    		p1ActiveCards.add(player1.playCard());
    		p2ActiveCards.add(player2.playCard()); 

    		int p1Val = p1ActiveCards.get(p1ActiveCards.size()-1).getValue();
    		int p2Val = p2ActiveCards.get(p2ActiveCards.size()-1).getValue(); // get values of last card for both p1 and p2
    		if (p1Val == p2Val) {
    			gameState = SHOULD_PLAY_WAR_FACE_DOWN;
    			return CONTINUE_WAR; // Play war again
    		} else if (p1Val > p2Val) {
    			return PLAYER_1_WAR_VICTORY; // Player 1 wins just the war
    		} else {
    			return PLAYER_2_WAR_VICTORY; // Player 2 wins just the war
    		}
    	}
    }
    
    // Returns proper ending war state
    private GameState checkForDeadPlayer() {
    	boolean p1isOut = player1.cardCount() == 0;
        boolean p2isOut = player2.cardCount() == 0;
        boolean lastTwoCardsInPlayAreFaceDown;
        if (!p1ActiveCards.isEmpty() && !p2ActiveCards.isEmpty())
        	lastTwoCardsInPlayAreFaceDown = !p1ActiveCards.get(p1ActiveCards.size()-1).getFaceUp() && !p2ActiveCards.get(p2ActiveCards.size()-1).getFaceUp();
        else
        	lastTwoCardsInPlayAreFaceDown = false;
        
        if (p1isOut && p2isOut && lastTwoCardsInPlayAreFaceDown) {
            // If they're both out of cards, and the last cards they played are face down, it's a draw
            return DRAW;
        } else if (p1isOut && p2isOut) { // Last two cards in play are face up
            // If they're both out of cards, but the last cards they played are face up, 
        	// compare the cards for a last-minute battle
            int p1Val = p1ActiveCards.get(p1ActiveCards.size()-1).getValue();
    		int p2Val = p2ActiveCards.get(p2ActiveCards.size()-1).getValue();
    		if (p1Val == p2Val)
    		    return DRAW; // Just end it
    		else if (p1Val > p2Val)
    		    return PLAYER_1_GAME_VICTORY; // Player 1 wins the entire game!
            else
    		    return PLAYER_2_GAME_VICTORY; // Player 2 wins the entire game!
        } else if (p2isOut) {
            return PLAYER_1_GAME_VICTORY;
        } else if (p1isOut) {
        	return PLAYER_2_GAME_VICTORY;
        } else {
            // p.println("Something is amiss.");
        	return DRAW;
        }
    }
    
    // Concludes a war (not the game, but it could), determining what to do with the ending GameState of the war
    // e.g., give cards to player1 if player1 won the war, but player2 still has cards
    // post: gameState = SHOULD_CLEAR_CARDS 
    private void endWar(GameState state) {
    	p.println("Ending the war");
    	switch (state) {
    	case PLAYER_1_WAR_VICTORY:
   	        // p.println("p1 war victory");
    		makeP2GiveCardsToP1(); // p2 gives cards to player1
    		gameState = SHOULD_CLEAR_CARDS;
    		break;
    	case PLAYER_2_WAR_VICTORY:
   	        // p.println("p2 war victory");
    		makeP1GiveCardsToP2(); // p1 gives cards to player2
    		gameState = SHOULD_CLEAR_CARDS;
    		break;
    	case DRAW:
       	    // p.println("draw");
    		concludeGame(0);
    		break;
    	case PLAYER_1_GAME_VICTORY:
       	    // p.println("p1 game victory");
    		concludeGame(1);
    		break;
    	case PLAYER_2_GAME_VICTORY:
       	    // p.println("p2 game victory");
    		concludeGame(2);
    		break;
    	}
    	
    }
    
    // Concludes the game
    // @params i: 0 -> draw, 1 -> player1 victory, 2 -> player2 victory 
    private void concludeGame(int i) {
   	    p.println("Concluding the game: "+i);
    	String pre = "<html><b><span style='font-size: 20px'>Game Over: ";
    	switch(i) {
    	case 0:
    		p1DeckPanel.removeAll(); // hide p2's deck
    		p1StatsPanel.removeAll(); // hide p1's deck
    		announcement.setText(pre+"DRAW");
    		break;
    	case 1:
    		p1DeckPanel.removeAll(); // hide p2's deck
    		announcement.setText(pre+player1Name+" WINS");
    		listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Player 1 victory"));
    		break;
    	case 2:
    		p1StatsPanel.removeAll(); // hide p1's deck
    		announcement.setText(pre+"COMPUTER WINS");
    		listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Player 2 victory"));
    		break;
    	}
    	updateArena();
    	gameState = SHOULD_EXIT_GAME;
    }
    
    // // Reveal cards in arena (mainly face down cards once a war is over) - for future versions
    // private void revealFaceDownCards() {
    //     // Loop through all active cards 
    //     for (int i=0; i<p1ActiveCards.size(); i++) {
    //         p1ActiveCards.get(i).setFaceUp(true);
    //         p2ActiveCards.get(i).setFaceUp(true);
    //     }
    // }

    // Card-giving methods
    private void makeP1GiveCardsToP2() {
    	announcement.setText("<html><b><span style='font-size: 20px'>Computer wins");
    	for (int i=0; i<p1ActiveCards.size(); i++) {
			player2.addCardToDeckBottom(p1ActiveCards.get(i));
			player2.addCardToDeckBottom(p2ActiveCards.get(i)); // p2ActiveCards is the same size
    	}
    }
    private void makeP2GiveCardsToP1() {
    	announcement.setText("<html><b><span style='font-size: 20px'>"+player1Name+" wins");
    	for (int i=0; i<p2ActiveCards.size(); i++) {
    		player1.addCardToDeckBottom(p1ActiveCards.get(i));
			player1.addCardToDeckBottom(p2ActiveCards.get(i));
    	}
    }    
    
    private void resetGame() {
        // Reset game state
        gameState = SHOULD_PLAY_CARDS;

        // Reset panels - removeAll for panels that dynamically get Cards added to them
        p1ArenaPanel.removeAll();
        p2ArenaPanel.removeAll();
        p1DeckPanel.removeAll();
        p2DeckPanel.removeAll();
        p1StatsPanel.removeAll();
        p2StatsPanel.removeAll();

        // Announcement text
        announcement.setText("");

        // Players' active cards
        p1ActiveCards.clear();
        p2ActiveCards.clear();
    }

    // Called in constructor
    private void setupPanels() {
        /** Creates the primary panels */
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // The sections of the mainPanel
        topPanel = new JPanel(new BorderLayout()); // Player 2 info
        arenaPanel = new JPanel(new BorderLayout()); // "arena" where active cards are shown
        									// divided into BorderLayout, for NORTH (player 2 cards) vs. SOUTH (player 1 cards)
        p1GeneralPanel = new JPanel(new BorderLayout()); // Player 1 info
        
	        // Within centerPanel
	        p2ArenaPanel = new JPanel(); // for NORTH side of arena, player 2's active cards
	        p2ArenaPanel.setLayout(new FlowLayout());//new BoxLayout(centerNorthPanel, BoxLayout.X_AXIS));
	        p1ArenaPanel = new JPanel(); // for SOUTH side of arena, player 1's active cards
	        p1ArenaPanel.setLayout(new FlowLayout());//new BoxLayout(centerSouthPanel, BoxLayout.X_AXIS));
	        announcementPanel = new JPanel();
	        p1DeckPanel = new JPanel(new FlowLayout()); // p1 deck
	        p1StatsPanel = new JPanel(new FlowLayout());

        // Combines panels with panels
        arenaPanel.add(p2ArenaPanel, BorderLayout.NORTH);
        arenaPanel.add(announcementPanel, BorderLayout.CENTER);
        arenaPanel.add(p1ArenaPanel, BorderLayout.SOUTH);
        p1GeneralPanel.add(p1DeckPanel, BorderLayout.NORTH);
        p1GeneralPanel.add(p1StatsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Options
        optionsPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) optionsPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        topPanel.add(optionsPanel, BorderLayout.NORTH);
        
        btnExitGame = new JButton("Exit game");
        btnExitGame.addActionListener(this);
        
        optionsPanel.add(btnExitGame);
        
        p2GeneralPanel = new JPanel();
        topPanel.add(p2GeneralPanel, BorderLayout.SOUTH);
        p2GeneralPanel.setLayout(new BorderLayout(0, 0));
        
        // Within north and southPanel
        p2StatsPanel = new JPanel(new FlowLayout()); // p2 stats
        p2GeneralPanel.add(p2StatsPanel, BorderLayout.NORTH);
        p2DeckPanel = new JPanel(new FlowLayout()); // p2 deck
        p2GeneralPanel.add(p2DeckPanel, BorderLayout.SOUTH);
        mainPanel.add(arenaPanel, BorderLayout.CENTER);
        mainPanel.add(p1GeneralPanel, BorderLayout.SOUTH);
        announcementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        // The announcement label!
        announcement = new JLabel();
        announcementPanel.add(announcement);
        
        setLayout(new BorderLayout());
        
        this.add(mainPanel);
    }
    
    /** Instance fields */
    private Printer p = new Printer();
    private ActionListener listener;
    private int numCards; // the TOTAL num cards that both players play with
    private Player player1, player2; // you are player1, computer is player2
    private ArrayList<Card> p1ActiveCards;
    private ArrayList<Card> p2ActiveCards;
    private GameState gameState; 
        // to keep track of what type of step() to perform
    
    // For stats
    private JLabel announcement; // e.g., "War!"
    private JLabel p2Label, p1Label;
    
    private JPanel topPanel, arenaPanel, p1GeneralPanel;
    private JPanel p2ArenaPanel, p1ArenaPanel, announcementPanel;
    private JPanel p2StatsPanel, p2DeckPanel, p1DeckPanel, p1StatsPanel;
    
    private static final String WAR_STRING = "<html><b><span style='font-size: 30px'>WAR</span></b></html>";

    private static final long serialVersionUID = 1L;
    private JButton btnExitGame;
    private JPanel optionsPanel;
    private JPanel p2GeneralPanel;

    private String player1Name;
}
