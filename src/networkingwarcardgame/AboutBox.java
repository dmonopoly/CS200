package networkingwarcardgame;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.xml.ws.Action;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.Component;


public class AboutBox extends JDialog {

    public AboutBox(Frame parent) {
        super(parent);
        
        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.NORTH);
        
        JSeparator separator = new JSeparator();
        panel_1.add(separator);
        
        JLabel lblBlank1 = new JLabel("");
        panel_1.add(lblBlank1);
        
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JLabel lblNotes1 = new JLabel("Welcome to War!");
        lblNotes1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNotes1.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));
        mainPanel.add(lblNotes1);
        
        JLabel lblNotes2 = new JLabel("<html>Player data is automatically LOADED upon <br>connecting to the server (launching this application).\n<br><br>\nExiting via clicking the x in the corner or doing <br>File > Exit automatically SAVES player data. \n<br><br>\nYou can save while using the application by doing File > Save, too.\n<br><br>\nIn this version, various players can connect to the server <br>and play their own games.\n");
        lblNotes2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNotes2.setFont(new Font("Lucida Fax", Font.PLAIN, 16));
        mainPanel.add(lblNotes2);
        
        JPanel panel_2 = new JPanel();
        getContentPane().add(panel_2, BorderLayout.EAST);
        
        JSeparator separator_2 = new JSeparator();
        panel_2.add(separator_2);
        
        JLabel lblBlank2 = new JLabel("");
        panel_2.add(lblBlank2);
        
        JPanel panel_3 = new JPanel();
        getContentPane().add(panel_3, BorderLayout.SOUTH);
        
        JSeparator separator_3 = new JSeparator();
        panel_3.add(separator_3);
        
        JLabel label = new JLabel("");
        panel_3.add(label);
        
        JPanel panel_4 = new JPanel();
        getContentPane().add(panel_4, BorderLayout.WEST);
        
        JSeparator separator_1 = new JSeparator();
        panel_4.add(separator_1);
        
        JLabel label_1 = new JLabel("");
        panel_4.add(label_1);
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
