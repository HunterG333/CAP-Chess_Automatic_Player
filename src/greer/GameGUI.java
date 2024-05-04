package greer;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameGUI {
	
	JFrame mainGameWindow;
	JPanel chessboard;
	Piece pieces[];
	ChessBoard startingBoard;
	
	String whitePlayerText = "Player Placeholder";
	String blackPlayerText = "player2 Placeholder";
	
	//stores the drawn dots on the screen
	Piece dots[];
	
	int squareSize = 80;
	int edgeBuffer = 75;
	
	ProjectManager projectManager;
	
	/**
	 * Called upon for general draw functions
	 */
	public GameGUI() {
		
	}
	
	/**
	 * Constructor
	 * @param projectManager A reference to the parent class. Used so that the main window GUI will be pulled back up when the GameGUI is closed
	 */
	public GameGUI(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	
	/**
	 * The main method for creating a game. Called when a game is created
	 * @param gameType The type of game to be played. See showOptionsMenu() in ProjectManager for more information on the String information
	 */
	public void createGame(String gameType, String selectedBot1, String selectedBot2, boolean whiteSelected, Color color1, Color color2, String FEN) {
		
		if(gameType.equals("SP")) {
			if(whiteSelected) {
				whitePlayerText = "Player";
				blackPlayerText = selectedBot1;
			}else {
				blackPlayerText = "Player";
				whitePlayerText = selectedBot1;
			}
		}else if(gameType.equals("TP")) {
			whitePlayerText = "Player 1";
			blackPlayerText = "Player 2";
		}else if(gameType.equals("W")) {
			whitePlayerText = selectedBot1;
			blackPlayerText = selectedBot2;
		}
		
		createGUI();
		drawBoard(color1, color2);
		drawPieces(FEN);
		
		//wait until everything is drawn to initialize the game manager
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {

	            
	            GameManager gameManager;
	            

	            if(gameType.equals("SP")) {
	                if(whiteSelected) {
	                    gameManager = new GameManager(startingBoard, pieces, GameGUI.this, true, false, selectedBot1, null);
	                }else {
	                    gameManager = new GameManager(startingBoard, pieces, GameGUI.this, false, true, selectedBot1, null);
	                }
	            }else if(gameType.equals("TP")) {
	                gameManager = new GameManager(startingBoard, pieces, GameGUI.this, true, true, null, null);
	            }else if(gameType.equals("W")){
	                gameManager = new GameManager(startingBoard, pieces, GameGUI.this, false, false, selectedBot1, selectedBot2);
	            }else{//default case. Single player playing white
	                gameManager = new GameManager(startingBoard, pieces, GameGUI.this, true, false, null, null);
	            }
	            
	            gameManager.startGame();
	        }
	    });
		
	}
	/**
	 * Removes the piece from the GUI
	 * @param piece The piece to remove
	 */
	public void removePiece(Piece piece) {
		chessboard.remove(piece.getJLabel());
	}
	
	/**
	 * Creates the fullscreen GUI that will be used for the game
	 */
	private void createGUI() {
		mainGameWindow = new JFrame("Game Window");
		//mainGameWindow.setLayout(null);
        
        mainGameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximize the window to cover the entire screen
        mainGameWindow.setUndecorated(true);  // Set the window to be borderless

        
        // Add a KeyListener to listen for the "Escape" key
        mainGameWindow.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is the "Escape" key (key code 27)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // Exit fullscreen mode
                    //gd.setFullScreenWindow(null);

                    // Close the game window
                    mainGameWindow.dispose();

                    // Make the main frame visible
                    projectManager.createMainFrame();
                }
            }

            public void keyReleased(KeyEvent e) {}
        });

        // Set the frame to be focusable to receive key events
        mainGameWindow.setFocusable(true);

        // Make the JFrame visible
        mainGameWindow.setVisible(true);
	}
	
	/**
	 * Method that draws the board on the gameGUI
	 * @param color1 The first color for the square
	 * @param color2 The second color for the other square
	 */
	public void drawBoard(Color color1, Color color2) {
		
		
		chessboard = new JPanel(); // Initialize the chessboard panel
        chessboard.setLayout(null); // Use null layout to manually position the squares and pieces
        mainGameWindow.add(chessboard); // Add the chessboard panel to the window
        
        //create JLabels for who is who
    	JLabel whitePlayerLabel = new JLabel(whitePlayerText); // Text to be displayed
    	whitePlayerLabel.setBounds(100, 710, 180, 80);
    	
    	JLabel blackPlayerLabel = new JLabel(blackPlayerText); // Text to be displayed
    	blackPlayerLabel.setBounds(100, 0, 180, 80);
    	
    	whitePlayerLabel.setFont(whitePlayerLabel.getFont().deriveFont(20f));
    	blackPlayerLabel.setFont(blackPlayerLabel.getFont().deriveFont(20f));
    	
    	chessboard.add(blackPlayerLabel);
    	chessboard.add(whitePlayerLabel);
        
        //standard chess board
		for(int row = 0; row < 8; row++) {
	    	for(int column = 0; column < 8; column++) {
	    		
	    		JPanel square = new JPanel();
	            square.setBounds((0 + squareSize*row) + edgeBuffer, (0 + squareSize*column) + edgeBuffer, squareSize, squareSize);
	            square.setBackground((row+column) % 2 == 0 ? color2: color1);
	            chessboard.add(square); // Add the square to the chessboard panel
	            
	    	}
	    	
	    }
			
		
        

	}
	
	 /**
	 * Function to draw pieces to the board
	 * @param FEN The fen input for a custom position
	 */
	public void drawPieces(String FEN) {
    	
		
		pieces = new Piece[64];
		
		startingBoard = new ChessBoard();
		
		if(!FEN.isBlank()) {
			startingBoard = new ChessBoard(FEN);
		}
		
		
		long []boardInfo = startingBoard.getPosition();
		
		long generalPos = boardInfo[0];
		long whitePieces = boardInfo[1];
		long blackPieces = boardInfo[2];
		long whitePawns = boardInfo[3];
		long blackPawns = boardInfo[4];
		long whiteRooks = boardInfo[5];
		long blackRooks = boardInfo[6];
		long whiteKnights = boardInfo[7];
		long blackKnights = boardInfo[8];
		long whiteBishops = boardInfo[9];
		long blackBishops = boardInfo[10];
		long whiteKing = boardInfo[11];
		long blackKing = boardInfo[12];
		long whiteQueens = boardInfo[13];
		long blackQueens = boardInfo[14];
		
		
		String basePath = System.getProperty("user.dir");
		
		//store imageicons outside of loop
		ImageIcon thisPieceImage = null;
		ImageIcon whitePawnImage = new ImageIcon(basePath + "/Images/whitePawn.png");
		ImageIcon blackPawnImage = new ImageIcon(basePath + "/Images/blackPawn.png");
		ImageIcon whiteRookImage = new ImageIcon(basePath + "/Images/whiteRook.png");
		ImageIcon blackRookImage = new ImageIcon(basePath + "/Images/blackRook.png");
		ImageIcon whiteKnightImage = new ImageIcon(basePath + "/Images/whiteKnight.png");
		ImageIcon blackKnightImage = new ImageIcon(basePath + "/Images/blackKnight.png");
		ImageIcon whiteBishopImage = new ImageIcon(basePath + "/Images/whiteBishop.png");
		ImageIcon blackBishopImage = new ImageIcon(basePath + "/Images/blackBishop.png");
		ImageIcon whiteKingImage = new ImageIcon(basePath + "/Images/whiteKing.png");
		ImageIcon blackKingImage = new ImageIcon(basePath + "/Images/blackKing.png");
		ImageIcon whiteQueenImage = new ImageIcon(basePath + "/Images/whiteQueen.png");
		ImageIcon blackQueenImage = new ImageIcon(basePath + "/Images/blackQueen.png");

		
		String pieceType = null;
		
		//loop through to find what piece to draw in what square
		for(int i = 0; i < 64; i++) {
			
			long squareToCheck = 1L << i;
			
			if((squareToCheck & generalPos) != 0) {
				
				if((squareToCheck & whitePieces) != 0) {
					
					if((squareToCheck & whitePawns) != 0) {
						thisPieceImage = whitePawnImage;
						pieceType = "WP";
					} else if((squareToCheck & whiteRooks) != 0) {
						thisPieceImage = whiteRookImage;
						pieceType = "WR";
					}else if((squareToCheck & whiteKnights) != 0) {
						thisPieceImage = whiteKnightImage;
						pieceType = "WN";
					}else if((squareToCheck & whiteBishops) != 0) {
						thisPieceImage = whiteBishopImage;
						pieceType = "WB";
					}else if((squareToCheck & whiteQueens) != 0) {
						thisPieceImage = whiteQueenImage;
						pieceType = "WQ";
					}else if((squareToCheck & whiteKing) != 0) {
						thisPieceImage = whiteKingImage;
						pieceType = "WK";
					}else {
						thisPieceImage = null;
						pieceType = null;
					}
				}else if((squareToCheck & blackPieces) != 0){
	
					if((squareToCheck & blackPawns) != 0) {
						thisPieceImage = blackPawnImage;
						pieceType = "BP";
					}else if((squareToCheck & blackRooks) != 0) {
						thisPieceImage = blackRookImage;
						pieceType = "BR";
					}else if((squareToCheck & blackKnights) != 0) {
						thisPieceImage = blackKnightImage;
						pieceType = "BN";
					}else if((squareToCheck & blackBishops) != 0) {
						thisPieceImage = blackBishopImage;
						pieceType = "BB";
					}else if((squareToCheck & blackQueens) != 0) {
						thisPieceImage = blackQueenImage;
						pieceType = "BQ";
					}else if((squareToCheck & blackKing) != 0) {
						thisPieceImage = blackKingImage;
						pieceType = "BK";
					}else {
						thisPieceImage = null;
						pieceType = null;
					}
					
				}
				
				
				if(thisPieceImage != null) {
					Image image = thisPieceImage.getImage();
			        Image scaledImage = image.getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH);
			        thisPieceImage = new ImageIcon(scaledImage);
			        JLabel newPiece = new JLabel(thisPieceImage);
			        
			        
			        int x = i % 8;
			        int y = i / 8;
			        

			        newPiece.setBounds(edgeBuffer + (x * squareSize), edgeBuffer + ((7 - y) * squareSize), squareSize, squareSize);
			      
			        Piece myPiece = new Piece(newPiece, x, y, pieceType);
			        
			        if(myPiece.pieceType.equals("K")) {
			        	if(myPiece.pieceColor.equals("W")) {
			        		startingBoard.setKing("W", myPiece);
			        	}else {
			        		startingBoard.setKing("B", myPiece);
			        	}
			        }
			        
			        
			        pieces[i] = myPiece;
			     
			        chessboard.add(newPiece); // Add the piece to the chessboard panel
			        chessboard.setComponentZOrder(newPiece, 0); // Set the z-order within the chessboard panel
				} else {
					pieces[i] = null;
				}
		        
				
			}else {
		        pieces[i] = null;
			}
			
			
		}
			
		
	}
	
	
	/**
	 * Function for drawing dots with the given moves
	 * @param possibleMoves an array of the possible moves that can be played
	 * @param piece the piece that we are moving
	 * @param currentBoard the board that we are moving the piece on
	 */
	public Piece[] drawDots(String []possibleMoves, Piece piece, ChessBoard currentBoard) {
		
		dots = new Piece[possibleMoves.length];
		
		for(int i = 0; i < possibleMoves.length; i++) {
			int xCoord = Integer.parseInt("" + possibleMoves[i].charAt(0));
			int yCoord = Integer.parseInt("" + possibleMoves[i].charAt(1));
			
			String basePath = System.getProperty("user.dir");
			
			
			//check if enemy occupies space
			boolean isWhite = piece.getPieceColor().equals("W");
			long enemyBitboard;
			if(isWhite) {
				enemyBitboard = currentBoard.getBlackBoard();
			}else {
				enemyBitboard = currentBoard.getWhiteBoard();
			}
		
			
			long currentSquare = 1L << (xCoord + 8 * yCoord);
			long enemyAttackedBitBoard = currentSquare & enemyBitboard;
			
			ImageIcon dotImage = new ImageIcon(basePath + "/Images/dot.png");
			if(enemyAttackedBitBoard != 0) {
				dotImage = new ImageIcon(basePath + "/Images/redDot.png");
			}
			
			
			// Get the original image
	        Image originalImage = dotImage.getImage();
	        // Scale the original image
	        Image scaledImage = originalImage.getScaledInstance((int)(squareSize / 1.5), (int)(squareSize / 1.5), Image.SCALE_SMOOTH);
	        // Create a new ImageIcon with the scaled image
	        dotImage = new ImageIcon(scaledImage);

	        JLabel dot = new JLabel(dotImage);
	        Piece dotPiece = new Piece(dot, xCoord, yCoord, "dot");
	        dots[i] = dotPiece;
			
	        
	        
	        dot.setBounds(
	        		edgeBuffer + (xCoord * squareSize), 
	        		edgeBuffer + ((7 - yCoord) * squareSize), 
	        		squareSize, 
	        		squareSize
	        	);

	        chessboard.add(dot); // Add the piece to the chessboard panel
	        chessboard.setComponentZOrder(dot, 0);
	       
		}
		
		chessboard.repaint();
	    
	    return dots;
		
	}
	
	/**
	 * Moves the piece on the game window
	 * @param pieceToMove The piece to move
	 * @param dotClickedOn The dot that was drawn that was clicked on
	 */
	public void movePiece(Piece pieceToMove, Piece dotClickedOn) {
		
		int coordX = dotClickedOn.getX();
		int coordY = dotClickedOn.getY();
		
		JLabel pieceLabel = pieceToMove.getJLabel();
		
		pieceLabel.setBounds(edgeBuffer + (coordX * squareSize), edgeBuffer + ((7 - coordY) * squareSize), squareSize, squareSize);
		
		mainGameWindow.repaint();
	}
	
	/**
	 * Clears the board
	 */
	public void clearBoard() {
		mainGameWindow.remove(chessboard);
		mainGameWindow.repaint();
	}
	
	/**
	 * Clears the dots off of the board
	 */
	public void clearDots() {
		if(dots != null) {
			for(int i = 0; i < dots.length; i++) {
				chessboard.remove(dots[i].getJLabel());
			}
			dots = null;
			chessboard.repaint();
		}
	}
	
	public void repaint() {
		mainGameWindow.repaint();
	}

}
