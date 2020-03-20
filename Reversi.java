// Reversi.java
// created by Ali Bibak
// Feb 4, 2014

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// contains the logic of the game
public class Reversi{
	
	boolean isOnePlayer;
	int whoseTurn;

	// next board after a move
	int[][] next;

	// the final board to decide
	// for AI in one player mode
	int[][] to;

	boolean up, down, left, right, upright, upleft, downright, downleft;

	// constructor
	Reversi(boolean _isOnePlayer){
		isOnePlayer = _isOnePlayer;

		next = new int[8][8];
		to = new int[8][8];

		Game.data[3][3] = Game.data[4][4] = 1;
		Game.data[3][4] = Game.data[4][3] = 0;
		Game.paintBoard();

		whoseTurn = 0;
		Game.paintTurn(whoseTurn, true);
		
		boolean dummy = moveLeft(whoseTurn);

		// add action listener to buttons
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				final int x = i, y = j;
				Game.board[i][j].addActionListener(new ActionListener(){
					@Override
          			public void actionPerformed(ActionEvent event){
               			if(isOnePlayer){
							if(checkMove(x, y, 0)){
								for(int a = 0; a < 8; a++)
									for(int b = 0; b < 8; b++)
										next[a][b] = Game.data[a][b];
								calcMove(x, y, 0);
								for(int a = 0; a < 8; a++)
									for(int b = 0; b < 8; b++)
										Game.data[a][b] = next[a][b];
								Game.paintBoard();
								makeMove();
							}
						}
						else{
							if(checkMove(x, y, whoseTurn)){
								for(int a = 0; a < 8; a++)
									for(int b = 0; b < 8; b++)
										next[a][b] = Game.data[a][b];
								calcMove(x, y, whoseTurn);
								for(int a = 0; a < 8; a++)
									for(int b = 0; b < 8; b++)
										Game.data[a][b] = next[a][b];
								Game.paintBoard();
								if(moveLeft(1 - whoseTurn)){
									whoseTurn = 1 - whoseTurn;
									Game.paintTurn(whoseTurn, false);
								}
								else if(!moveLeft(whoseTurn)){
									int whiteScore = 0, blueScore = 0;
									Game.paintTurn(2, false);
									for(int a = 0; a < 8; a++)
										for(int b = 0; b < 8; b++)
											if(Game.data[a][b] == 0)
												blueScore++;
											else if(Game.data[a][b] == 1)
												whiteScore++;
									if(blueScore > whiteScore)
										Game.paintWinner(0);
									else if(blueScore < whiteScore)
										Game.paintWinner(1);
									else
										Game.paintWinner(2);
								}

							}
						}
          			}
				});
		}
	}	

	public boolean suit(int x, int player){
		return x != 2 && x != player;
	}

	// check if the move is legal
	public boolean checkMove(int i, int j, int player){
		if(Game.data[i][j] != 2)
			return false;
		int a, b;
		up = down = left = right = upright = upleft = downright = downleft = false;
		// up
		a = i;
		b = j-1;
		while(b >= 0 && suit(Game.data[a][b], player))
			b--;
		if(b >= 0 && b != j-1 && Game.data[a][b] == player)
			up = true;
		// down
		a = i;
		b = j+1;
		while(b < 8 && suit(Game.data[a][b], player))
			b++;
		if(b < 8 && b != j+1 && Game.data[a][b] == player)
			down = true;
		// left
		a = i-1;
		b = j;
		while(a >= 0 && suit(Game.data[a][b], player))
			a--;
		if(a >= 0 && a != i-1 && Game.data[a][b] == player)
			left = true;
		// right
		a = i+1;
		b = j;
		while(a < 8 && suit(Game.data[a][b], player))
			a++;
		if(a < 8 && a != i+1 && Game.data[a][b] == player)
			right = true;
		// upright
		a = i+1;
		b = j-1;
		while(a < 8 && b >= 0 && suit(Game.data[a][b], player)){
			a++;
			b--;
		}
		if(a < 8 && b >= 0 && a != i+1 && Game.data[a][b] == player)
			upright = true;
		// upleft
		a = i-1;
		b = j-1;
		while(a >= 0 && b >= 0 && suit(Game.data[a][b], player)){
			a--;
			b--;
		}
		if(a >= 0 && b >= 0 && a != i-1 && Game.data[a][b] == player)
			upleft = true;
		// downright
		a = i+1;
		b = j+1;
		while(a < 8 && b < 8 && suit(Game.data[a][b], player)){
			a++;
			b++;
		}
		if(a < 8 && b < 8 && a != i+1 && Game.data[a][b] == player)
			downright = true;
		// downleft
		a = i-1;
		b = j+1;
		while(a >= 0 && b < 8 && suit(Game.data[a][b], player)){
			a--;
			b++;
		}
		if(a >= 0 && b < 8 && a != i-1 && Game.data[a][b] == player)
			downleft = true;
		return up || down || left || right || upleft || upright || downleft || downright;
	}

	// find the best next move for AI
	public void makeMove(){
		int	X = 0, Y = 0, max = 0;
		boolean found = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				int x = i, y = j;
				if(checkMove(x, y, 1)){
					found = true;
					for(int a = 0; a < 8; a++)
						for(int b = 0; b < 8; b++)
							next[a][b] = Game.data[a][b];
					calcMove(x, y, 1);
					int s = getWorth();
					if(s > max){
						max = s;
						X = x;
						Y = y;
						for(int a = 0; a < 8; a++)
							for(int b = 0; b < 8; b++)
								to[a][b] = next[a][b];
					}
				}
			}
		if(found){
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)
					Game.data[i][j] = to[i][j];
			Game.paintBoard();
			if(!moveLeft(0))
				makeMove();
		}
		else if(!moveLeft(0)){
			Game.paintTurn(2, false);
			int cpuScore = 0, humanScore = 0;
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++)
					if(Game.data[i][j] == 0)
						humanScore++;
					else if(Game.data[i][j] == 1)
						cpuScore++;
			if(humanScore > cpuScore)
				Game.paintWinner(0);
			else if(humanScore < cpuScore)
				Game.paintWinner(1);
			else
				Game.paintWinner(2);
		}
	}

	// check if there are any moves left for a player
	public boolean moveLeft(int player){
		boolean ret = false;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				int x = i, y = j;
				if(checkMove(x, y, player)){
					Game.board[x][y].setBackground(Color.darkGray);
					ret = true;
				}
				else{
					if((x + y) % 2 == 0)
						Game.board[x][y].setBackground(Color.BLACK);
					else
						Game.board[x][y].setBackground(Game.myRed);
				}
			}
		return ret;
	}

	// change the board after the move
	public void calcMove(int i, int j, int player){
		next[i][j] = player;
		int a, b;
		// up
		if(up){
			a = i;
			b = j-1;
			while(b >= 0 && suit(next[a][b], player)){
				next[a][b] = player;
				b--;
			}
		}
		// down
		if(down){
			a = i;
			b = j+1;
			while(b < 8 && suit(next[a][b], player)){
				next[a][b] = player;
				b++;
			}
		}
		// left
		if(left){
			a = i-1;
			b = j;
			while(a >= 0 && suit(next[a][b], player)){
				next[a][b] = player;
				a--;
			}
		}
		// right
		if(right){
			a = i+1;
			b = j;
			while(a < 8 && suit(next[a][b], player)){
				next[a][b] = player;
				a++;
			}
		}
		// upright
		if(upright){
			a = i+1;
			b = j-1;
			while(a < 8 && b >= 0 && suit(next[a][b], player)){
				next[a][b] = player;
				a++;
				b--;
			}
		}
		// upleft
		if(upleft){
			a = i-1;
			b = j-1;
			while(a >= 0 && b >= 0 && suit(next[a][b], player)){
				next[a][b] = player;
				a--;
				b--;
			}
		}
		// downright
		if(downright){
			a = i+1;
			b = j+1;
			while(a < 8 && b < 8 && suit(next[a][b], player)){
				next[a][b] = player;
				a++;
				b++;
			}
		}
		// downleft
		if(downleft){
			a = i-1;
			b = j+1;
			while(a >= 0 && b < 8 && suit(next[a][b], player)){
				next[a][b] = player;
				a--;
				b++;
			}
		}
	}

	// get the worth of a possible move
	public int getWorth(){
		int w = 0;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				if(next[i][j] == 1)
					w += Game.worth[i][j];
		return w;
	}
}
