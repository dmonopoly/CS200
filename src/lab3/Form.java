package lab3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Form extends JPanel {
    /** Instance Fields */
	private JLabel label;
    public JTextField tf1, tf2, tf3, tf4;

    private String info1, info2, info3, info4;

	/**
	 * Create the panel.
	 */
	public Form() {
		setLayout(new FlowLayout());
		
		label = new JLabel("test");
        tf1 = new JTextField(20);
        tf2 = new JTextField(20);
        tf3 = new JTextField(20);
        tf4 = new JTextField(20);
        
        // Combine
//        add(label);
        add(tf1);
        add(tf2);
        add(tf3);
        add(tf4);
	}

    public void grabTextInfo() {
    	info1 = tf1.getText();
        info2 = tf2.getText();
        info3 = tf3.getText();
        info4 = tf4.getText();
    }
}
