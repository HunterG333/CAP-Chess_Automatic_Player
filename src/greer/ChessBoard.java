package greer;

import java.util.ArrayList;

public class ChessBoard {
	
	//needs a bitboard for each piece-type and color 
	final long whitePawnsStart = 0b1111111100000000L;
	final long blackPawnsStart = 0b11111111000000000000000000000000000000000000000000000000L;
	final long whiteRookStart = 0b10000001L;
	final long blackRookStart = 0b1000000100000000000000000000000000000000000000000000000000000000L;
	final long whiteKnightStart = 0b01000010L;
	final long blackKnightStart = 0b0100001000000000000000000000000000000000000000000000000000000000L;
	final long whiteBishopStart = 0b00100100L;
	final long blackBishopStart = 0b0010010000000000000000000000000000000000000000000000000000000000L;
	final long whiteKingStart = 0b10000L;
	final long blackKingStart = 0b0001000000000000000000000000000000000000000000000000000000000000L;
	final long whiteQueenStart = 0b00001000L;
	final long blackQueenStart = 0b0000100000000000000000000000000000000000000000000000000000000000L;
	
	//helper static bitboards
	final long edgeSquares = 0b1000000110000001100000011000000110000001100000011000000110000001L;
	final long rightEdgeSquares = 0b1000000010000000100000001000000010000000100000001000000010000000L;
	final long leftEdgeSquares = 0b0000000100000001000000010000000100000001000000010000000100000001L;
	final long firstRank = 0b11111111L;
	final long secondRank = 0b1111111100000000L;
	final long seventhRank = 0b11111111000000000000000000000000000000000000000000000000L;
	final long eigthRank = 0b1111111100000000000000000000000000000000000000000000000000000000L;
	
	MoveLogic logicObject;
	
	long generalPos;
	long whitePieces;
	long blackPieces;
	long whitePawns;
	long blackPawns;
	long whiteRooks;
	long blackRooks;
	long whiteKnights;
	long blackKnights;
	long whiteBishops;
	long blackBishops;
	long whiteKing;
	long blackKing;
	long whiteQueens;
	long blackQueens;
	
	//helper bitboards
	long whiteAttackedSquares; //squares that white attacks
	long blackAttackedSquares; //squares that black attacks
	
	//creating piece king references
	Piece whiteKingPiece;
	Piece blackKingPiece;
	
	String startingPlay = "w";
	
	/**
	 * Default Constructor for creating a chess board
	 */
	public ChessBoard() {
		
		whitePawns = whitePawnsStart;
		blackPawns = blackPawnsStart;
		whiteRooks = whiteRookStart;
		blackRooks = blackRookStart;
		whiteKnights = whiteKnightStart;
		blackKnights = blackKnightStart;
		whiteBishops = whiteBishopStart;
		blackBishops = blackBishopStart;
		whiteKing = whiteKingStart;  
		blackKing = blackKingStart;  
		whiteQueens = whiteQueenStart; 
		blackQueens = blackQueenStart; 
		
		updatePosition();
		
	}
	
	/**
	 * Constructor for a ChessBoard with a FEN String
	 * @param FEN
	 */
	public ChessBoard(String FEN) {
		
		int currentIndex = 63;
		ArrayList<String> pieceStrings = new ArrayList<String>();
		
		String[] splitFen = FEN.split("/");
		
		
		for(int i = 0; i < splitFen.length - 1; i++) {
			String reversedString = reverseString(splitFen[i]);
			pieceStrings.add(reversedString);
			
		}
		
		String[] lastPart = splitFen[splitFen.length - 1].split(" ");
		
		//add lastPart[0] to pieceStrings
		pieceStrings.add(reverseString(lastPart[0]));
		
		startingPlay = lastPart[1];
		
	    
		for (int i = 1; i < lastPart.length; i++) {
	    	
	        //System.out.println(lastPart[i]);
	    }
	    
	    
	    //loop through pieceStrings part
	    for(int i = 0; i < pieceStrings.size(); i++) {
	    	String currentString = pieceStrings.get(i);
	    	currentIndex = breakPieceString(currentString, currentIndex);
	    }
	    
	    updatePosition();
	}
	
