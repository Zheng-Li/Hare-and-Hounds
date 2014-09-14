package board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import players.Hare;
import players.Hound;
import control.AI;


public class BoardControlPanel extends JPanel {
	//Board settings
	private Image buffer;
	private Graphics graph;
	private static final int width = 600;
	private static final int height = 400;
	
	//11 board position, 22 connections and 4 players
	private Board board;
	private ArrayList<Position> cos;
	private ArrayList<Connect> conn;
	private Hare hare;
	private Hound alpha, beta, gamma;
	
	//Moving rules
	private int player_selected;
	private String mov = null;
	private Position start;
	
	//AI based on Alpha-Beta Algorithm
	private AI ai;
	
	//Initialize the board
	public BoardControlPanel() {
		setPreferredSize(new Dimension(width, height));
		
		
		hare = new Hare(new Position(500, 200));
		alpha = new Hound(new Position(100, 200));
		beta = new Hound(new Position(200, 100));
		gamma = new Hound(new Position(200, 300));
		
		ArrayList<Position> hounds = new ArrayList<Position>();
		hounds.add(alpha.getPosition());
		hounds.add(beta.getPosition());
		hounds.add(gamma.getPosition());
		
		board = new Board(hare.getPosition(), hounds);
		cos = board.getPosition();
		conn = board.getConnection();
		
		String[] options = {"Hound", "Hare"};
		int side = JOptionPane.showOptionDialog(null, "Choose your side...",
				"Game Starts", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);
		
		//Testing AI - hare
		ai = new AI(side + 1);
		if(ai.get_side() == 2) {
			ai_move();
		}
		
		//Player mouse control
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(player_selected == 0) {
					player_selection(e);
				} else {
					player_moved(e);
					
					//TEST
					check_game_status();
					ai_move();
					check_game_status();
				}
				
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		//Create board canvas
		buffer = createImage(width, height);
		graph = buffer.getGraphics();
		drawBoard(graph);
		setPlayer(graph);
		g.drawImage(buffer, 0, 0, null);
	}
	
	void drawBoard(Graphics bg) {
		ArrayList<Polygon> ps = new ArrayList<Polygon>();
		
		bg.setColor(Color.CYAN);
		//First 7 Polygon
		for(int i=0; i<7; i++) {
			ps.add(getPolygon(cos.get(i).getX(), cos.get(i).getY()));
			bg.drawPolygon(ps.get(i));
			bg.fillPolygon(ps.get(i));
		}
		
		//Last 4 Rectangle
		for(int j=7; j<11; j++) {
			ps.add(getPolygon(cos.get(j).getX(), cos.get(j).getY()));
			bg.drawRect(cos.get(j).getX()-30, cos.get(j).getY()-30, 60, 60);
			bg.fillRect(cos.get(j).getX()-30, cos.get(j).getY()-30, 60, 60);
		}
		
		//Connection between slots
		bg.setColor(Color.black);
		for(int k=0; k<conn.size(); k++) {
			bg.drawLine(conn.get(k).startX, conn.get(k).startY, 
					conn.get(k).endX, conn.get(k).endY);
		}
	}
	
