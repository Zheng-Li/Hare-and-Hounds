package players;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import board.Position;

public class Hound {
	private Image img;
	private Position pos;
	
	public Hound(Position p) {
		this.pos = new Position(p);
		try {
			this.img = ImageIO.read(new File("hound.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//get Position
	public Position getPosition() {
		return this.pos;
	}
	
	//get Image
	public Image getImage() {
		return this.img;
	}
	
	//Make a move
	public void move(int x, int y) {
		this.pos.setX(x);
		this.pos.setY(y);
	}
}
