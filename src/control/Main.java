package control;


import java.util.ArrayList;

import javax.swing.SwingUtilities;

import board.Position;

public class Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainGUI gui = new MainGUI();
			}
		});	
	}
	
	public static void testResult(ArrayList<Position> p) {
		System.out.println();
		//Display all the positions valid in board
		for(Position position: p) {
			System.out.println("(" + position.getX() + ", " 
					+ position.getY() + ") : " + position.getOcc());
		}
	}
}