	/**
	 * Breaks the piece string and adds it to the bitboards. Used for FEN string recognition
	 * @param FEN the FEN that is being used for the custom position
	 * @param currentIndex the current index that the piece we are drawing is on
	 */
	private int breakPieceString(String FEN, int currentIndex) {
		
		for(int i = 0; i < FEN.length(); i++) {
			
			long square = 1L << currentIndex;
			
			char pieceChar = FEN.charAt(i);
			
			switch(pieceChar) {
			case 'r':
				blackRooks += square;
				currentIndex--;
				break;
			case 'R':
				whiteRooks += square;
				currentIndex--;
				break;
			case 'n':
				blackKnights += square;
				currentIndex--;
				break;
			case 'N':
				whiteKnights += square;
				currentIndex--;
				break;
			case 'b':
				blackBishops += square;
				currentIndex--;
				break;
			case 'B':
				whiteBishops += square;
				currentIndex--;
				break;
			case 'q':
				blackQueens += square;
				currentIndex--;
				break;
			case 'Q':
				whiteQueens += square;
				currentIndex--;
				break;
			case 'k':
				blackKing += square;
				currentIndex--;
				break;
			case 'K':
				whiteKing += square;
				currentIndex--;
				break;
			case 'p':
				blackPawns += square;
				currentIndex--;
				break;
			case 'P':
				whitePawns += square;
				currentIndex--;
				break;
			default:
				int num = Integer.parseInt("" + pieceChar);
				currentIndex -= num;
				
			}
			
		}
		
		
		return currentIndex;
		
	}
	
	/**
	 * Reverses the inputed string
	 * @param str The string to be reversed
	 * @return A reversed string
	 */
	private String reverseString(String str) {
	    return new StringBuilder(str).reverse().toString();
	}
	
	/**
	 * Copy Constructor for checking future positions
	 */
	public ChessBoard(ChessBoard other) {
		generalPos = other.generalPos;
		whitePieces = other.whitePieces;
		blackPieces = other.blackPieces;
		whitePawns = other.whitePawns;
		blackPawns = other.blackPawns;
		whiteRooks = other.whiteRooks;
		blackRooks = other.blackRooks;
		whiteKnights = other.whiteKnights;
		blackKnights = other.blackKnights;
		whiteBishops = other.whiteBishops;
		blackBishops = other.blackBishops;
		whiteKing = other.whiteKing;
		blackKing = other.blackKing;
		whiteQueens = other.whiteQueens;
		blackQueens = other.blackQueens;
		
		whiteAttackedSquares = other.whiteAttackedSquares;
		blackAttackedSquares = other.blackAttackedSquares;
		
		whiteKingPiece = other.whiteKingPiece;
		blackKingPiece = other.blackKingPiece;
	}
	
	/**
	 * Updated the logic object for the class
	 * @param logicObject The logic object to update to
	 */
	public void setLogicObject(MoveLogic logicObject) {
		this.logicObject = logicObject;
	}
	
	/**
	 * Updates the position of the helper bitboards based on the piece bitboards
	 */
	public void updatePosition() {	
		whitePieces = (whiteQueens | whiteKing | whiteBishops | whiteKnights| whiteRooks| whitePawns);
		blackPieces = (blackQueens | blackKing | blackBishops | blackKnights | blackRooks | blackPawns);
		generalPos = (whitePieces | blackPieces);
	}
	
	/**
	 * gets the position in an array
	 * @return an array of the piece bitboards
	 */
	public long[] getCurrentPosition() {
		//currentPosition[0] contains information regarding the general board
		long[] currentPosition = {generalPos, whitePieces, blackPieces, whitePawns, blackPawns, whiteRooks, blackRooks, whiteKnights, blackKnights, whiteBishops, blackBishops, whiteKing, blackKing, whiteQueens, blackQueens};
		return currentPosition;
	}
	
	/**
	 * Sets the king piece
	 * @param color The color of the king
	 * @param piece The king piece reference
	 */
	public void setKing(String color, Piece piece) {
		if(color.equals("W")) {
			whiteKingPiece = piece;
		}else {
			blackKingPiece = piece;
		}
	}
	
	/**
	 * Gets the king piece
	 * @param color The color of the king ot get
	 * @return The king of the specified color
	 */
	public Piece getKing(String color) {
		if(color.equals("W")) {
			return whiteKingPiece;
		}else {
			return blackKingPiece;
		}
	}
	
	/**
	 * Gets a bitboard of the attacked squares for the color provided
	 * @param color The color of the attacked squares to get
	 * @return The attacked squares bitboard for the specified color
	 */
	public long getAttackedSquares(String color) {
		if(color.equals("W")) {
			return whiteAttackedSquares;
		}else {
			return blackAttackedSquares;
		}
	}
	
