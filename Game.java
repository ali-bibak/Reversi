// Game.java
// created by Ali Bibak
// Feb 4, 2014

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// this class contains the main method and the 
// coherence between main menu and the game
// and the graphical functions
public class Game extends JFrame{

	protected static MainMenu menu;
	protected static Reversi reversi;

	protected static boolean isMainMenu;

	// contains the image of piece
	protected static String piece[] = {"data/blue.png", "data/white.png"};

	protected static JLayeredPane panel;
	
	// data[][] contains information about the game board
	// data[i][j] = 0 (first player), 1 (second player), 2 (empty)
	// it will also be dealt with within Reversi & MainMenu
	static int[][] data;
	
	// the worth of a square
	protected static int[][] worth;

	// board[][] contains the squares (buttons) of the game board
	// it will also be dealt with within Reversi & MainMenu
	static JButton[][] board;

	protected static int layer;
	
	protected static JLabel turn;

	protected static int cx = 150, cy = 120;

	protected static Color myRed;

	// constructor
	Game(){
		isMainMenu = true;

		setTitle("Reversi");
		setSize(700, 700);
		setLocation(350, 50);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel = new JLayeredPane();
		panel.setLayout(null);
		getContentPane().add(panel);

		myRed = new Color(204, 0, 0);

		data = new int[8][8];
		worth = new int[8][8];

		setWorth();

		initialize();
		
		// run main menu on start
		runMainMenu();
	}

	// initialize data & graphics
	public static void initialize(){
		layer = 1;
		resetData();
		panel.removeAll();
		panel.validate();
		panel.repaint();
		paintInitialGraphics(isMainMenu);
	}

	// set data[][] to 2 (empty)
	public static void resetData(){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				// empty
				data[i][j] = 2;
	}

