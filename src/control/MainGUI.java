package control;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import board.BoardControlPanel;


public class MainGUI extends JFrame {
	JPanel button_panel;
	JLabel label;
	JButton b_restart;
	JButton b_close;
	BoardControlPanel board_panel;
	
	public MainGUI() {
		//Frame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Hare-and-Hounds");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		label = new JLabel("This is the game board");
		add(label, BorderLayout.BEFORE_FIRST_LINE);
		
		//Canvas panel
		board_panel = new BoardControlPanel();
		add(board_panel, BorderLayout.CENTER);
		
		//Button panel
		button_panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		b_restart = new JButton("Reset");
		b_close = new JButton("Close");
		button_panel.add(b_restart);
		button_panel.add(b_close);
		add(button_panel, BorderLayout.SOUTH);
		pack();
		
		b_restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove(board_panel);
				board_panel = new BoardControlPanel();
				add(board_panel, BorderLayout.CENTER);
				invalidate();
				validate();
				JOptionPane.showMessageDialog(null, "Restarting the game...", "New Game",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		b_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
