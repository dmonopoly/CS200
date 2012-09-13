package warcardgame;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 *
 * @author David Zhang
 */
 
public class Application extends JFrame implements ActionListener {
    public Application() {
        // Set frame dimensions
        maxX = 800;
        maxY = 800;
        setSize(maxX, maxY);
        
        // // Deal with listeners
        addKeyListener(new MyKeyListener());
        
        // Initialize other variables
        // background = new Rectangle(0, 0, maxX, maxY);
        game = new Game(this);
    }
    
    public static void main(String[] args) {
        Application app = new Application();
        
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    
    // public void paint(Graphics g) {
    //     Graphics2D g2 = (Graphics2D) g;
    //     
    //     // Set background color
    //     // g2.setColor(Color.white);
    //     // g2.fill(background);
    //     
    //     // Redraw cards
    //     // game.redrawCards();
    // }
    
    private class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    game.step();
                    break;
                default:
                    System.out.println("Nothing...");
            }
        }
        
        public void keyTyped(KeyEvent e) { }
        public void keyReleased(KeyEvent e) { }
        
    }

    /* Instance fields */
    private int maxX;
    private int maxY;
    
    private Rectangle background;
    private Game game; // essential
}