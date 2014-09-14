package board;

import java.util.ArrayList;

public class Board {
	private ArrayList<Position> positions;
	private ArrayList<Connect> connections;
	private int winner;
	
	public Board(Position hareP, ArrayList<Position> houndsP) {
		initial();
		for(Position p: positions) {
			if(p.equalsTo(hareP)) {
				p.hare_arrive();
			} else if(houndsP.get(0).equalsTo(p) || houndsP.get(1).equalsTo(p) ||
					houndsP.get(2).equalsTo(p)) {
				p.hound_arrive();
			}
		}
	}
	
	public void initial() {
		positions = new ArrayList<Position>();
		// 7 Octogan slot Positions
		positions.add(new Position(100, 200));
		positions.add(new Position(200, 100));
		positions.add(new Position(200, 300));
		positions.add(new Position(300, 200));
		positions.add(new Position(400, 100));
		positions.add(new Position(400, 300));
		positions.add(new Position(500, 200));
		//4 Square slot Positions
		positions.add(new Position(200, 200));
		positions.add(new Position(300, 100));
		positions.add(new Position(300, 300));
		positions.add(new Position(400, 200));
		
		//22 Connections
		connections = new ArrayList<Connect>();
		connections.add(new Connect(100, 200, 200, 100));
		connections.add(new Connect(100, 200, 200, 200));
		connections.add(new Connect(100, 200, 200, 300));
		connections.add(new Connect(400, 100, 500, 200));
		connections.add(new Connect(400, 200, 500, 200));
		connections.add(new Connect(400, 300, 500, 200));
		connections.add(new Connect(200, 100, 300, 100));
		connections.add(new Connect(200, 200, 300, 200));
		connections.add(new Connect(200, 300, 300, 300));
		connections.add(new Connect(300, 100, 400, 100));
		connections.add(new Connect(300, 200, 400, 200));
		connections.add(new Connect(300, 300, 400, 300));
		connections.add(new Connect(200, 100, 200, 200));
		connections.add(new Connect(300, 100, 300, 200));
		connections.add(new Connect(400, 100, 400, 200));
		connections.add(new Connect(200, 200, 200, 300));
		connections.add(new Connect(300, 200, 300, 300));
		connections.add(new Connect(400, 200, 400, 300));
		connections.add(new Connect(200, 100, 300, 200));
		connections.add(new Connect(200, 300, 300, 200));
		connections.add(new Connect(300, 200, 400, 100));
		connections.add(new Connect(300, 200, 400, 300));
		
		//initial board winner;
		winner = 0;
	}
	
	public ArrayList<Position> getPosition() {
		return this.positions;
	}
	
	public ArrayList<Connect> getConnection() {
		return this.connections;
	}
	
	public Position getHare() {
		for(Position p: positions) {
			if(p.getOcc() == 1) {
				return p;
			}
		}
		return null;
	}
	
	public ArrayList<Position> getHounds() {
		ArrayList<Position> hounds = new ArrayList<Position>();
		for(Position p: positions) {
			if(p.getOcc() == 2) {
				hounds.add(p);
			}
		}
		return hounds;
	}
	
	public boolean is_moveable(Position start, Position end) {
		if(start == null) {
			for(Position pos: positions) {
				System.out.println("Error!!!");
				System.out.println(pos.getX() + ", " + pos.getY() + " :" + pos.getOcc());
			}
		}
		for(int i=0; i<connections.size(); i++) {
			if(connections.get(i).startX == start.getX() &&
					connections.get(i).startY == start.getY() &&
					connections.get(i).endX == end.getX() &&
					connections.get(i).endY == end.getY() ||
					connections.get(i).startX == end.getX() &&
					connections.get(i).startY == end.getY() &&
					connections.get(i).endX == start.getX() &&
					connections.get(i).endY == start.getY()) {
				if(start.getOcc() == 1) {
					return true;
				} else if(start.getOcc() == 2 && end.getX() >= start.getX()) {
					return true;
				} else {
					return false;
				}
			}
		}
//		System.out.println("not valid move");
		return false;
	}
	
	public boolean is_game_over() {
		
		//retrieve player positions
		Position hare = null;
		Position h1 = null;
		Position h2 = null;
		Position h3 = null;
		for(Position ps : positions) {
			switch(ps.getOcc()) {
			case 0: break;
			case 1: hare = ps;
			case 2: if(h1 == null) { 
						h1 = ps;
					} else if(h2 == null) {
						h2 = ps;
					} else {
						h3 = ps;
					}
					break;
			default: break;
			}
		}
		
		//Hare wins in this way
		if(hare.getX() <= h1.getX() && hare.getX() <= h2.getX() &&
				hare.getX() <= h3.getX()) {
			winner = 1;
			return true;
		}
		
		//Game continues
		for(Position p : positions) {
			if(!p.equalsTo(hare) && !p.is_occupied() && is_moveable(hare, p)) {
				winner = 0;
				return false;
			}
		}
		
		winner = 2;
		return true;
	}
	
	public int getWinner() {
		is_game_over();
//		switch(winner) {
//			case 0: break;
//			case 1: System.out.println("Hare wins!!!");
//					break;
//			case 2: System.out.println("Hound wins!!!");
//					break;
//			default: System.out.println("System error...");
//		}
		return this.winner;
	}
	
	public ArrayList<Integer> getBoard() {
		ArrayList<Integer> gb = new ArrayList<Integer>();
		for(int i=0; i<11; i++) {
			 gb.add(positions.get(i).getOcc());
		}
		return gb;
	}
	
}