	/**
	 * Gets a bitboard of the rooks in a posiition
	 * @param color The color of the rooks bitboard to get
	 * @return The rooks bitboard that is needed
	 */
	public long getRooks(String color) {
		if(color.equals("W")) {
			return whiteRooks;
		}else {
			return blackRooks;
		}
	}
	
	/**
	 * Another method for getting the general position
	 * @return An array of the bitboards
	 */
	public long[] getPosition() {
		
		return getCurrentPosition();
	}
	
	public long getGeneralPos() {
		return generalPos;
	}
	
	public long getBlackBoard() {
		return blackPieces;
	}
	
	public long getWhiteBoard() {
		return whitePieces;
	}
	
	/**
	 * 
	 * Function that updates the bitboard positions. Usually for piece movement
	 * @param pieceToMove The piece that is getting moved
	 * @param dotClickedOn the dot that was clicked on so we move the piece to it
	 */
	public void updateBitboard(Piece pieceToMove, Piece dotClickedOn) {
		
		long pieceToMoveBitboard = pieceToMove.getPostionBitboard();
		long dotToMoveToBitboard = dotClickedOn.getPostionBitboard();
		
		if(pieceToMove.getPieceColor().equals("W")) {
			
			switch(pieceToMove.getPieceType()) {
			case("P"):
				whitePawns ^= pieceToMoveBitboard;
				whitePawns |= dotToMoveToBitboard;
				break;
			case("R"):
				whiteRooks ^= pieceToMoveBitboard;
				whiteRooks |= dotToMoveToBitboard;
				break;
			case("B"):
				whiteBishops ^= pieceToMoveBitboard;
				whiteBishops |= dotToMoveToBitboard;
				break;
			case("N"):
				whiteKnights ^= pieceToMoveBitboard;
				whiteKnights |= dotToMoveToBitboard;
				break;
			case("Q"):
				whiteQueens ^= pieceToMoveBitboard;
				whiteQueens |= dotToMoveToBitboard;
				break;
			case("K"):
				whiteKing ^= pieceToMoveBitboard;
				whiteKing |= dotToMoveToBitboard;
				break;
			}
			
			//check square we are moving to for an enemy piece
			String squareStatus = checkSquare(dotToMoveToBitboard, pieceToMove.getPieceColor());
			if(squareStatus != null) {
				switch(squareStatus) {
				case("P"):
					blackPawns ^= dotToMoveToBitboard;
					break;
				case("R"):
					blackRooks ^= dotToMoveToBitboard;
					break;
				case("B"):
					blackBishops ^= dotToMoveToBitboard;
					break;
				case("N"):
					blackKnights ^= dotToMoveToBitboard;
					break;
				case("Q"):
					blackQueens ^= dotToMoveToBitboard;
					break;
				}
			}
			
			
		}else {
			
			
			
			switch(pieceToMove.getPieceType()) {
			case("P"):
				blackPawns ^= pieceToMoveBitboard;
				blackPawns |= dotToMoveToBitboard;
				break;
			case("R"):
				blackRooks ^= pieceToMoveBitboard;
				blackRooks |= dotToMoveToBitboard;
				break;
			case("B"):
				blackBishops ^= pieceToMoveBitboard;
				blackBishops |= dotToMoveToBitboard;
				break;
			case("N"):
				blackKnights ^= pieceToMoveBitboard;
				blackKnights |= dotToMoveToBitboard;
				break;
			case("Q"):
				blackQueens ^= pieceToMoveBitboard;
				blackQueens |= dotToMoveToBitboard;
				break;
			case("K"):
				blackKing ^= pieceToMoveBitboard;
				blackKing |= dotToMoveToBitboard;
				break;
			}
			
			//check square we are moving to for an enemy piece
			String squareStatus = checkSquare(dotToMoveToBitboard, pieceToMove.getPieceColor());
			if(squareStatus != null) {
				switch(squareStatus) {
				case("P"):
					whitePawns ^= dotToMoveToBitboard;
					break;
				case("R"):
					whiteRooks ^= dotToMoveToBitboard;
					break;
				case("B"):
					whiteBishops ^= dotToMoveToBitboard;
					break;
				case("N"):
					whiteKnights ^= dotToMoveToBitboard;
					break;
				case("Q"):
					whiteQueens ^= dotToMoveToBitboard;
					break;
				}
			}
			
		}
			
		
		updatePosition();
	}
	
