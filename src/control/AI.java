package control;

import java.util.ArrayList;

import board.Board;
import board.Position;

public class AI {
	final private int computer;
	private int human;

	public AI(int computer) {
		this.computer = computer;
		if(computer == 1) {
			this.human = 2;
		} else {
			this.human = 1;
		}
	}
	
	public int alpha_beta(Board bo, int depth, int alpha, int beta, boolean maxplayer) {
		ArrayList<Board> children = new ArrayList<Board>();
		
		//Game over in board or reach depth limit
		if(bo.is_game_over() || depth == 0) {
			switch(bo.getWinner()) {
			case 0: return cutoff(bo, maxplayer);
			case 1: if(maxplayer) {return -99;}
					else {return 99;}
			case 2: if(maxplayer) {return 99;}
					else {return 99;}
			}
		}
		
		if(maxplayer) {
			children  = get_children(bo, computer);
			for(Board child_board : children) {
				int score = alpha_beta(child_board, depth-1, alpha, beta, !maxplayer);
				if(score > alpha) {
					alpha = score;
				}
				if(alpha >= beta) {
					return alpha;
				}
			}
			return alpha;
		} else {
			children  = get_children(bo, human);
			for(Board child_board : children) {
				int score = alpha_beta(child_board, depth-1, alpha, beta, !maxplayer);
				if(score < beta) {
					beta = score;
				}
				if(alpha >= beta) {
					return beta;
				}
			}
			return beta;
		}
	}
	
	public ArrayList<Integer> best_move(Board board) {
		ArrayList<Board> children = new ArrayList<Board>();
		children = get_children(board, computer);
		
		int size = children.size();
		int max_index = 0;
		int max_value = -9999;
		
		int[] scores = new int[size];
		for(int i=0; i<size; i++) {
			scores[i] = alpha_beta(children.get(i), 10, -9999, 9999, false);
		}
		
		for(int k=0; k<size; k++) {
			if(scores[k] > max_value) {
				max_index = k;
				max_value = scores[k];
			}
		}
		
		for(int j=0; j<size; j++) {
			System.out.println("No." + j + " child" + children.get(j).getBoard().toString() +
					scores[j]);
		}
		System.out.println("Choose No." + max_index);
		
		return children.get(max_index).getBoard();
	}
	
	public ArrayList<Board> get_children(Board boa, int player) {
		ArrayList<Board> children = new ArrayList<Board>();
		
		//Initialize 2 players on board
		Position hareP = new Position (boa.getHare());
		hareP.hare_arrive();
		ArrayList<Position> houndsP = new ArrayList<Position>(boa.getHounds());
		for(Position pos : houndsP) {
			pos.hound_arrive();
		}
		
		//Choose side for AI to move
		if(player == 1) {
			for(Position p : boa.getPosition()) {
				if(boa.is_moveable(hareP, p) && !p.is_occupied()) {
					Board tmp = new Board(p, houndsP);
					children.add(tmp);
				}
			}
		} 
		else if (player == 2){
			for(Position p: boa.getPosition()) {
				for(Position s: houndsP) {
					if(!p.is_occupied() && boa.is_moveable(s, p)) {
						ArrayList<Position> temp = new ArrayList<Position>(houndsP);
						temp.remove(s);
						temp.add(p);
						Board tmp = new Board(hareP, temp);
						children.add(tmp);
						temp = new ArrayList<Position>(houndsP);
					}
				}
			}
		}
		//return the possible children for current board
		return children;
	}
	
	public int cutoff(Board b, boolean maxplayer) {
		int hare_loc = 0;
		int[] bo = new int[11];
		int cutoff_value = 0;
		
		Position hare = new Position(b.getHare());
		ArrayList<Position> hounds = new ArrayList<Position>(b.getHounds());
		
		if((computer == 2 && maxplayer)||(computer == 1 && !maxplayer)) {
			//computer on hound side
			cutoff_value = (manhattan_distance(hare, hounds.get(0)) +
					manhattan_distance(hare, hounds.get(1)) +
					manhattan_distance(hare, hounds.get(2))) / 3;
			if(hare.getX() > hounds.get(0).getX()) {
				cutoff_value += manhattan_distance(hare, b.getPosition().get(0)) +
						manhattan_distance(hare, hounds.get(0)) - 1;
			}
			if(hare.getX() > hounds.get(1).getX()) {
				cutoff_value += manhattan_distance(hare, b.getPosition().get(0)) +
						manhattan_distance(hare, hounds.get(1)) - 1;
			}
			if(hare.getX() > hounds.get(2).getX()) {
				cutoff_value += manhattan_distance(hare, b.getPosition().get(0)) +
						manhattan_distance(hare, hounds.get(2)) - 1;
			}
			if(hare.getX() == hounds.get(0).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(0));
			}
			if(hare.getX() == hounds.get(1).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(1));
			}
			if(hare.getX() == hounds.get(2).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(2));
			}
			
		} else {
			//computer on hare side
			cutoff_value = manhattan_distance(hare, b.getPosition().get(0)) -
					manhattan_distance(hare, b.getPosition().get(6));
			if(hare.getX() > hounds.get(0).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(0)) - 1;
			}
			if(hare.getX() > hounds.get(1).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(1)) - 1;
			}
			if(hare.getX() > hounds.get(2).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(2)) - 1;
			}
			if(hare.getX() == hounds.get(0).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(0));
			}
			if(hare.getX() == hounds.get(1).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(1));
			}
			if(hare.getX() == hounds.get(2).getX()) {
				cutoff_value += manhattan_distance(hare, hounds.get(2));
			}
		}
		return cutoff_value;
	}
	
	public int get_side() {
		return this.computer;
	}
	
	public int manhattan_distance(Position start, Position end) {
		int md = 0;
		md += end.getX() - start.getX();
		md += end.getY() - start.getY();
		return md/100;
	}
}

