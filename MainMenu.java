// MainMenu.java
// created by Ali Bibak
// Feb 4, 2014

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.*;

// this class contains the main menu
public class MainMenu{

	// constructor
	MainMenu(){
		changeBoard();
		// add action listener to buttons
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				Game.board[i][j].addActionListener(new ActionListener(){
					@Override
          			public void actionPerformed(ActionEvent event){
						Game.runMainMenu();
          			}
				});
			}
	}
	
	// generate a random board
	public void generateBoard(){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				Random random = new Random();
				int r = random.nextInt();
				Game.data[i][j] = Math.abs(r % 3);
			}
	}
	
	// change the board at random
	public void changeBoard(){
		generateBoard();
		Game.paintBoard();
	}
}