	/**
	 * Returns if the king is in check
	 * @param toPlay The color of which king to check if it is in check
	 * @return true if in check, false if not in check
	 */
	public boolean isKingInCheck(String toPlay) {
		
		
		long kingBitboard;
		long opponentAttacking;
		
		if(toPlay.equals("W")) {
			kingBitboard = whiteKing;
			opponentAttacking = blackAttackedSquares;
		}else {
			kingBitboard = blackKing;
			opponentAttacking = whiteAttackedSquares;
		}
		
		return ((kingBitboard & opponentAttacking) != 0) ? true:false;
		
		
	}
	
	/**
	 * Checks the given square for an occuping piece
	 * @param squareToCheck The square to check
	 * @param pieceColor A string of the piece color. Cannot move on top of your pieces so redundant to check those bitboards
	 * @return A string of what type of piece is occuping the square. Null if none.
	 */
	public String checkSquare(long squareToCheck, String pieceColor) {
		if((squareToCheck & generalPos) == 0) {
			return null;
		}else {
			//return piece that occupies square
			if(pieceColor.equals("W")) {
				
				//check black pawns
				if((squareToCheck & blackPawns) != 0) {
					return "P";
				}else if((squareToCheck & blackRooks) != 0) {
					return "R";
				}else if((squareToCheck & blackKnights) != 0) {
					return "N";
				}else if((squareToCheck & blackBishops) != 0) {
					return "B";
				}else if((squareToCheck & blackQueens) != 0) {
					return "Q";
				}else {
					return "K";
				}
				
			}else {
				if((squareToCheck & whitePawns) != 0) {
					return "P";
				}else if((squareToCheck & whiteRooks) != 0) {
					return "R";
				}else if((squareToCheck & whiteKnights) != 0) {
					return "N";
				}else if((squareToCheck & whiteBishops) != 0) {
					return "B";
				}else if((squareToCheck & whiteQueens) != 0) {
					return "Q";
				}else {
					return "K";
				}
			}
		}
	}