final class hound_ref {
	hound_ref() {}
	
	static final int ref[][] = {
		{0,0,0,2,2,2,0,0,0,0,1},
		{0,2,0,0,2,2,1,0,0,0,0},
		{0,0,2,0,2,2,1,0,0,0,0},
		{0,0,0,0,2,2,1,2,0,0,0},
		{0,0,0,0,2,2,1,0,2,0,0},
		{0,0,0,0,2,2,1,0,0,2,0},
		{0,0,0,2,2,1,0,0,0,2,0},
		{0,0,0,2,1,2,0,0,2,0,0},
		{0,2,0,0,2,2,1,0,0,0,0},
		{0,2,0,2,1,2,0,0,0,0,0},
		{0,0,2,2,2,0,0,0,0,1,0},
		{0,2,0,2,0,2,0,0,0,0,1},
		{0,0,2,2,2,0,0,0,0,0,1},
		{0,0,0,2,2,1,0,2,0,0,0},
		{0,0,0,2,1,2,0,2,0,0,0},
		{0,0,2,0,2,0,1,0,0,2,0},
		{0,2,0,0,0,2,1,0,2,0,0},
		{0,2,0,2,2,0,0,0,0,0,1},
		{0,0,2,2,0,2,0,0,0,0,1},
		{0,0,0,2,0,0,1,2,0,0,2},
		{0,0,2,0,2,0,1,2,0,0,0},
		{0,2,0,0,0,2,1,2,0,0,0},
		{2,0,0,2,0,2,1,0,0,0,0},
		{2,0,0,2,2,0,1,0,0,0,0},
		{0,2,0,0,2,0,1,2,0,0,0},
		{0,0,2,0,0,2,1,2,0,0,0},
		{0,2,0,2,1,0,0,0,2,0,0},
		{0,0,2,2,0,1,0,0,0,2,0},
		{0,0,2,2,0,1,0,0,0,0,2},
		{0,2,0,2,1,0,0,0,0,0,2},
		{0,2,0,0,2,0,1,0,0,2,0},
		{0,0,2,0,0,2,1,0,2,0,0},
		{0,2,2,0,0,0,1,0,0,0,2},
		{2,0,0,2,0,0,1,0,0,0,2},
		{0,2,0,2,1,0,0,0,0,2,0},
		{0,0,2,2,0,1,0,0,2,0,0},
		{0,2,0,2,0,1,0,0,0,2,0},
		{0,0,2,2,1,0,0,0,2,0,0},
		{0,2,2,2,0,0,0,0,1,0,0},
		{0,2,2,2,0,0,0,0,0,1,0},
		{0,2,2,2,0,0,0,0,0,0,1},
		{2,0,0,2,1,2,0,0,0,0,0},
		{2,0,0,2,2,1,0,0,0,0,0},
		{2,0,0,2,0,2,0,0,0,0,1},
		{2,0,0,2,2,0,0,0,0,0,1},
		{2,0,0,2,0,0,0,0,0,2,1},
		{2,0,0,2,0,0,0,0,2,0,1},
		{0,2,2,0,0,0,1,0,2,0,0},
		{0,2,2,0,0,0,1,0,0,2,0},
		{2,0,0,0,2,0,1,0,2,0,0},
		{2,0,0,0,0,2,1,0,0,2,0},
		{2,0,0,0,2,0,1,0,0,2,0},
		{2,0,0,0,0,2,1,0,2,0,0},
		{2,2,0,0,2,0,1,0,0,0,0},
		{2,0,2,0,2,0,1,0,0,0,0},
		{2,0,2,0,2,0,1,0,0,0,0},
		{2,2,0,0,0,2,1,0,0,0,0},
		{0,0,2,2,1,0,0,2,0,0,0},
		{0,2,0,2,0,1,0,2,0,0,0},
		{0,0,2,2,0,1,0,2,0,0,0},
		{0,2,0,2,1,0,0,2,0,0,0},
		{2,0,0,2,0,1,0,0,0,2,0},
		{2,0,0,2,1,0,0,0,2,0,0},
		{2,0,0,2,0,0,1,0,0,2,0},
		{2,0,0,2,0,0,1,0,2,0,0}
	};
}

