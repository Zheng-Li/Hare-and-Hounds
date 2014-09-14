package board;

public class Position {
	private int is_occupied;
	private int x;
	private int y;
	
	public Position(){}
	
	public Position(int x_co, int y_co) {
		this.is_occupied = 0;
		this.x = x_co;
		this.y = y_co;
	}
	
	public Position(Position p) {
		this.x = p.getX();
		this.y = p.getY();
		this.is_occupied = 0;
	}
	
	//Check if Occupied
	public boolean is_occupied() {
		if(is_occupied == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getOcc() {
		return this.is_occupied; 
	}
	
	//Hare arrives at spot
	public void hare_arrive() {
		this.is_occupied = 1;
	}
	
	//Hound arrives at spot
	public void hound_arrive() {
		this.is_occupied = 2;
	}
	
	//leave a spot
	public void leave() {
		this.is_occupied = 0;
	}
	
	public boolean equalsTo(Position p) {
		if(this.x == p.getX() && this.y == p.getY()) {
			return true;
		} else {
			return false;
		}
	}

}