	/**
	 * Function to create the bitboards for the attacked squares
	 */
	public void updateAttackedSquares() {
		
		//white pieces first
		whiteAttackedSquares = 0;
		//white pawns attack
		long whitePawnsOnLeftEdge = whitePawns & leftEdgeSquares;
		long whitePawnsOnRightEdge = whitePawns & rightEdgeSquares;
		
		long whitePawnsNotOnEdge = whitePawns - whitePawnsOnLeftEdge - whitePawnsOnRightEdge;
		
		//black pieces second
		blackAttackedSquares = 0;
		long blackPawnsOnLeftEdge = blackPawns & leftEdgeSquares;
		long blackPawnsOnRightEdge = blackPawns & rightEdgeSquares;
		
		long blackPawnsNotOnEdge = blackPawns - blackPawnsOnLeftEdge - blackPawnsOnRightEdge;
		
		//for every bit that is a 1 on the NotOnEdge bitboard, set the attackedbitboard 2^7 and 2^9 to 1
		//goes to 56 because white pawns shouldnt exist on the top row
		for(int i = 0; i < 64; i++) {
			int x = i % 8;
			int y = i / 8;
			
			long checkingSquare = 1L << (x + 8 * y);
			
			//white pawns
			if(i < 56 && (checkingSquare & whitePawnsNotOnEdge) != 0) {
				//white pawn not on edge
				whiteAttackedSquares = (whiteAttackedSquares | checkingSquare << 7 | checkingSquare << 9);
				
				
			}else if (i < 56 && (checkingSquare & whitePawnsOnRightEdge) != 0) {
				
				whiteAttackedSquares = (whiteAttackedSquares | checkingSquare << 7);
				
				
			}else if (i < 56 && (checkingSquare & whitePawnsOnLeftEdge) != 0) {
				
				whiteAttackedSquares = (whiteAttackedSquares | checkingSquare << 9);
				
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//white sliding pieces
			//rook & some queen
			if((checkingSquare & whiteRooks) != 0 || (checkingSquare & whiteQueens) != 0) {
				//check x directions
				int checkingX = x + 1;
				while(checkingX <= 7) {
					checkingSquare = 1L << (checkingX + 8 * y);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
				}
				
				checkingX = x - 1;
				while(checkingX >= 0) {
					checkingSquare = 1L << (checkingX + 8 * y);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
				}
				
				//check y directions
				
				checkingX = x;
				int checkingY = y + 1;
				while(checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingY++;
				}
				
				checkingY = y - 1;
				while(checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingY--;
				}
				
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//bishop and the rest of queen
			if((checkingSquare & whiteBishops) != 0 || (checkingSquare & whiteQueens) != 0) {
				//check pos x,y directions
				int checkingX = x + 1;
				int checkingY = y + 1;
				while(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
					checkingY++;
				}
				
				//check pos x neg y directions
				checkingX = x + 1;
				checkingY = y - 1;
				while(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
					checkingY--;
				}
				
				//check neg x pos y directions
				checkingX = x - 1;
				checkingY = y + 1;
				while(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
					checkingY++;
				}
				
				//check neg x neg y directions
				checkingX = x - 1;
				checkingY = y - 1;
				while(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
					}else {
						whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
					checkingY--;
				}
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			
			//knight
			if((checkingSquare & whiteKnights) != 0) {
				//up 2 right 1
				int checkingX = x + 1;
				int checkingY = y + 2;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//up 1 right 2
				checkingX = x + 2;
				checkingY = y + 1;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 1 right 2
				checkingX = x + 2;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 2 right 1
				checkingX = x + 1;
				checkingY = y - 2;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 2 left 1
				checkingX = x - 1;
				checkingY = y - 2;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 1 left 2
				checkingX = x - 2;
				checkingY = y - 1;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//up 1 left 2
				checkingX = x - 2;
				checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//up 2 left 1
				checkingX = x - 1;
				checkingY = y + 2;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//king
			if((checkingSquare & whiteKing) != 0) {
				//up 1 left 1
				int checkingX = x - 1;
				int checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//up 1
				checkingX = x;
				checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//up 1 right 1
				checkingX = x + 1;
				checkingY = y + 1;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//right 1
				checkingX = x + 1;
				checkingY = y;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 1 right 1
				checkingX = x + 1;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 1
				checkingX = x;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//down 1 left 1
				checkingX = x - 1;
				checkingY = y - 1;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
				//left 1
				checkingX = x - 1;
				checkingY = y;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					whiteAttackedSquares = (whiteAttackedSquares | checkingSquare);
				}
				
			}
			
			
			//***********************************************************************
			
			checkingSquare = 1L << (x + 8 * y);
			
			//black pieces
			if(i >= 8 && (checkingSquare & blackPawnsNotOnEdge) != 0) {
				//white pawn not on edge
				blackAttackedSquares = (blackAttackedSquares | checkingSquare >> 7 | checkingSquare >> 9);
				
				
			}else if (i >= 8 && (checkingSquare & blackPawnsOnRightEdge) != 0) {
				
				blackAttackedSquares = (blackAttackedSquares | checkingSquare >> 9);
				
				
			}else if (i >= 8 && (checkingSquare & blackPawnsOnLeftEdge) != 0) {
				
				blackAttackedSquares = (blackAttackedSquares | checkingSquare >> 7);
				
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//white sliding pieces
			//rook & some queen
			if((checkingSquare & blackRooks) != 0 || (checkingSquare & blackQueens) != 0) {
				
					
				
				//check x directions
				int checkingX = x + 1;
				while(checkingX <= 7) {
					checkingSquare = 1L << (checkingX + 8 * y);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
				}
				
				checkingX = x - 1;
				while(checkingX >= 0) {
					checkingSquare = 1L << (checkingX + 8 * y);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
				}
				
				//check y directions
				
				checkingX = x;
				int checkingY = y + 1;
				while(checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingY++;
				}
				
				checkingY = y - 1;
				while(checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingY--;
				}
				
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//bishop and the rest of queen
			if((checkingSquare & blackBishops) != 0 || (checkingSquare & blackQueens) != 0) {
				//check pos x,y directions
				int checkingX = x + 1;
				int checkingY = y + 1;
				while(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
					checkingY++;
				}
				
				//check pos x neg y directions
				checkingX = x + 1;
				checkingY = y - 1;
				while(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX++;
					checkingY--;
				}
				
				//check neg x pos y directions
				checkingX = x - 1;
				checkingY = y + 1;
				while(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
					checkingY++;
				}
				
				//check neg x neg y directions
				checkingX = x - 1;
				checkingY = y - 1;
				while(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					if((checkingSquare & generalPos) == 0) {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
					}else {
						blackAttackedSquares = (blackAttackedSquares | checkingSquare);
						break;
					}
					
					checkingX--;
					checkingY--;
				}
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			
			//knight
			if((checkingSquare & blackKnights) != 0) {
				//up 2 right 1
				int checkingX = x + 1;
				int checkingY = y + 2;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//up 1 right 2
				checkingX = x + 2;
				checkingY = y + 1;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 1 right 2
				checkingX = x + 2;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 2 right 1
				checkingX = x + 1;
				checkingY = y - 2;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 2 left 1
				checkingX = x - 1;
				checkingY = y - 2;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 1 left 2
				checkingX = x - 2;
				checkingY = y - 1;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//up 1 left 2
				checkingX = x - 2;
				checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//up 2 left 1
				checkingX = x - 1;
				checkingY = y + 2;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
			}
			
			checkingSquare = 1L << (x + 8 * y);
			
			//king
			if((checkingSquare & blackKing) != 0) {
				//up 1 left 1
				int checkingX = x - 1;
				int checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//up 1
				checkingX = x;
				checkingY = y + 1;
				if(checkingX >= 0 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//up 1 right 1
				checkingX = x + 1;
				checkingY = y + 1;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//right 1
				checkingX = x + 1;
				checkingY = y;
				if(checkingX <= 7 && checkingY <= 7) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 1 right 1
				checkingX = x + 1;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 1
				checkingX = x;
				checkingY = y - 1;
				if(checkingX <= 7 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//down 1 left 1
				checkingX = x - 1;
				checkingY = y - 1;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
				//left 1
				checkingX = x - 1;
				checkingY = y;
				if(checkingX >= 0 && checkingY >= 0) {
					checkingSquare = 1L << (checkingX + 8 * checkingY);
					blackAttackedSquares = (blackAttackedSquares | checkingSquare);
				}
				
			}
			
		}
		
	}
	
	/**
	 * Clears the specified square from the bitboards
	 * @param square The square to clear
	 */
	public void clearSquare(long square) {
		if((generalPos & square) != 0) {
			if((whitePieces & square) != 0) {
				
				if((whitePawns & square) != 0) {
					whitePawns ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((whiteRooks & square) != 0) {
					whiteRooks ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((whiteKnights & square) != 0) {
					whiteKnights ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((whiteBishops & square) != 0) {
					whiteBishops ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((whiteQueens & square) != 0) {
					whiteQueens ^= square;
					updatePosition();
					updateAttackedSquares();
				}
				
			}else if ((blackPieces & square) != 0) {
				
				if((blackPawns & square) != 0) {
					blackPawns ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((blackRooks & square) != 0) {
					blackRooks ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((blackKnights & square) != 0) {
					whiteKnights ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((blackBishops & square) != 0) {
					whiteBishops ^= square;
					updatePosition();
					updateAttackedSquares();
				}else if((blackQueens & square) != 0) {
					whiteQueens ^= square;
					updatePosition();
					updateAttackedSquares();
				}
				
			}
		}
	}
	
	/**
	 * Prints out the status of the position
	 */
	public String toString() {
		System.out.println("*****************START BOARD PRINTOUT******************");
		printReadableBoard("General Position", generalPos, 1);
		printReadableBoard("White", whitePieces, 1);
		printReadableBoard("Black", blackPieces, 1);
		System.out.println("*****************END BOARD PRINTOUT******************");
		
		return "";
	}
	
	/**
	 * Prints out a readable message for the board
	 * @param precedingMessage The message to print out before the boards
	 * @param bitboard The bitboard to print out
	 * @param numSpacesAfter The number of lines to print out after the bitboards
	 */
	public void printReadableBoard(String precedingMessage, long bitboard, int numSpacesAfter) {
		System.out.print(precedingMessage + ": ");
		for(int i = 0; i < 64; i++) {
			int x = i % 8;
			int y = 7 - (i / 8);
			
			long checkingSquare = 1L << (x + 8 * y);
			
			
			
			if(i % 8 == 0) {
				System.out.println();
			}
			
			if((bitboard & checkingSquare) == 0) {
				System.out.print(0);
			}else {
				System.out.print(1);
			}
		}
		
		for(int i = 0; i <= numSpacesAfter; i++) {
			System.out.println();
		}
		
	}

}