	public void saveBoard(Image img) {
		//Save Backgroudn Image
		try{
			BufferedImage bi = (BufferedImage) buffer;
			File outputFile = new File("back.png");
			ImageIO.write(bi, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayer(Graphics pg) {
		
		for(Position pt : cos) {
			if(hare.getPosition().equalsTo(pt)) {
				pt.hare_arrive();
				pg.drawImage(hare.getImage(), pt.getX()-25, pt.getY()-25, null);
			} else if(alpha.getPosition().equalsTo(pt)) {
				pt.hound_arrive();
				pg.drawImage(alpha.getImage(), pt.getX()-25, pt.getY()-25, null);
			} else if(beta.getPosition().equalsTo(pt)) {
				pt.hound_arrive();
				pg.drawImage(beta.getImage(), pt.getX()-25, pt.getY()-25, null);
			} else if(gamma.getPosition().equalsTo(pt)) {
				pt.hound_arrive();
				pg.drawImage(gamma.getImage(), pt.getX()-25, pt.getY()-25, null);
			}
		}
 	}
	
	public void player_selection(MouseEvent e) {
		for(Position po : cos) {
			if(e.getX()>po.getX()-27 && e.getX()<po.getX()+27 &&
				e.getY()>po.getY()-27 && e.getY()<po.getY()+27) {
				switch (po.getOcc()) {
				case 0:	System.out.println("Empty slot");
						break;
				case 1:	player_selected = 1;
						po.leave();
						start = new Position(po);
						mov = "hare";
						start.hare_arrive();
						System.out.println("I've seleceted hare");
						break;
				case 2: player_selected = 2;
						//Check which hound is moving
						if(alpha.getPosition().equalsTo(po)) {
							mov = "alpha";
						} else if(beta.getPosition().equalsTo(po)) {
							mov = "beta";
						} else if(gamma.getPosition().equalsTo(po)){
							mov = "gamma";
						}
						po.leave();
						start = new Position(po);
						start.hound_arrive();
						System.out.println("I've selected hound " + mov + "!");
						break;
				default:System.out.println("Something wrong...");
						break;
				}
			}
		}
	}
	
	public void player_moved(MouseEvent e) {
		for(Position ps : cos) {
			if(e.getX()>ps.getX()-27 && e.getX()<ps.getX()+27 && 
					e.getY()>ps.getY()-27 && e.getY()<ps.getY()+27 &&
					!ps.is_occupied() && board.is_moveable(start, ps)) {
				switch(player_selected) {
					case 0:	System.out.println("Totally wrong!!!");
							break;
					case 1: hare.move(ps.getX(), ps.getY());
							ps.hare_arrive();
							break;
					case 2: if(mov == "alpha") {
								alpha.move(ps.getX(), ps.getY());
							} else if(mov == "beta") {
								beta.move(ps.getX(), ps.getY());
							} else if(mov == "gamma") {
								gamma.move(ps.getX(), ps.getY());
							}
							ps.hound_arrive();
							break;
					default: System.out.println("Something wrong...");
							break;
				}
				player_selected = 0;
				mov = null;
				System.out.println("Moved!!!");
				repaint();
			}
		}
	}

	public Polygon getPolygon(int x, int y) {
		Polygon p = new Polygon();
		int[] vertice = { x-30, y-10, x-10, y-30, x+10, y-30, x+30, y-10,
				x+30, y+10, x+10, y+30, x-10, y+30, x-30, y+10};
		for(int i=0; i<8; i++) {
			p.addPoint(vertice[i*2], vertice[i*2+1]);
		}
		return p;
	}
	
	public void ai_move() {
		ArrayList<Integer> aiMove = new ArrayList<Integer>();
		aiMove = ai.best_move(board);
		System.out.println(aiMove.toString());
		
		Position tmp_s = new Position();
		Position tmp_e = new Position();
		
		//Move hound to new position
		for(int i=0; i<11; i++) {
			if(cos.get(i).getOcc() == 0 && aiMove.get(i) == 2) {
				cos.get(i).hound_arrive();
				tmp_e = new Position(cos.get(i));
			} else if(cos.get(i).getOcc() == 2 && aiMove.get(i) == 0) {
				cos.get(i).leave();
				tmp_s = new Position(cos.get(i));
			}
		}
		
		if(tmp_s.equalsTo(alpha.getPosition())) {
			alpha.move(tmp_e.getX(), tmp_e.getY());
		} else if(tmp_s.equalsTo(beta.getPosition())) {
			beta.move(tmp_e.getX(), tmp_e.getY());
		} else if(tmp_s.equalsTo(gamma.getPosition())){
			gamma.move(tmp_e.getX(), tmp_e.getY());
		} else {
			System.out.println("WTF!!!");
		}
		
		//Move hare to new position
		for(int i=0; i<11; i++) {
			if(cos.get(i).getOcc() == 0 && aiMove.get(i) == 1) {
				cos.get(i).hare_arrive();
				hare.move(cos.get(i).getX(), cos.get(i).getY());
			} else if(cos.get(i).getOcc() == 1 && aiMove.get(i) == 0){
				cos.get(i).leave();
			}
		}
		
	}
	public void check_game_status() {
		switch(board.getWinner()) {
		case 0: System.out.println("Continue!!!");
				break;
		case 1: JOptionPane.showMessageDialog(null, 
				"Hare wins the game!", "Game Over", 
				JOptionPane.INFORMATION_MESSAGE);
				break;
		case 2: JOptionPane.showMessageDialog(null,
				"Hounds win the game!", "Game Over", 
				JOptionPane.INFORMATION_MESSAGE);
				break;
		}
	}

}
