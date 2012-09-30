package warcardgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.Font;

/**
 * A JPanel that displays player info.
 * @author David Zhang
 */
public class PlayerPanel extends JPanel implements ActionListener {
	public PlayerPanel(Player p, ActionListener theListener) {
		// Variables
		listener = theListener;
		player = p;

		// Layout stuff
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		playerBoxPanel = new JPanel();
		add(playerBoxPanel);
		playerBoxPanel.setLayout(new BoxLayout(playerBoxPanel, BoxLayout.Y_AXIS));
		
		namePanel = new JPanel();
		playerBoxPanel.add(namePanel);
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		
		lblName = new JLabel(player.getName());
		lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		namePanel.add(lblName);
		
		statsPanel = new JPanel();
		playerBoxPanel.add(statsPanel);
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		
		winPanel = new JPanel();
		winPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		statsPanel.add(winPanel);
		winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.X_AXIS));
		
		lblNumberOfWins = new JLabel("Number of wins: ");
		lblNumberOfWins.setAlignmentX(Component.RIGHT_ALIGNMENT);
		winPanel.add(lblNumberOfWins);
		lblNumberOfWins.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		
		lblWinCount = new JLabel(player.getNumWins()+"");
		lblWinCount.setAlignmentX(Component.RIGHT_ALIGNMENT);
		winPanel.add(lblWinCount);
		
		lossPanel = new JPanel();
		lossPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		statsPanel.add(lossPanel);
		lossPanel.setLayout(new BoxLayout(lossPanel, BoxLayout.X_AXIS));
		
		lblNumberOfLosses = new JLabel("Number of losses: ");
		lblNumberOfLosses.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lossPanel.add(lblNumberOfLosses);
		lblNumberOfLosses.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		
		lblLossCount = new JLabel(player.getNumLosses()+"");
		lblLossCount.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lossPanel.add(lblLossCount);
		
		playerButtonPanel = new JPanel();
		playerButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(playerButtonPanel);
		playerButtonPanel.setLayout(new BoxLayout(playerButtonPanel, BoxLayout.X_AXIS));
		
		btnSelectPlayer = new JButton("Select");
		btnSelectPlayer.addActionListener(this);
		btnSelectPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSelectPlayer.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		playerButtonPanel.add(btnSelectPlayer);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSelectPlayer) {
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Selected player: "+player.getName()));
		}
	}
	
	public void refresh() {
		lblName.setText(player.getName());
		lblWinCount.setText(player.getNumWins()+"");
		lblLossCount.setText(player.getNumLosses()+"");
	}

	public void addOneWin() {
		player.setNumWins(player.getNumWins()+1);
	}

	public void addOneLoss() {
		player.setNumLosses(player.getNumLosses()+1);
	}

	public String getName() {
		return player.getName();
	}

	/** Instance fields */
	private ActionListener listener;
	private Player player;

	JPanel playerBoxPanel;
	JPanel namePanel;
	JLabel lblName;
	JPanel statsPanel;
	JPanel winPanel;
	JLabel lblNumberOfWins;
	JLabel lblWinCount;
	JPanel lossPanel;
	JLabel lblNumberOfLosses;
	JLabel lblLossCount;
	JPanel playerButtonPanel;
	JButton btnSelectPlayer;
}
