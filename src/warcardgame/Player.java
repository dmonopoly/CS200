package warcardgame;

import java.io.Serializable;

/**
 *
 * @author David Zhang
 */
public class Player implements Serializable {
	public Player(String theName, Deck theDeck) {
		name = theName;
		deck = theDeck;
	}
	
	public Card playCard() {
		
		deck.setFaceUpForCard(deck.size()-1, true); // make it faceUp
		Card c = deck.pop();
		
		System.out.println(c.getValue() + "|" + name);
		
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

	public void setName(String theName) {
		name = theName;
	}
    /* Instance fields */
	private Deck deck;
	private String name;
	private int numWins, numLosses;
}
