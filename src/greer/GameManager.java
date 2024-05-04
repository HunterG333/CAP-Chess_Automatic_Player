package greer;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class GameManager {
	
	Bot botV0;
	Bot botV1;
	Bot botV2;
	Bot botV3;
	
	Bot botPlayingWhite;
	Bot botPlayingBlack;
	
	ChessBoard chessBoard;
	GameLogic gameLogic;
	Piece[] pieces;
	boolean whiteTurn;
	boolean gamePlaying;
	boolean whiteIsPlayer;
	boolean blackIsPlayer;
	boolean whiteCanClick;
	boolean blackCanClick;
	GameGUI gameGUI;
	
	Piece enPassantPiece;
	
	/**
	 * The constructor for the GameManger object. This class manages everything about the game
	 * @param chessBoard The starting board state. Will be used to store the information about the current game
	 */
	public GameManager(ChessBoard chessBoard, Piece[] pieces, GameGUI gameGUI, boolean whiteIsPlayer, boolean blackIsPlayer, String selectedBot1, String selectedBot2) {
		this.chessBoard = chessBoard;
		this.pieces = pieces;
		this.gameGUI = gameGUI;
		
		this.whiteIsPlayer = whiteIsPlayer;
		this.blackIsPlayer = blackIsPlayer;
		enPassantPiece = null;
		
		//bot initilization
		botV0 = new BotV0();
		botV1 = new BotV1();
		botV2 = new BotV2();
		botV3 = new BotV3();
		
		if(selectedBot1 != null) {
			
			if(selectedBot1.equals("BotV1")) {
				
				if(selectedBot2 == null) {
					
					if(!whiteIsPlayer) {//white is a bot and singleplayer is selected
						botPlayingWhite = botV1;
					}else {//black is a bot and singleplayer is selcted
						botPlayingBlack = botV1;
					}
					
				}else {//watch is selected so set selectedBot1 to white
					botPlayingWhite = botV1;
				}
				
			}else if(selectedBot1.equals("BotV2")) {
				
				if(selectedBot2 == null) {
					
					if(!whiteIsPlayer) {//white is a bot and singleplayer is selected
						botPlayingWhite = botV2;
					}else {//black is a bot and singleplayer is selcted
						botPlayingBlack = botV2;
					}
					
				}else {//watch is selected so set selectedBot1 to white
					botPlayingWhite = botV2;
				}
				
			}else if(selectedBot1.equals("BotV3")) {
				
				if(selectedBot2 == null) {
					
					if(!whiteIsPlayer) {//white is a bot and singleplayer is selected
						botPlayingWhite = botV3;
					}else {//black is a bot and singleplayer is selcted
						botPlayingBlack = botV3;
					}
					
				}else {//watch is selected so set selectedBot1 to white
					botPlayingWhite = botV3;
				}
				
			}
			
		}
		if(selectedBot2 != null) {
			
			if(selectedBot2.equals("BotV1")) {
				
				botPlayingBlack = botV1;
				
			}else if(selectedBot2.equals("BotV2")) {
				
				botPlayingBlack = botV2;
				
			}else if(selectedBot2.equals("BotV3")) {
				
				botPlayingBlack = botV3;
				
			}
			
		}
	}
	
	/**
	 * Function that starts the game and allows players to play
	 */
	public void startGame() {
		
		
		
		//do not change unless debugging
		gamePlaying = true;
		whiteCanClick = true;
		blackCanClick = false;
		
		
		//set on click listeners for pieces
		for(int i = 0; i< pieces.length; i++) {
			if(pieces[i] != null) {
				
				
				
				Piece piece = pieces[i];

				pieces[i].getJLabel().addMouseListener(new MouseAdapter() {
			            @Override
			            public void mouseClicked(MouseEvent e) {
			                pieceClicked(piece);
			            }
			        });
				
			}
		}
		
		
		gameLogic = new GameLogic();
		whiteTurn = chessBoard.startingPlay.equals("w")? true:false;
		
		chessBoard.updateAttackedSquares();
		
		gameLoop();
		
	}
	
	/**
	 * The game loop for the Chess Game
	 */
	public void gameLoop() {
		
		if(gamePlaying) {
			if(whiteTurn) {
				if(whiteIsPlayer) {
					//allow click pieces for white
					whiteCanClick = true;
				}else {
					//bot plays a move
					HashMap<Piece, String> moveToPlay = botPlayingWhite.selectMove(chessBoard, pieces, "W");
					Set<Piece> pieceSet = moveToPlay.keySet();
					List<Piece> pieceList = new ArrayList<>(pieceSet);
					Piece pieceToMove = pieceList.get(0);
					String tempDotCoords = moveToPlay.get(pieceToMove);
					Piece tempPiece = createTempPiece(tempDotCoords);
					movePiece(tempPiece, pieceToMove);
				}
			} else {
				if(blackIsPlayer) {
					//allow click pieces for black
					blackCanClick = true;
				}else {
					//bot plays a move
					HashMap<Piece, String> moveToPlay = botPlayingBlack.selectMove(chessBoard, pieces, "B");
					Set<Piece> pieceSet = moveToPlay.keySet();
					List<Piece> pieceList = new ArrayList<>(pieceSet);
					Piece pieceToMove = pieceList.get(0);
					String tempDotCoords = moveToPlay.get(pieceToMove);
					Piece tempPiece = createTempPiece(tempDotCoords);
					movePiece(tempPiece, pieceToMove);
					
				}
			}
		}
		
	}
	
	/**
	 * Logic for gathering everything that is needed when a piece is clicked
	 * @param piece The piece that has been clicked
	 */
	public void pieceClicked(Piece piece) {
		
		if(whiteTurn && piece.pieceColor.equals("W") && whiteCanClick) {
			gameLogic.setClickedPiece(piece);
			String possibleMoves[] = gameLogic.getClickedPieceLegalMoves(chessBoard, pieces);
			
			//sometimes the possibleMoves will return an empty character. We need to filter this out
			if(!possibleMoves[0].equals("")) {
				drawMoves(possibleMoves, piece);
			}else {//to show that there are no possible moves for that piece
				gameGUI.clearDots();
			}
			
		}else if(!whiteTurn && piece.pieceColor.equals("B") && blackCanClick) {
			gameLogic.setClickedPiece(piece);
			String possibleMoves[] = gameLogic.getClickedPieceLegalMoves(chessBoard, pieces);
			
			//sometimes the possibleMoves will return an empty character. We need to filter this out
			if(!possibleMoves[0].equals("")) {
				drawMoves(possibleMoves, piece);
			}else {//to show that there are no possible moves for that piece
				gameGUI.clearDots();
			}
			
		}
		
		
		
	}
	
	/**
	 * Draws the moves as dots on the GUI
	 * @param possibleMoves The possible moves that can be done
	 * @param piece The piece that we are drawing dots for their moves
	 */
	public void drawMoves(String []possibleMoves, Piece piece) {
		gameGUI.clearDots();
		Piece dotsArr[] = gameGUI.drawDots(possibleMoves, piece, chessBoard);
		
		createClickListeners(dotsArr, piece);
		
	}
	
	/**
	 * Creates click listeners for the dots so that the user can click on them
	 * @param dotsArr An array of the dots that are drawn on the screen
	 * @param pieceToMove The piece that was clicked on that we want to move
	 */
	public void createClickListeners(Piece[] dotsArr, Piece pieceToMove) {
		for(int i = 0; i < dotsArr.length; i++) {
			Piece dotToClick = dotsArr[i];
			dotsArr[i].getJLabel().addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	               movePiece(dotToClick, pieceToMove);
	            }
	        });
		
		}
	}
	
	/**
	 * Method handles everything about moving a piece from square to square
	 * @param dotToMoveTo The dot that the user has selected to mvoe to
	 * @param pieceToMove The piece that is getting moved
	 */
	public void movePiece(Piece dotToMoveTo, Piece pieceToMove) {
		
		int origX = pieceToMove.getX();
		int origY = pieceToMove.getY();
		
		int newX = dotToMoveTo.getX();
		int newY = dotToMoveTo.getY();
		
		//need all logic for piece movement to go here
		chessBoard.updateBitboard(pieceToMove, dotToMoveTo);
		
		gameGUI.movePiece(pieceToMove, dotToMoveTo);
		gameGUI.clearDots();
		
		pieceToMove.moveTo(dotToMoveTo);
		
		
		//special piece rules update
		//need to check for if a piece is enPassanted it will be taken
		if(enPassantPiece != null) {
			enPassantPiece.enPassantable = false;
			enPassantPiece = null;
		}
		
		if(pieceToMove.getPieceType().equals("K")) {
			if(pieceToMove.getPieceColor().equals("W")) {
				if(origX - newX < -1) {
					
					Piece rookToMove = pieces[7];
					
					//create temp piece to use to move the rook to
					Piece tempPiece = new Piece(null, 5, 0, "00");
					rookToMove.moveTo(tempPiece);
					chessBoard.updateBitboard(rookToMove, tempPiece);
					gameGUI.movePiece(rookToMove, tempPiece);
					pieces[5] = pieces[7];
					pieces[7] = null;
					chessBoard.clearSquare(1L << 7);
					
				}else if(origX - newX > 1) {
					
					Piece rookToMove = pieces[0];
					
					//create temp piece to use to move the rook to
					Piece tempPiece = new Piece(null, 3, 0, "00");
					rookToMove.moveTo(tempPiece);
					chessBoard.updateBitboard(rookToMove, tempPiece);
					gameGUI.movePiece(rookToMove, tempPiece);
					pieces[3] = pieces[0];
					pieces[0] = null;
					chessBoard.clearSquare(1L);
					
				}
			}else {
				if(origX - newX > 1) {

					Piece rookToMove = pieces[56];
					
					//create temp piece to use to move the rook to
					Piece tempPiece = new Piece(null, 3, 7, "00");
					rookToMove.moveTo(tempPiece);
					chessBoard.updateBitboard(rookToMove, tempPiece);
					gameGUI.movePiece(rookToMove, tempPiece);
					pieces[59] = pieces[56];
					pieces[56] = null;
					chessBoard.clearSquare(1L << 56);
					
				}else if(origX - newX < -1) {

					Piece rookToMove = pieces[63];
					
					//create temp piece to use to move the rook to
					Piece tempPiece = new Piece(null, 5, 7, "00");
					rookToMove.moveTo(tempPiece);
					chessBoard.updateBitboard(rookToMove, tempPiece);
					gameGUI.movePiece(rookToMove, tempPiece);
					pieces[61] = pieces[63];
					pieces[63] = null;
					chessBoard.clearSquare(1L << 63);
					
				}
			}
			
		}
		
		if(pieceToMove.getPieceType().equals("K") || pieceToMove.getPieceType().equals("R")) {
			pieceToMove.castleRights = false;
		}
		
		
		
		if(pieceToMove.getPieceType().equals("P")) {
			pieceToMove.canMoveTwo = false;
			
			//piece can be en passanted check
			if(Math.abs(origY - newY) > 1) {
				pieceToMove.enPassantable = true;
				enPassantPiece = pieceToMove;
			}
			
			//piece was enpassanted
			if((Math.abs(origY - newY) == 1 && Math.abs(origX - newX) == 1) && pieces[newX + (8 * newY)] == null) {
				//remove piece that was enpassanted
				if(pieceToMove.getPieceColor().equals("W")) {
					gameGUI.removePiece(pieces[newX + (8 * (newY - 1))]);
					chessBoard.clearSquare(1L << (newX + (8 * (newY - 1))));
				}else {
					gameGUI.removePiece(pieces[newX + (8 * (newY + 1))]);
					chessBoard.clearSquare(1L << (newX + (8 * (newY + 1))));
				}
			}
			
			//check for pawn promotion
			if(pieceToMove.getPieceColor().equals("W")) {
				
				if(pieceToMove.getY() == 7) {
					pawnPromotion(pieceToMove);
				}
				
			}else {
				
				if(pieceToMove.getY() == 0) {
					pawnPromotion(pieceToMove);
				}
				
			}
		}
		
		if(pieces[newX + (8 * newY)] != null) {
			gameGUI.removePiece(pieces[newX + (8 * newY)]);
		}
		
		//update pieces array
		pieces[newX + (8 * newY)] = pieces[origX + (8 * origY)];
		pieces[origX + (8 * origY)] = null;
		
		
		
		//update attackedSquares bitboard
		chessBoard.updateAttackedSquares();
		
		
		
		//wait until everything is done to play a move
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	
	        	if(whiteIsPlayer) {
	    			whiteCanClick = !whiteCanClick;
	    		}
	    		if(blackIsPlayer) {
	    			blackCanClick = !blackCanClick;
	    		}
	        	
	        	switchTurn();
	    		
	    		//check for end game scenarios
	    		String colorToCheck = whiteTurn ? "W" : "B";
	    		boolean isGameOver = gameLogic.isGameOver(chessBoard, colorToCheck, pieces);
	    		if(isGameOver) {
	    			if(gameLogic.isKingMated(chessBoard, colorToCheck, pieces)) {
	    				System.out.println(colorToCheck + " has been CheckMated. Press ESC to exit");
	    				gamePlaying = false;
	    			}else {
	    				System.out.println(colorToCheck + " has been StaleMated. Press ESC to exit");
	    				gamePlaying = false;
	    			}
	    		}
	    		
	    		gameLoop();
	        	
	        }
		});
	}
	
	/**
	 * Called when the piece has the right paramaters to promote
	 * @param piece The piece to promote
	 */
	public void pawnPromotion(Piece piece) {
		
		//white is playing and the piece promoting is white
		if(whiteIsPlayer && piece.getPieceColor().equals("W")) {
			
			JFrame promotionWindow = new JFrame("Promotion Window");
			promotionWindow.setBounds(0, 0, 320, 100);
			promotionWindow.setUndecorated(true);  // Set the window to be borderless
			promotionWindow.setLayout(new GridLayout(1, 4));  // Use GridLayout to evenly distribute components
			promotionWindow.setVisible(true);

			String basePath = System.getProperty("user.dir");

			//store imageicons
			ImageIcon whiteRookImage = new ImageIcon(basePath + "/Images/whiteRook.png");
			ImageIcon whiteKnightImage = new ImageIcon(basePath + "/Images/whiteKnight.png");
			ImageIcon whiteBishopImage = new ImageIcon(basePath + "/Images/whiteBishop.png");
			ImageIcon whiteQueenImage = new ImageIcon(basePath + "/Images/whiteQueen.png");

			// Scale images
			Image image = whiteRookImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			whiteRookImage = new ImageIcon(image);
			Image image2 = whiteKnightImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			whiteKnightImage = new ImageIcon(image2);
			Image image3 = whiteBishopImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			whiteBishopImage = new ImageIcon(image3);
			Image image4 = whiteQueenImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			whiteQueenImage = new ImageIcon(image4);

			// Create JLabels for each piece
			JLabel knightPiece = new JLabel(whiteKnightImage);
			JLabel bishopPiece = new JLabel(whiteBishopImage);
			JLabel rookPiece = new JLabel(whiteRookImage);
			JLabel queenPiece = new JLabel(whiteQueenImage);

			// Add pieces to the promotion window
			promotionWindow.add(knightPiece);
			promotionWindow.add(bishopPiece);
			promotionWindow.add(rookPiece);
			promotionWindow.add(queenPiece);
			
			
			knightPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(knightPiece, "KN");
	               promotionWindow.dispose();
	            }
	        });
			
			bishopPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(bishopPiece, "B");
	               promotionWindow.dispose();
	            }
	        });
			
			rookPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(rookPiece, "R");
	               promotionWindow.dispose();
	            }
	        });
			
			queenPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(queenPiece, "Q");
	               promotionWindow.dispose();
	            }
	        });
			
			
		}else if(blackIsPlayer && piece.getPieceColor().equals("B")) { //black is playing and the piece promoting is white
			
			JFrame promotionWindow = new JFrame("Promotion Window");
			promotionWindow.setBounds(400, 700, 320, 100);
			promotionWindow.setUndecorated(true);  // Set the window to be borderless
			promotionWindow.setLayout(new GridLayout(1, 4));  // Use GridLayout to evenly distribute components
			promotionWindow.setVisible(true);

			String basePath = System.getProperty("user.dir");

			//store imageicons
			ImageIcon blackRookImage = new ImageIcon(basePath + "/Images/blackRook.png");
			ImageIcon blackKnightImage = new ImageIcon(basePath + "/Images/blackKnight.png");
			ImageIcon blackBishopImage = new ImageIcon(basePath + "/Images/blackBishop.png");
			ImageIcon blackQueenImage = new ImageIcon(basePath + "/Images/blackQueen.png");

			// Scale images
			Image image = blackRookImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			blackRookImage = new ImageIcon(image);
			Image image2 = blackKnightImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			blackKnightImage = new ImageIcon(image2);
			Image image3 = blackBishopImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			blackBishopImage = new ImageIcon(image3);
			Image image4 = blackQueenImage.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
			blackQueenImage = new ImageIcon(image4);

			// Create JLabels for each piece
			JLabel knightPiece = new JLabel(blackKnightImage);
			JLabel bishopPiece = new JLabel(blackBishopImage);
			JLabel rookPiece = new JLabel(blackRookImage);
			JLabel queenPiece = new JLabel(blackQueenImage);

			// Add pieces to the promotion window
			promotionWindow.add(knightPiece);
			promotionWindow.add(bishopPiece);
			promotionWindow.add(rookPiece);
			promotionWindow.add(queenPiece);
			
			
			knightPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(knightPiece, "KN");
	               promotionWindow.dispose();
	            }
	        });
			
			bishopPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	piece.promotePiece(bishopPiece, "B");
	               promotionWindow.dispose();
	            }
	        });
			
			rookPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	               piece.promotePiece(rookPiece, "R");
	               promotionWindow.dispose();
	            }
	        });
			
			queenPiece.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	               piece.promotePiece(queenPiece, "Q");
	               promotionWindow.dispose();
	            }
	        });
			
		}else {//bot is playing
			if(whiteTurn) {
				String promoteToStr = botPlayingWhite.pickPromotionPiece(piece, chessBoard);
				JLabel newPieceLabel = createPieceJLabel(promoteToStr, piece.pieceColor);
				piece.promotePiece(newPieceLabel, promoteToStr);
			}else {
				String promoteToStr = botPlayingBlack.pickPromotionPiece(piece, chessBoard);
				JLabel newPieceLabel = createPieceJLabel(promoteToStr, piece.pieceColor);
				piece.promotePiece(newPieceLabel, promoteToStr);
			}
			
		}
		
		
		
	}
	
	/**
	 * Class only creates a JLabel for a promotion piece for now. If I use this for any other reason besides that I will need to update this class.
	 * @param piece The piece to be promoted to
	 * @param color The color of the piece
	 * @return A JLabel of the piece
	 */
	public JLabel createPieceJLabel(String piece, String color) {
		
		String basePath = System.getProperty("user.dir");
		ImageIcon pieceIcon;
		
		if(color.equals("W")) {
			
			if(piece.equals("R")) {
				pieceIcon = new ImageIcon(basePath + "/Images/whiteRook.png");
			}else if(piece.equals("N")) {
				pieceIcon = new ImageIcon(basePath + "/Images/whiteKnight.png");
			}else if(piece.equals("B")) {
				pieceIcon = new ImageIcon(basePath + "/Images/whiteBishop.png");
			}else {
				pieceIcon = new ImageIcon(basePath + "/Images/whiteQueen.png");
			}
			
			
		}else {
			if(piece.equals("R")) {
				pieceIcon = new ImageIcon(basePath + "/Images/blackRook.png");
			}else if(piece.equals("N")) {
				pieceIcon = new ImageIcon(basePath + "/Images/blackKnight.png");
			}else if(piece.equals("B")) {
				pieceIcon = new ImageIcon(basePath + "/Images/blackBishop.png");
			}else {
				pieceIcon = new ImageIcon(basePath + "/Images/blackQueen.png");
			}
		}
		
		Image image = pieceIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		pieceIcon = new ImageIcon(image);
		
		JLabel pieceLabel = new JLabel(pieceIcon);
		
		return pieceLabel;
		
	}
	
	/**
	 * Creates a temporary piece
	 * @param xy The x and y position for the piece to be created
	 * @return a new temporary piece
	 */
	public Piece createTempPiece(String xy) {
		int x = Integer.parseInt("" + xy.charAt(0));
		int y = Integer.parseInt("" + xy.charAt(1));
		
		Piece tempPiece = new Piece(null, x, y, "TDOT");
		
		return tempPiece;
	}
	
	/**
	 * Switches the turn for the game
	 */
	public void switchTurn() {
		if(whiteTurn) {
			whiteTurn = false;
		}else {
			whiteTurn = true;
		}
	}
}

