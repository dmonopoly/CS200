package warcardgame;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that has many Cards
 * @author David Zhang
 */
public class Deck {
    public Deck() {
    	cards = new ArrayList<Card>();
    }
    
    public Deck(ArrayList<Card> theCards) {
    	cards = theCards;
    }
    
    // Removes half of this deck and returns the removed half; used when dividing the deck into 2
    public ArrayList<Card> split() {
    	ArrayList<Card> otherCards = new ArrayList<Card>();
    	for (int i=size()/2; i<size(); i++)
    	   otherCards.add(cards.remove(i));
    	return otherCards;
    }
    
    public void shuffle() {
    	// TODO
    }
    
    // Sets the faceUp value for the card at index
    public void setFaceUpForCard(int index, boolean b) {
    	cards.get(index).setFaceUp(b);
    }
    
    public Card pop() {
    	return cards.remove(this.size()-1);
    }
    
    public int size() {
    	return cards.size();
    }
    
    /* Instance fields */
    private ArrayList<Card> cards; // cards[0] is the bottom card; cards[size-1] is the top card
}
