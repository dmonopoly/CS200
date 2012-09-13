package warcardgame;

/**
 *
 * @author David Zhang
 */
public class Player {
	public Player(Deck theDeck) {
		deck = theDeck;
	}
	private Player() {};
	
	public Card playCard() {
		// faceUp by default is false, so set it to true
		deck.setFaceUpForCard(deck.size()-1, true);
		return deck.pop();
	}
	
	public Card playFaceDownCard() {
		return deck.pop();
	}
	
	public int cardCount() {
		return deck.size();
	}
	
    /* Instance fields */
	private Deck deck;
}
