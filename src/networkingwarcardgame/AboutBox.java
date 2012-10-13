package networkingwarcardgame;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.xml.ws.Action;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;


public class AboutBox extends JDialog {

    public AboutBox(Frame parent) {
        super(parent);
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        
        JLabel lblThisApplicationIs = new JLabel("This application is a simple game of War.");
        panel.add(lblThisApplicationIs);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    @Action public void closeAboutBox() {
        dispose();
    }

    private void initComponents() {
        closeButton = new javax.swing.JButton();
        

        pack();
    }                        

    // Instance variables
    private JButton closeButton;
}
