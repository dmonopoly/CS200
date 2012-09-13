package warcardgame;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static warcardgame.GameState.*;

/**
 * Handles the actual card game, displaying images of cards
 * and progressing wars.
 * @author David Zhang
 */
public class Game {
    public Game(JFrame theContainer) {
        frame = theContainer;
        cardsInPlay = new ArrayList<Card>();

        // Prepare decks for players
        Deck deck1 = new Deck();
        deck1.shuffle();
        Deck deck2 = new Deck(deck1.split());
        
        // The players!
        player1 = new Player(deck1);
        player2 = new Player(deck2);
        setupPanels();
    }
    
    // KEY method: Continues the game, like drawing the next card
    public void step() {
    	if (player1.cardCount() == 0)
    		showGameResult(2);
    	else if (player2.cardCount() == 0)
    		showGameResult(1);
    	else {
    		cardsInPlay.add(player1.playCard());
    		cardsInPlay.add(player2.playCard());
    		if (cardsInPlay.get(0).getValue() == cardsInPlay.get(1).getValue()) {
    			// WARRRRR!!!
    			GameState resultOfWar = playWar();
    			
    		}
    	}
        System.out.println("Stepped");
    }
    
    /** Private methods */
    // Recursive method that plays out a war sequence; uses enums
    // @return 0 if the war is a draw (which also means the game is over); 1 for player1 victory; 2 for player2 victory
    private GameState playWar() {
        try {
            cardsInPlay.add(player1.playFaceDownCard());
    		cardsInPlay.add(player2.playFaceDownCard());
    		cardsInPlay.add(player1.playCard());
    		cardsInPlay.add(player2.playCard());
    		int p1Val = cardsInPlay.get(cardsInPlay.size()-2).getValue();
    		int p2Val = cardsInPlay.get(cardsInPlay.size()-1).getValue();
    		if (p1Val == p2Val)
    		    return playWar(); // Play war again
    		else if (p1Val > p2Val)
    		    return PLAYER_1_WAR_VICTORY; // Player 1 wins just the war, so give him the cards
            else
    		    return PLAYER_2_WAR_VICTORY; // Player 2 wins just the war, so give him the cards
        } catch (Exception e) { // This happens when a player runs out of cards.
            System.out.println("Exception: "+e.getMessage());
            boolean p1isOut = player1.cardCount() == 0;
            boolean p2isOut = player2.cardCount() == 0;
            boolean lastTwoCardsInPlayAreFaceDown = !cardsInPlay.get(cardsInPlay.size()-2).getFaceUp() && !cardsInPlay.get(cardsInPlay.size()-1).getFaceUp();
            if (p1isOut && p2isOut && lastTwoCardsInPlayAreFaceDown)
                // If they're both out of cards, and the last cards they played are face down, it's a draw
                return DRAW;
            else if (p1isOut && p2isOut) {
                // If they're both out of cards, but the last cards they played are face up, 
            	// compare the cards for a last-minute battle
                int p1Val = cardsInPlay.get(cardsInPlay.size()-2).getValue();
        		int p2Val = cardsInPlay.get(cardsInPlay.size()-1).getValue();
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
            } else
                System.out.println("Something is amiss."); // REMOVE LATER
            	return DRAW; 
        }
    }
    
    // Concludes the game
    // @params i: 0 -> draw, 1 -> player1 victory, 2 -> player2 victory 
    private void showGameResult(int i) {
    	// TODO: Add panels and stuff to show who won; reset the game
    }
    
    // Called in constructor
    private void setupPanels() {
        /** Creates the primary panels */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // The sections of the mainPanel
        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        
        // Combines panels with panels
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        /** Fill in inner panels */
        northLabel = new JLabel("Welcome to WAR");
        northPanel.add(northLabel);

        centerLabel = new JLabel("Hit SPACE to play");
        centerPanel.add(centerLabel);

        // Combines JPanel with JFrame
        frame.setContentPane(mainPanel);
    }
    
    /* Instance fields */
    private Player player1, player2; // you are player1, computer is player2
    private ArrayList<Card> cardsInPlay; // to keep track of cards played in the arena
                                        // [0] = player1's card, [1] = player2's card
                                        // If we have a war, [2,4,6..] = player1's cards, 
                                        //                  [1,3,5..] = player2's card
    
    private JLabel northLabel;
    private JLabel centerLabel;
    private JFrame frame;
}
