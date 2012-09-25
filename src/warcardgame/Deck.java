package warcardgame;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that has many Cards
 * @author David Zhang
 */
public class Deck {
    public Deck() {
    	cards = new ArrayList<Card>();
    	fillUpDeck();
    }
    
    public Deck(ArrayList<Card> theCards) {
    	cards = theCards;
    }
    
    // Removes half of this deck and returns the removed half; used when dividing the deck into 2
    public ArrayList<Card> split() {
    	int size = size(); // save static size
    	ArrayList<Card> otherCards = new ArrayList<Card>();
    	for (int i=0; i<size/2; i++)
     	   otherCards.add(cards.remove(0));
    	
    	return otherCards;
    }
    
    public void shuffle() {
    	ArrayList<Card> shCards = new ArrayList<Card>();
    	Random r = new Random();
    	int size = cards.size();
    	int index = -1;
    	for (int i=0; i<size; i++) {
    		index = r.nextInt(size-i);
    		shCards.add(cards.remove(index));
    	}
    	cards = shCards;
    }
    
    // Adds a card to the bottom of the deck (index 0)
    public void putOnBottom(Card c) {
    	cards.add(0, c);
    }
    
    // Sets the faceUp value for the card at index
    public void setFaceUpForCard(int index, boolean b) {
    	cards.get(index).setFaceUp(b);
    }
    
    // Removes and returns the card at the top of the deck
    public Card pop() {
    	return cards.remove(this.size()-1);
    }
    
    public int size() {
    	return cards.size();
    }
    
    /** Test methods */
    public void setCard(int index, Card c) {
    	cards.set(index, c);
    }
    
    // Removes cards from [a, b)
    public void removeCards(int a, int b) {
    	for (int i=a; i<b; i++) {
    		cards.remove(a);
    	}
    }
    
    public void printCards() {
    	for (Card c : cards)
            System.out.print(c + " ");
    }
    
    public void add(Card c) {
    	cards.add(c);
    }
    
    /** Private methods */
    // Used in constructor to fill up the deck
    private void fillUpDeck() {
    	// Go through each possible card
    	String[] suits = { "c","d","h","s" };
    	String[] type = { "2","3","4","5","6","7","8","9","t","j","q","k","a" };
    	ArrayList<String> cardKeys = new ArrayList<String>();
    	for (int i=0; i<suits.length; i++)
    	   for (int j=0; j<type.length; j++)
    	       cardKeys.add(type[j] + suits[i]);
    	
    	for (int i=0; i < cardKeys.size(); i++) {
    		cards.add(new Card(cardKeys.get(i)));
    	}
    }
    
    // Tester method
    public static void main(String[] args) {
    }
    
    /* Instance fields */
    private ArrayList<Card> cards; // cards[0] is the bottom card; cards[size-1] is the top card
}
