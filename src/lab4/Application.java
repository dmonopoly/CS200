package lab4;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author David Zhang
 */
 
public class Application extends JFrame {
    public Application() {
        // Set frame dimensions
        maxX = 900;
        maxY = 600;
        setSize(maxX, maxY);
        preparePanels();
        
    }
    
    private void preparePanels() {
        outerGridPanel = new JPanel();
        GridLayout gl_outerGridPanel = new GridLayout(2,2);
        outerGridPanel.setLayout(gl_outerGridPanel);
        
        /** Box */
        
        /** GridBag */

        // Objects
        makeGridBagColumn1();
        makeGridBagColumn2();
        makeGridBagColumn3();

        /** Scroll Pane */

        /** Combine panels with panels */
        
        scrollPanePanel = new JPanel();
        outerGridPanel.add(scrollPanePanel);
        scrollPanePanel.setLayout(new BorderLayout(10, 10));
        txtrLoremIpsumDolor = new JTextArea(100,200);
        txtrLoremIpsumDolor.setLineWrap(true);
        txtrLoremIpsumDolor.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean dignissim tincidunt massa, id bibendum magna congue in. Nullam elementum egestas erat, sit amet varius mi interdum id. Sed elit nisi, semper vel aliquet eu, fringilla vitae ligula. Fusce in massa dui. Nulla facilisi. Cras nec enim magna, id blandit massa. Fusce nulla tellus, elementum vel ornare eget, tempus a nisl. Fusce et nibh eu odio vehicula pharetra vitae nec tellus. Nullam id sollicitudin metus. Suspendisse malesuada suscipit arcu, nec lacinia nisl ullamcorper a. Nullam nec ipsum libero. In sit amet velit in felis vestibulum mattis. Donec tincidunt porttitor ligula, condimentum posuere turpis pretium id. Praesent tincidunt velit non nisl venenatis quis commodo tortor tristique. Sed placerat, est ac consequat vestibulum, tortor ante laoreet mauris, ac condimentum tortor massa eu orci. Nulla pellentesque leo quis lectus adipiscing egestas. Duis condimentum, massa malesuada mattis hendrerit, odio lorem facilisis ipsum, rutrum pulvinar metus risus vel enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Phasellus pharetra metus quis arcu feugiat eu porta ligula iaculis.");
        
                scrollPane = new JScrollPane(txtrLoremIpsumDolor);
                scrollPanePanel.add(scrollPane);
        
        getContentPane().add(outerGridPanel);
        gridBagPanel = new JPanel();
        outerGridPanel.add(gridBagPanel);
        gridBagPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints_1_1_1 = new GridBagConstraints();
        constraints_1_1_1.insets = new Insets(0, 0, 5, 5);
        constraints_1_1_1.weightx = 0.5;
        
        gridCol1Label = new JLabel("label one");
        gridCol1tf = new JTextField(50);
        gridCol1b = new JButton("Button 1");
        
                constraints_1_1_1.gridx = 0;
                constraints_1_1_1.gridy = 0;
                gridBagPanel.add(gridCol1Label, constraints_1_1_1);
                
                        constraints_1_1_1.gridx = 0;
                        constraints_1_1_1.gridy = 1;
                        
                        gridCol3Label = new JLabel("label three");
                        GridBagConstraints gbc_gridCol3Label = new GridBagConstraints();
                        gbc_gridCol3Label.insets = new Insets(0, 0, 5, 0);
                        gbc_gridCol3Label.gridx = 3;
                        gbc_gridCol3Label.gridy = 0;
                        gridBagPanel.add(gridCol3Label, gbc_gridCol3Label);
                        GridBagConstraints gbc_gridCol1tf = new GridBagConstraints();
                        gbc_gridCol1tf.insets = new Insets(0, 0, 5, 5);
                        gbc_gridCol1tf.fill = GridBagConstraints.HORIZONTAL;
                        gbc_gridCol1tf.gridy = 1;
                        gbc_gridCol1tf.gridx = 0;
                        gbc_gridCol1tf.weightx = 0.5;
                        gridBagPanel.add(gridCol1tf, gbc_gridCol1tf);
                        
                                constraints_1_1_1.gridx = 0;
                                constraints_1_1_1.gridy = 2;
                                GridBagConstraints gbc_gridCol1b = new GridBagConstraints();
                                gbc_gridCol1b.insets = new Insets(0, 0, 0, 5);
                                gbc_gridCol1b.gridy = 2;
                                gbc_gridCol1b.gridx = 0;
                                gridBagPanel.add(gridCol1b, gbc_gridCol1b);
                                GridBagConstraints constraints_1_1_11 = new GridBagConstraints();
                                constraints_1_1_11.insets = new Insets(0, 0, 5, 5);
                                constraints_1_1_11.weightx = 0.5;
                                
                                gridCol2Label = new JLabel("label two");
                                gridCol2tf = new JTextField(30);
                                gridCol2b = new JButton("Button 2");
                                
                                        constraints_1_1_11.gridx = 1;
                                        constraints_1_1_11.gridy = 0;
                                        gridBagPanel.add(gridCol2Label, constraints_1_1_11);
                                        
                                                constraints_1_1_11.gridx = 1;
                                                constraints_1_1_11.gridy = 1;
                                                GridBagConstraints gbc_gridCol2tf = new GridBagConstraints();
                                                gbc_gridCol2tf.insets = new Insets(0, 0, 5, 5);
                                                gbc_gridCol2tf.fill = GridBagConstraints.HORIZONTAL;
                                                gbc_gridCol2tf.gridx = 1;
                                                gbc_gridCol2tf.gridy = 1;
                                                gridBagPanel.add(gridCol2tf, gbc_gridCol2tf);
                                                
                                                        constraints_1_1_11.gridx = 1;
                                                        constraints_1_1_11.gridy = 2;
                                                        GridBagConstraints gbc_gridCol2b = new GridBagConstraints();
                                                        gbc_gridCol2b.insets = new Insets(0, 0, 0, 5);
                                                        gbc_gridCol2b.gridy = 2;
                                                        gbc_gridCol2b.gridx = 1;
                                                        gridBagPanel.add(gridCol2b, gbc_gridCol2b);
                                                        gridCol3tf = new JTextField(30);
                                                        gridCol3b = new JButton("Button 3");
                                                        
                                                                constraints_1_1_11.gridx = 3;
                                                                constraints_1_1_11.gridy = 0;
                                                                
                                                                        constraints_1_1_11.gridx = 3;
                                                                        constraints_1_1_11.gridy = 1;
                                                                        GridBagConstraints gbc_gridCol3tf = new GridBagConstraints();
                                                                        gbc_gridCol3tf.insets = new Insets(0, 0, 5, 0);
                                                                        gbc_gridCol3tf.fill = GridBagConstraints.HORIZONTAL;
                                                                        gbc_gridCol3tf.gridy = 1;
                                                                        gbc_gridCol3tf.gridx = 3;
                                                                        gridBagPanel.add(gridCol3tf, gbc_gridCol3tf);
                                                                        
                                                                                constraints_1_1_11.gridx = 3;
                                                                                constraints_1_1_11.gridy = 2;
                                                                                GridBagConstraints gbc_gridCol3b = new GridBagConstraints();
                                                                                gbc_gridCol3b.gridy = 2;
                                                                                gbc_gridCol3b.gridx = 3;
                                                                                gridBagPanel.add(gridCol3b, gbc_gridCol3b);
        boxPanel = new JPanel();
        outerGridPanel.add(boxPanel);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        
                rb1 = new JRadioButton("Radio button 1");
                rb2 = new JRadioButton("Radio button 2");
                rb3 = new JRadioButton("Radio button 3");
                
                ButtonGroup group = new ButtonGroup();
                group.add(rb1);
                group.add(rb2);
                group.add(rb3);
                
                        boxPanel.add(rb1);
                        boxPanel.add(rb2);
                        boxPanel.add(rb3);
                        
                        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
                        outerGridPanel.add(tabbedPane);
                        
                        panel = new JPanel();
                        tabbedPane.addTab("New tab", null, panel, null);
                        
                        lblNewLabel = new JLabel("Label 1");
                        panel.add(lblNewLabel);
                        
                        textField = new JTextField();
                        panel.add(textField);
                        textField.setColumns(10);
                        
                        panel_1 = new JPanel();
                        tabbedPane.addTab("New tab", null, panel_1, null);
                        
                        lblLabel = new JLabel("Label 2");
                        panel_1.add(lblLabel);
                        
                        textField_1 = new JTextField();
                        textField_1.setColumns(10);
                        panel_1.add(textField_1);
                        
                        panel_2 = new JPanel();
                        tabbedPane.addTab("New tab", null, panel_2, null);
                        
                        lblLabel_1 = new JLabel("Label 3");
                        panel_2.add(lblLabel_1);
                        
                        textField_2 = new JTextField();
                        textField_2.setColumns(10);
                        panel_2.add(textField_2);
    }
    
    private void makeGridBagColumn1() {
    }
    
    private void makeGridBagColumn2() {
    }

    private void makeGridBagColumn3() {
    }

    public static void main(String[] args) {
        Application app = new Application();
        
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /* Instance fields */
    private int maxX;
    private int maxY;
    
    private JPanel outerGridPanel, boxPanel, gridBagPanel, scrollPanePanel, tabbedPanePanel;
    private JScrollPane scrollPane;

    // Within boxPanel
    private JRadioButton rb1, rb2, rb3;

    // Within gridBagPanel
    private JLabel gridCol1Label, gridCol2Label, gridCol3Label;
    private JTextField gridCol1tf, gridCol2tf, gridCol3tf;
    private JButton gridCol1b, gridCol2b, gridCol3b;
    
    // Within scrollPanePanel
    private JTextArea txtrLoremIpsumDolor;
    private JTabbedPane tabbedPane;
    private JLabel lblNewLabel;
    private JTextField textField;
    private JPanel panel;
    private JPanel panel_1;
    private JLabel lblLabel;
    private JTextField textField_1;
    private JPanel panel_2;
    private JLabel lblLabel_1;
    private JTextField textField_2;

    // Within tabbedPanePanel
    

}