	// calculate worth[][]
	// 3 for ordinary positions
	// 4 for sides
	// 6 for corners
	public static void setWorth(){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				int w = 0;
				if(i == 0 || i == 7)
					w++;
				if(j == 0 || j == 7)
					w++;
				if(w == 0)
					worth[i][j] = 3;
				if(w == 1)
					worth[i][j] = 4;
				else
					worth[i][j] = 6;
			}
	}

	// paint the initial graphics
	public static void paintInitialGraphics(boolean isMainMenu){
		// quit button
		JButton quitButton = new JButton("Quit");
       	quitButton.setBounds(470, 550, 80, 30);
       	quitButton.addActionListener(new ActionListener(){
			@Override
          	public void actionPerformed(ActionEvent event){
               System.exit(0);
          	}
		});
		panel.add(quitButton);

		// paint the board
		board = new JButton[8][8];
		
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++){
				board[i][j] = new JButton();
				board[i][j].setBounds(cx + 50 * i, cy + 50 * j, 50, 50);
				if((i + j) % 2 == 0)
					board[i][j].setBackground(Color.black);
				else
					board[i][j].setBackground(myRed);
				panel.add(board[i][j]);
			}

		// main menu
		if(isMainMenu){
			// title
			JLabel label = new JLabel("<html><h1>Reversi</h1><br/></html>");
			label.setFont(new Font("Myriad Pro",Font.PLAIN,15));
			label.setBounds(300, 40, 200, 100);
			label.setForeground(myRed);
			panel.add(label, new Integer(layer++));
			JLabel ali = new JLabel("by Ali Bibak");
			ali.setBounds(310, 55, 200, 100);
			panel.add(ali, new Integer(layer++));

			// one player button
			JButton onePlayerButton = new JButton("One Player");
       		onePlayerButton.setBounds(150, 550, 120, 30);
       		onePlayerButton.addActionListener(new ActionListener(){
				@Override
       		   	public void actionPerformed(ActionEvent event){
					// run one player reversi
       		       	runReversi(true);
       		   	}
			});
			panel.add(onePlayerButton);

			// two player button
			JButton twoPlayerButton = new JButton("Two Player");
       		twoPlayerButton.setBounds(310, 550, 120, 30);
       		twoPlayerButton.addActionListener(new ActionListener(){
				@Override
        	  	public void actionPerformed(ActionEvent event){
					// run two player reversi
        	      	runReversi(false);
        	  	}
			});
			panel.add(twoPlayerButton);
		}
		
		// game
		else{
			// main menu button
			JButton menuButton = new JButton("Main Menu");
	       	menuButton.setBounds(150, 550, 120, 30);
	       	menuButton.addActionListener(new ActionListener(){
				@Override
   		       	public void actionPerformed(ActionEvent event){
	              	runMainMenu();
	          	}
			});
			panel.add(menuButton);

			// reset button
			JButton resetButton = new JButton("Reset");
    	   	resetButton.setBounds(310, 550, 120, 30);
    	   	resetButton.addActionListener(new ActionListener(){
				@Override
       		   	public void actionPerformed(ActionEvent event){
					initialize();
					runReversi(reversi.isOnePlayer);
        	  	}
			});
			panel.add(resetButton);

		}
	}

	// run main menu
	public static void runMainMenu(){
		isMainMenu = true;
		initialize();
		menu = new MainMenu();
	}

	// run reversi
	public static void runReversi(boolean isOnePlayer){
		isMainMenu = false;
		initialize();
		reversi = new Reversi(isOnePlayer);
	}
	
	// paint whose turn it is
	public static void paintTurn(int player, boolean firstTime){
		if(!firstTime){
			panel.remove(turn);
			panel.validate();
			panel.repaint();
		}
		if(player == 2){
			turn = new JLabel("GAME ENDED");
			turn.setBounds(460, 50, 200, 100);
			panel.add(turn, new Integer(layer++));
		}
		else if(player == 0){
			turn = new JLabel("BLUE'S TURN");
			turn.setBounds(460, 50, 200, 100);
			panel.add(turn, new Integer(layer++));
		}
		else if(player == 1){
			turn = new JLabel("WHITE'S TURN");
			turn.setBounds(450, 50, 200, 100);
			panel.add(turn, new Integer(layer++));
		}
	}

	// paint the winner
	public static void paintWinner(int player){
		if(reversi.isOnePlayer){
			if(player == 1){
				JLabel display = new JLabel("I WON!");
				display.setBounds(150, 50, 200, 100);
        		panel.add(display, new Integer(layer++));
			}
			else if(player == 0){
				JLabel display = new JLabel("YOU WON!");
				display.setBounds(150, 50, 200, 100);
        		panel.add(display, new Integer(layer++));
			}
			else{
				JLabel display = new JLabel("WE HAVE A TIE!");
				display.setBounds(150, 50, 200, 100);
        		panel.add(display, new Integer(layer++));
			}
		}
		else{
			if(player == 1){
				JLabel winner = new JLabel("WHITE WON!");
				winner.setBounds(150, 50, 200, 100);
	    	    panel.add(winner, new Integer(layer++));
			}
			else if(player == 0){
				JLabel winner = new JLabel("BLUE WON!");
				winner.setBounds(150, 50, 200, 100);
		        panel.add(winner, new Integer(layer++));
			}
			else{
				JLabel winner = new JLabel("WE HAVE A TIE!");
				winner.setBounds(150, 50, 200, 100);
		        panel.add(winner, new Integer(layer++));
			}
		}
	}

	// paint a piece
	public static void paintPiece(int i, int j, int player){
		JLabel display = new JLabel();
		display.setLocation(cx + i * 50 + 3, cy + j * 50);
		display.setSize(50, 50);
		display.setIcon(new ImageIcon(ClassLoader.getSystemResource(piece[player])));
        panel.add(display, new Integer(layer++));
	}

	// paint the pieces on board
	public static void paintBoard(){
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				if(data[i][j] != 2)
					paintPiece(i, j, data[i][j]);
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
	    	@Override
    		public void run(){
				Game game = new Game();
				game.setVisible(true);
    		}
		});	
	}
}
