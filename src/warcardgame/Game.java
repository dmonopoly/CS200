package warcardgame;

import static warcardgame.GameState.CONTINUE_WAR;
import static warcardgame.GameState.DRAW;
import static warcardgame.GameState.PLAYER_1_GAME_VICTORY;
import static warcardgame.GameState.PLAYER_1_WAR_VICTORY;
import static warcardgame.GameState.PLAYER_2_GAME_VICTORY;
import static warcardgame.GameState.PLAYER_2_WAR_VICTORY;
import static warcardgame.GameState.SHOULD_CLEAR_CARDS;
import static warcardgame.GameState.SHOULD_EXIT_GAME;
import static warcardgame.GameState.SHOULD_PLAY_CARDS;
import static warcardgame.GameState.SHOULD_PLAY_WAR_FACE_DOWN;
import static warcardgame.GameState.SHOULD_PLAY_WAR_FACE_UP;
import static warcardgame.GameState.SHOULD_TEST_WAR;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Handles the actual card game, displaying images of cards
 * and progressing wars.
 * @author David Zhang
 */
public class Game extends JPanel {
	public Game() {
        p1ActiveCards = new ArrayList<Card>();
        p2ActiveCards = new ArrayList<Card>();
        gameState = SHOULD_PLAY_CARDS;
        numCards = 26; // default, should be re-set later
        
        /** IO */
        setupPanels();
    }

