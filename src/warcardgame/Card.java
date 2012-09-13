package warcardgame;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * The card entity; includes Image and Id
 * @author David Zhang
 */
public class Card extends JLabel {
    public Card(String theKey) {
        key = theKey;
        value = getValueFromKey();
        faceUp = false;
        setIcon(new ImageIcon(CARDS_PATH + key + ".gif"));
    }
    private Card() {}

    public void setFaceUp(boolean b) {
    	faceUp = b;
    }
    public boolean getFaceUp() {
    	return faceUp;
    }
        
    public int getValue() {
    	return value;
    }
    
    /** Private methods */
    // Gets the value of the card based on the key; called in constructor
    private int getValueFromKey() {
    	int val = -1;
    	String firstChar = key.substring(0,1);
    	try {
    		// Try getting the number 2-9, if available
    		val = Integer.parseInt(firstChar);
    	} catch (Exception e) {
    		// 2-9 is not available, so check special cases
    		if (firstChar.equals("t"))
    			val = 10; // ten
    		else if (firstChar.equals("j"))
    			val = 11; // jack
    		else if (firstChar.equals("q"))
    			val = 12; // queen
    		else if (firstChar.equals("k"))
    			val = 13; // king
    		else if (firstChar.equals("a"))
    			val = 14; // ace
    		else 
    			System.out.println("Invalid card identifier - check out class Card.");
    	}
    	return val;
    }
    
    // Tester method
    public static void main(String[] args) {
    }
    
    /* Instance fields */
    private String key; // a 2-character string like 'as' or '3d', mimicking the names of the image files
    private int value; // an integer, 2-14, where 11 is Jack, 12 is Queen, 13 is King, and 14 is Ace; -1 means back of card is shown
    private boolean faceUp;
    
    public static final String CARDS_PATH = "cards/"; // used like CARDS_PATH+"2c.gif"
}
