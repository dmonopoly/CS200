package warcardgame;

import java.io.Serializable;

/**
 * A class that encapsulates all player-related data.
 * @author David Zhang
 */
public class Player implements Serializable {
	public Player(String theName, Deck theDeck) {
		name = theName;
		deck = theDeck;
		numWins = 0;
		numLosses = 0;
	}
	
	public Card playCard() {
		deck.setFaceUpForCard(deck.size()-1, true); // make it faceUp
		Card c = deck.pop();
		
//		System.out.println(c.getValue() + "|" + name);
		
		return c;
	}
	
	public void addCardToDeckBottom(Card c) {
		deck.putOnBottom(c);
	}
	
	public Card playFaceDownCard() {
		deck.setFaceUpForCard(deck.size()-1, false); // make it faceDown
		return deck.pop();
	}
	
	public int cardCount() {
		return deck.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String theName) {
		name = theName;
	}
	
    public int getNumWins() {
		return numWins;
	}

	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}

	public int getNumLosses() {
		return numLosses;
	}

	public void setNumLosses(int numLosses) {
		this.numLosses = numLosses;
	}

	/* Instance fields */
	private Deck deck;
	private String name;
	private int numWins, numLosses;
}