    public void readData() {
        try {
            fis = new FileInputStream(SAVE_FILE_NAME);
            ois = new ObjectInputStream(fis);
            
            // Read here

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void writeData() {
        try {
            fos = new FileOutputStream(SAVE_FILE_NAME);
            oos = new ObjectOutputStream(fos);

            // Write here
            
            oos.close();
        } catch (Exception e) {
            System.out.println("blahhh no game.sav file.. but shouldn't that be fine? it should just create it then.");
        }
    }

    public void initializeDecksAndPlayers() {
//    	setupPanels();
    	
        // Prepare decks for players
        Deck deck1 = new Deck(numCards);
        deck1.shuffle();
        Deck deck2 = new Deck(deck1.split());

        // The players!
        player1 = new Player("player 1", deck1);
        player2 = new Player("player 2", deck2);

        /** Fill in inner panels */
        // Stuff in the outer panel, surrounding the arena
        p2Label = new JLabel("Player 2 card count: "+player2.cardCount());
        
        northNorthPanel.add(p2Label);
        northSouthPanel.add(new Card("b")); // Back of card to represent deck
        
        p1Label = new JLabel("Player 1 card count: "+player1.cardCount());
        
        southNorthPanel.add(new Card("b")); // Back of card to represent deck
        southSouthPanel.add(p1Label, BorderLayout.SOUTH);
    }

    public void setNumCards(int theNumberOfCards) {
        numCards = theNumberOfCards;
    }

    // KEY method: Continues the game, like drawing the next card
    // 2 states: show cards & clear cards
    public void step() {
    	switch(gameState) {
    	case SHOULD_PLAY_CARDS:
    		try {
	    		p1ActiveCards.add(player1.playCard());
	    		p2ActiveCards.add(player2.playCard());
	
	    		int p1Value = p1ActiveCards.get(0).getValue();
	    		int p2Value = p2ActiveCards.get(0).getValue();
	    		if (p1Value == p2Value) {
	    			// WAR!
                    // System.out.println("War mode turned on");
	    			announcement.setText(WAR_STRING);
	    			gameState = SHOULD_PLAY_WAR_FACE_DOWN;
	    		} else {
	    			gameState = SHOULD_CLEAR_CARDS;
	    			if (p1Value > p2Value) {
	    				makeP2GiveCardsToP1();
    	   				// System.out.println("p1 victory");
	    			} else {
	    				makeP1GiveCardsToP2();
    	   				// System.out.println("p2 victory");
	    			}
	    		}
    	   		// System.out.println("Played cards normally");
    		} catch (Exception e) { // This happens if a player is dead (index out of bounds in playCard())
    			GameState g = checkForDeadPlayer();
    			endWar(g); // This should not receive "CONTINUE_WAR"... the only case that endWar doesn't check for, and the case that should never happen here (we're not in a war)
    		}
	    	break;
    	case SHOULD_PLAY_WAR_FACE_UP:
    	case SHOULD_PLAY_WAR_FACE_DOWN:
    	case SHOULD_TEST_WAR:
    		GameState resultOfWar = playWar();
   		    // System.out.println("Started a WAR!");
    		if (resultOfWar != CONTINUE_WAR) {
    			endWar(resultOfWar); // post: gameState = SHOULD_CLEAR_CARDS
       			// System.out.println("Just ended a war");
    		}		
    		break;
    	case SHOULD_CLEAR_CARDS:
    		gameState = SHOULD_PLAY_CARDS;
    		announcement.setText(""); // reset action
    		p1ActiveCards.clear();
    		p2ActiveCards.clear();
       		// System.out.println("Cleared arena");
    		break;
    	case SHOULD_EXIT_GAME:
    		System.exit(0);
    		break;
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
    		centerSouthPanel.removeAll();
    	}
    	if (p2ActiveCards.isEmpty()) {
    		addCards = false;
    		centerNorthPanel.removeAll();
    	}
    	
    	if (addCards) {
        	// Deal with adding cards
        	for (int i=0; i<p1ActiveCards.size(); i++) {
        		centerNorthPanel.add(p2ActiveCards.get(i)); // p2ActiveCards should be same size as p1ActiveCards
        		centerSouthPanel.add(p1ActiveCards.get(i));
        	}
    	}

    	// Update stats
    	p2Label.setText("Player 2 card count: "+player2.cardCount());
    	p1Label.setText("Player 1 card count: "+player1.cardCount());
    	

    	centerNorthPanel.revalidate();
    	centerCenterPanel.revalidate();
    	centerSouthPanel.revalidate();
    	announcement.revalidate();
    	revalidate();
    	this.updateUI();
    	
        // System.out.println("updated");
    }
    
    // Plays out HALF a war sequence in one through; this method is called twice if there is a one-round war.
    // returns the proper "War over" state
    // post: returns "CONTINUE_WAR" if the war should continue; otherwise, the proper ending war state
    private GameState playWar() {
        try {
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
//        		return CONTINUE_WAR;
//        	default: //SHOULD_TEST_WAR:
        		int p1Val = p1ActiveCards.get(p1ActiveCards.size()-1).getValue();
        		int p2Val = p2ActiveCards.get(p2ActiveCards.size()-1).getValue(); // get values of last card for both p1 and p2
        		if (p1Val == p2Val) {
        			gameState = SHOULD_PLAY_WAR_FACE_DOWN;
        		    return CONTINUE_WAR; // Play war again
        		} else if (p1Val > p2Val) {
        		    return PLAYER_1_WAR_VICTORY; // Player 1 wins just the war, so give him the cards
        		} else {
        		    return PLAYER_2_WAR_VICTORY; // Player 2 wins just the war, so give him the cards
        		}
        	}
        } catch (Exception e) { // This happens when a player runs out of cards.
            // System.out.println("Exception: "+e.getMessage());
        	return checkForDeadPlayer();
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
        
        if (p1isOut && p2isOut && lastTwoCardsInPlayAreFaceDown)
            // If they're both out of cards, and the last cards they played are face down, it's a draw
            return DRAW;
        else if (p1isOut && p2isOut) {
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
            // System.out.println("Something is amiss.");
        	return DRAW;
        }
    }
    
    // Concludes a war (not the game, but it could), determining what to do with the ending GameState of the war
    // e.g., give cards to player1 if player1 won the war, but player2 still has cards
    // post: gameState = SHOULD_CLEAR_CARDS 
    private void endWar(GameState state) {
    	switch (state) {
    	case PLAYER_1_WAR_VICTORY:
   	        // System.out.println("p1 war victory");
    		makeP2GiveCardsToP1(); // p2 gives cards to player1
    		gameState = SHOULD_CLEAR_CARDS;
    		break;
    	case PLAYER_2_WAR_VICTORY:
   	        // System.out.println("p2 war victory");
    		makeP1GiveCardsToP2(); // p1 gives cards to player2
    		gameState = SHOULD_CLEAR_CARDS;
    		break;
    	case DRAW:
       	    // System.out.println("draw");
    		concludeGame(0);
    		break;
    	case PLAYER_1_GAME_VICTORY:
       	    // System.out.println("p1 game victory");
    		concludeGame(1);
    		break;
    	case PLAYER_2_GAME_VICTORY:
       	    // System.out.println("p2 game victory");
    		concludeGame(2);
    		break;
    	}
    	
    }
    
    // Concludes the game
    // @params i: 0 -> draw, 1 -> player1 victory, 2 -> player2 victory 
    private void concludeGame(int i) {
   	    // System.out.println("Game result: "+i);
    	String pre = "<html><b><span style='font-size: 20px'>Game Over: ";
    	switch(i) {
    	case 0:
    		southNorthPanel.removeAll(); // hide p2's deck
    		southSouthPanel.removeAll(); // hide p1's deck
    		announcement.setText(pre+"DRAW");
    		break;
    	case 1:
    		southNorthPanel.removeAll(); // hide p2's deck
    		announcement.setText(pre+"PLAYER 1 WINS");
    		break;
    	case 2:
    		southSouthPanel.removeAll(); // hide p1's deck
    		announcement.setText(pre+"PLAYER 2 WINS");
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
    	announcement.setText("<html><b><span style='font-size: 20px'>Player 2 wins");
    	for (int i=0; i<p1ActiveCards.size(); i++) {
			player2.addCardToDeckBottom(p1ActiveCards.get(i));
			player2.addCardToDeckBottom(p2ActiveCards.get(i)); // p2ActiveCards is the same size
    	}
    }
    private void makeP2GiveCardsToP1() {
    	announcement.setText("<html><b><span style='font-size: 20px'>Player 1 wins");
    	for (int i=0; i<p2ActiveCards.size(); i++) {
    		player1.addCardToDeckBottom(p1ActiveCards.get(i));
			player1.addCardToDeckBottom(p2ActiveCards.get(i));
    	}
    }    
    
    // Called in constructor
    private void setupPanels() {
        /** Creates the primary panels */
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // The sections of the mainPanel
        northPanel = new JPanel(new BorderLayout()); // Player 2 info
        centerPanel = new JPanel(new BorderLayout()); // "arena" where active cards are shown
        									// divided into BorderLayout, for NORTH (player 2 cards) vs. SOUTH (player 1 cards)
        southPanel = new JPanel(new BorderLayout()); // Player 1 info
        
	        // Within centerPanel
	        centerNorthPanel = new JPanel(); // for NORTH side of arena, player 2's active cards
	        centerNorthPanel.setLayout(new FlowLayout());//new BoxLayout(centerNorthPanel, BoxLayout.X_AXIS));
	        centerSouthPanel = new JPanel(); // for SOUTH side of arena, player 1's active cards
	        centerSouthPanel.setLayout(new FlowLayout());//new BoxLayout(centerSouthPanel, BoxLayout.X_AXIS));
	        centerCenterPanel = new JPanel();
	        centerCenterPanel.setLayout(new FlowLayout()); // where announcement is, in the very middle of the arena
	        
	        // Within north and southPanel
	        northNorthPanel = new JPanel(new FlowLayout()); // p2 stats
	        northSouthPanel = new JPanel(new FlowLayout()); // p2 deck
	        southNorthPanel = new JPanel(new FlowLayout()); // p1 deck
	        southSouthPanel = new JPanel(new FlowLayout());

        // Combines panels with panels
        centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
        centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
        centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
        
        northPanel.add(northNorthPanel, BorderLayout.NORTH);
        northPanel.add(northSouthPanel, BorderLayout.SOUTH);
        southPanel.add(southNorthPanel, BorderLayout.NORTH);
        southPanel.add(southSouthPanel, BorderLayout.SOUTH);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Arena stuff is all in centerNorthPanel and centerSouthPanel
        
        // The announcement label!
        announcement = new JLabel();
        centerCenterPanel.add(announcement, BorderLayout.CENTER);
        setLayout(new BorderLayout(0, 0));
        
        this.add(mainPanel);
    }
    
    /** Instance fields */
    private int numCards;
    private Player player1, player2; // you are player1, computer is player2
    private ArrayList<Card> p1ActiveCards;
    private ArrayList<Card> p2ActiveCards;
    private GameState gameState; 
        // to keep track of what type of step() to perform
    
    // IO
    private FileInputStream fis;
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    // For stats
    private JLabel announcement; // e.g., "War!"
    private JLabel p2Label, p1Label;
    
    private JPanel northPanel, centerPanel, southPanel;
    private JPanel centerNorthPanel, centerSouthPanel, centerCenterPanel;
    private JPanel northNorthPanel, northSouthPanel, southNorthPanel, southSouthPanel;
    
    private static final String WAR_STRING = "<html><b><span style='font-size: 30px'>WAR</span></b></html>";
    private static final String SAVE_FILE_NAME = "game.sav";

    private static final long serialVersionUID = 1L;
}
