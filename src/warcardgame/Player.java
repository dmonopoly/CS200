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
		deck.setFaceUpForCard(deck.size()-1, true); // make it faceUp
		return deck.pop();
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
}
