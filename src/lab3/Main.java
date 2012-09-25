package lab3;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame implements ActionListener {
	private CardLayout layout;
	private JPanel contentPane;
	private Form form1, form2;
	private JButton button1, button2;
	private JLabel results;

//	public JPanel getContentPane() {
//		form1 = new Form();
//		return form1;
//	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 300);

		// Instantiate
		results = new JLabel();
		button1 = new JButton("Next page");
		button2 = new JButton("See results");
		
		form1 = new Form();
		form2 = new Form();
		
		form1.add(button1);
		form2.add(button2);
		form2.add(results);
		
		contentPane = new JPanel();
		contentPane.setLayout(new CardLayout());
		
        // Add actionlisteners
        button1.addActionListener(this);
        button2.addActionListener(this);
		
		// Combine
		contentPane.add(form1, "FORM 1");
		contentPane.add(form2, "FORM 2");
		
		setContentPane(contentPane);
	}

   public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == button1) {
    		System.out.println("1");
    		form1.grabTextInfo();
    		layout = (CardLayout)(contentPane.getLayout());
            layout.next(contentPane); // show(contentPane, "FORM 2");
    	} else if (e.getSource() == button2) {
    		System.out.println("2");
    		results.setText(grabResults());
    	}
    }
   
   private String grabResults() {
	   String s = form1.tf1.getText() + " " + form1.tf2.getText() + " " + form1.tf3.getText() + " " + form1.tf4.getText() + "\n";
	   s += form2.tf1.getText() + " " + form2.tf2.getText() + " " + form2.tf3.getText() + " " + form2.tf4.getText();
	   return s;
   }
}
