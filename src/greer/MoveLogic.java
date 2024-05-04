package greer;

import java.util.ArrayList;

public class MoveLogic {
	
	ChessBoard currentBoard;
	Piece piece;
	String clickedPieceCoords; //00 - 77
	
	Piece pieceList[];
	
	/**
	 * Finds the possible moves for a given piece
	 * @param piece The piece that we are finding a move for
	 * @param currentBoard The board that the piece resides on
	 * @param pieceList A list of the pieces on the board
	 * @return An array of possible moves
	 */
	public String[] findMoveList(Piece piece, ChessBoard currentBoard, Piece[] pieceList) {
		this.currentBoard = currentBoard;
		this.piece = piece;
		clickedPieceCoords = "" + piece.x + piece.y;
		this.pieceList = pieceList;
		
		String[] moves = null;
		
		
		if(piece.pieceType.equals("P")) {
			moves = findPawnMoves();
		}else if(piece.pieceType.equals("N")) {
			moves = findKnightMoves();
		}else if(piece.pieceType.equals("R")) {
			moves = findRookMoves();
		}else if(piece.pieceType.equals("B")) {
			moves = findBishopMoves();
		}else if(piece.pieceType.equals("Q")) {
			moves = findQueenMoves();
		}else {
			moves = findKingMoves();
		}
		
		return moves;
		
	}
	

	/**
	 * Finds the king moves for a given positon
	 * @return a list of moves that the king can make
	 */
	public String[] findKingMoves() {
		String moveSet = ""; //will create a list of target squares packed into a single string split by commas
		
		String pieceColor = piece.getPieceColor();
		boolean whitePiece = pieceColor.equals("W");
		long pieceBitboard = currentBoard.getGeneralPos();
		long enemyBitboard = whitePiece ? currentBoard.getBlackBoard() : currentBoard.getWhiteBoard();

		int pieceX = piece.getX();
		int pieceY = piece.getY();

		//check immediate squares for empty spaces, enemies or attacked squares. 
		//Pos X
		if(pieceX != 7) {
			int checkingX = pieceX + 1;
			int checkingY = pieceY;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Neg X
		if(pieceX != 0) {
			int checkingX = pieceX - 1;
			int checkingY = pieceY;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Pos Y
		if(pieceY != 7) {
			int checkingX = pieceX;
			int checkingY = pieceY + 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Neg Y
		if(pieceY != 0) {
			int checkingX = pieceX;
			int checkingY = pieceY - 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Pos Y Pos X
		if(pieceX != 7 && pieceY != 7) {
			int checkingX = pieceX + 1;
			int checkingY = pieceY + 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Pos Y Neg X
		if(pieceX != 0 && pieceY != 7) {
			int checkingX = pieceX - 1;
			int checkingY = pieceY + 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		
		//Neg Y Pos X
		if(pieceX != 7 && pieceY != 0) {
			int checkingX = pieceX + 1;
			int checkingY = pieceY - 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		//Neg Y Neg X
		if(pieceX != 0 && pieceY != 0) {
			int checkingX = pieceX - 1;
			int checkingY = pieceY - 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard.updateBitboard(piece, tempPiece);
			checkBoard.updateAttackedSquares();
			long enemyAttackedSquares = whitePiece ? checkBoard.getAttackedSquares("B") : checkBoard.getAttackedSquares("W");
			
			
			if((squareToCheck & pieceBitboard) == 0 && (enemyAttackedSquares & squareToCheck) == 0) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && (enemyAttackedSquares & squareToCheck) == 0) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
			}
		}
		
		//find castle moves
		if(piece.castleRights) {
			
			if(piece.getPieceColor().equals("W")) {
				
				long shortCastle = 1L << 7;
				long longCastle = 1L;
				
				long whiteRooks = currentBoard.getRooks("W");
				
				long generalPos = currentBoard.getGeneralPos();
				long attackingSquares = currentBoard.getAttackedSquares("B");
				
				Piece shortCastlePiece = pieceList[7];
				Piece longCastlePiece = pieceList[0];
				
				
				boolean emptyShort = (((1L << 5) & (generalPos)) == 0) && (((1L << 6) & (generalPos)) == 0);
				boolean legalShortCheckMove = ((((1L << 4) & (attackingSquares)) == 0) && ((1L << 5) & (attackingSquares)) == 0) && (((1L << 6) & (attackingSquares)) == 0);
				boolean canShortCastle = ((shortCastle & whiteRooks) != 0) && emptyShort && legalShortCheckMove && shortCastlePiece.castleRights;
				
				boolean emptyLong = (((1L << 1) & (generalPos)) == 0) && (((1L << 2) & (generalPos)) == 0) && (((1L << 3) & (generalPos)) == 0);
				boolean legalLongCheckMove = ((((1L << 4) & (attackingSquares)) == 0) && ((1L << 3) & (attackingSquares)) == 0) && (((1L << 2) & (attackingSquares)) == 0);
				boolean canLongCastle = ((longCastle & whiteRooks) != 0) && emptyLong && legalLongCheckMove && longCastlePiece.castleRights;
				
				
				if(canShortCastle) {
					moveSet += "" + 6 + 0 + ",";
				}
				if(canLongCastle) {
					moveSet += "" + 2 + 0 + ",";
				}
				
			}else {
				
				long shortCastle = 1L << 63;
				long longCastle = 1L << 56;
				
				long blackRooks = currentBoard.getRooks("B");
				
				long generalPos = currentBoard.getGeneralPos();
				long attackingSquares = currentBoard.getAttackedSquares("W");
				
				Piece shortCastlePiece = pieceList[63];
				Piece longCastlePiece = pieceList[56];
				
				
				boolean emptyShort = (((1L << 61) & (generalPos)) == 0) && (((1L << 62) & (generalPos)) == 0);
				boolean legalShortCheckMove = ((((1L << 60) & (attackingSquares)) == 0) && ((1L << 61) & (attackingSquares)) == 0) && (((1L << 62) & (attackingSquares)) == 0);
				boolean canShortCastle = ((shortCastle & blackRooks) != 0) && emptyShort && legalShortCheckMove && shortCastlePiece.castleRights;
				
				boolean emptyLong = (((1L << 57) & (generalPos)) == 0) && (((1L << 58) & (generalPos)) == 0) && (((1L << 59) & (generalPos)) == 0);
				boolean legalLongCheckMove = ((((1L << 60) & (attackingSquares)) == 0) && ((1L << 59) & (attackingSquares)) == 0) && (((1L << 58) & (attackingSquares)) == 0);
				boolean canLongCastle = ((longCastle & blackRooks) != 0) && emptyLong && legalLongCheckMove && longCastlePiece.castleRights;
				
				
				if(canShortCastle) {
					moveSet += "" + 6 + 7 + ",";
				}
				if(canLongCastle) {
					moveSet += "" + 2 + 7 + ",";
				}
				
			}
			
		}
		
		
		String[] movesArr = moveSet.split(",");
		return movesArr;
	}
	
	/**
	 * Finds the queen moves for a given positon
	 * @return a list of moves that the queen can make
	 */
	public String[] findQueenMoves() {
		
		String moveSet = ""; //will create a list of target squares packed into a single string split by commas
		
		boolean whitePiece = piece.getPieceColor().equals("W");
		long samePieceBitboard = currentBoard.getGeneralPos();
		long enemyBitboard = whitePiece ? currentBoard.getBlackBoard() : currentBoard.getWhiteBoard();
		long friendlyBitboard = whitePiece ? currentBoard.getWhiteBoard() : currentBoard.getBlackBoard();
		
		final int pieceX = piece.getX();
		final int pieceY = piece.getY();
		
		
		//check available moves
		//Positive X and Positive Y
		int checkingX = pieceX + 1;
		int checkingY = pieceY + 1;
		while(checkingX <= 7 && checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
			checkingY++;
		}
		//Positive X and Negative Y
		checkingX = pieceX + 1;
		checkingY = pieceY - 1;
		while(checkingX <= 7 && checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
			checkingY--;
		}
		
		//Negative X and Negative Y
		checkingX = pieceX - 1;
		checkingY = pieceY - 1;
		while(checkingX >= 0 && checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
			checkingY--;
		}
		//Negative X and Positive Y
		checkingX = pieceX - 1;
		checkingY = pieceY + 1;
		while(checkingX >= 0 && checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
			checkingY++;
		}
		//Positive X
		checkingX = pieceX + 1;
		checkingY = pieceY;
		while(checkingX <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + pieceY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + pieceY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
		}
		
		//Negative X
		checkingX = pieceX - 1;
		checkingY = pieceY;
		while(checkingX >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + pieceY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + pieceY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
		}
		
		//Positive Y
		checkingY = pieceY + 1;
		checkingX = pieceX;
		while(checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + pieceX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + pieceX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingY++;
		}
		
		//Negative Y
		checkingY = pieceY - 1;
		checkingX = pieceX;
		while(checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + pieceX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + pieceX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingY--;
		}
		
		//check if king is in check
		
		String[] movesArr = moveSet.split(",");
		return movesArr;
		
	}
	/**
	 * Finds the bishop moves for a given positon
	 * @return a list of moves that the bishop can make
	 */
	public String[] findBishopMoves() {
		
		String moveSet = ""; //will create a list of target squares packed into a single sring split by commas
		
		boolean whitePiece = piece.getPieceColor().equals("W");
		long pieceBitboard = currentBoard.getGeneralPos();
		long enemyBitboard = whitePiece ? currentBoard.getBlackBoard() : currentBoard.getWhiteBoard();
		long friendlyBitboard = whitePiece ? currentBoard.getWhiteBoard() : currentBoard.getBlackBoard();
		
		int pieceX = piece.getX();
		int pieceY = piece.getY();
		
		//check if piece is pinned 
		
		//check available moves
		
		//Positive X and Positive Y
		int checkingX = pieceX + 1;
		int checkingY = pieceY + 1;
		while(checkingX <= 7 && checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
		
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard)!= 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
			checkingY++;
		}
		//Positive X and Negative Y
		checkingX = pieceX + 1;
		checkingY = pieceY - 1;
		while(checkingX <= 7 && checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
			checkingY--;
		}
		
		//Negative X and Negative Y
		checkingX = pieceX - 1;
		checkingY = pieceY - 1;
		while(checkingX >= 0 && checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
			checkingY--;
		}
		//Negative X and Positive Y
		checkingX = pieceX - 1;
		checkingY = pieceY + 1;
		while(checkingX >= 0 && checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
			checkingY++;
		}
		
		//check if king is in check
		
		String[] movesArr = moveSet.split(",");
		return movesArr;
	}
	
	/**
	 * Finds the rook moves for a given positon
	 * @return a list of moves that the rook can make
	 */
	public String[] findRookMoves() {
		
		String moveSet = ""; //will create a list of target squares packed into a single sring split by commas
		
		boolean whitePiece = piece.getPieceColor().equals("W");
		long pieceBitboard = currentBoard.getGeneralPos();
		long enemyBitboard = whitePiece ? currentBoard.getBlackBoard() : currentBoard.getWhiteBoard();
		long friendlyBitboard = whitePiece ? currentBoard.getWhiteBoard() : currentBoard.getBlackBoard();
		
		int pieceX = piece.getX();
		int pieceY = piece.getY();
		
		//check if the piece is pinned
		
		//check what moves are possible
		//loop through each vertical and horizontal direction to search for pieces
		//Positive X
		int checkingX = pieceX + 1;
		int checkingY = pieceY;
		while(checkingX <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + pieceY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + pieceY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX++;
		}
		
		//Negative X
		checkingX = pieceX - 1;
		checkingY = pieceY;
		while(checkingX >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + checkingX + pieceY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + checkingX + pieceY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingX--;
		}
		
		//Positive Y
		checkingY = pieceY + 1;
		checkingX = pieceX;
		while(checkingY <= 7) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + pieceX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + pieceX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
					break;
			}
			
			checkingY++;
		}
		
		//Negative Y
		checkingY = pieceY - 1;
		checkingX = pieceX;
		while(checkingY >= 0) {
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			if((squareToCheck & pieceBitboard) == 0 && !isKingInCheck1) { //empty square
				moveSet += "" + pieceX + checkingY + ",";
			}else if((squareToCheck & enemyBitboard) != 0 && !isKingInCheck1) {//enemy sits there, allow move then break loop
				moveSet += "" + pieceX + checkingY + ",";
				break;
			}else if((squareToCheck & friendlyBitboard) != 0){//friendly piece sits there, break loop
				break;
			}
			
			checkingY--;
		}
		
		//check if king is in check
		
		//check castle logic
		
		String[] movesArr = moveSet.split(",");
		return movesArr;
	}
	
	/**
	 * Finds the knight moves for a given positon
	 * @return a list of moves that the knight can make
	 */
	public String[] findKnightMoves() {
		
		String moveSet = ""; //will create a list of target squares packed into a single sring split by commas
		
		boolean whitePiece = piece.getPieceColor().equals("W");
		long samePieceBitboard = whitePiece ? currentBoard.getWhiteBoard() : currentBoard.getBlackBoard();
		
		int pieceX = piece.getX();
		int pieceY = piece.getY();
		
		
		
		//checking piece bitboard and square to check to check for same pieces that it cannot move to
		//2 right 1 up
		if(pieceX + 2 <= 7 && pieceY + 1 <= 7) {
			int checkingX = pieceX + 2;
			int checkingY = pieceY + 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX + 2) + (pieceY + 1) + ",";
			}
			
			
		}
		//1 right 2 up
		if(pieceX + 1 <= 7 && pieceY + 2 <= 7) {
			int checkingX = pieceX + 1;
			int checkingY = pieceY + 2;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX + 1) + (pieceY + 2) + ",";
			}
			
			
		}
		//1 left 2 up
		if(pieceX - 1 >= 0 && pieceY + 2 <= 7) {
			int checkingX = pieceX - 1;
			int checkingY = pieceY + 2;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX - 1) + (pieceY + 2) + ",";
			}
			
		}
		//2 left 1 up
		if(pieceX - 2 >= 0 && pieceY + 1 <= 7) {
			int checkingX = pieceX - 2;
			int checkingY = pieceY + 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX - 2) + (pieceY + 1) + ",";
			}
			
			
		}
		//2 left 1 down
		if(pieceX - 2 >= 0 && pieceY - 1 >= 0) {
			int checkingX = pieceX - 2;
			int checkingY = pieceY - 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX - 2) + (pieceY - 1) + ",";
			}
			
		}
		//1 left 2 down
		if(pieceX - 1 >= 0 && pieceY - 2 >= 0) {
			int checkingX = pieceX - 1;
			int checkingY = pieceY - 2;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX - 1) + (pieceY - 2) + ",";
			}
			
		}
		//1 right 2 down
		if(pieceX + 1 <= 7 && pieceY - 2 >= 0) {
			int checkingX = pieceX + 1;
			int checkingY = pieceY - 2;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX + 1) + (pieceY - 2) + ",";
			}
			
		}
		//2 right 1 down
		if(pieceX + 2 <= 7 && pieceY - 1 >= 0) {
			int checkingX = pieceX + 2;
			int checkingY = pieceY - 1;
			long squareToCheck = 1L << (checkingX + (8 * checkingY));
			
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX, checkingY, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			boolean isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
			
			if((squareToCheck & samePieceBitboard) == 0 && !isKingInCheck1) {
				moveSet += "" + (pieceX + 2) + (pieceY - 1) + ",";
			}

		}
		
		
		String[] movesArr = moveSet.split(",");
		return movesArr;
	}
	
	/**
	 * Finds the pawn moves for a given positon
	 * @return a list of moves that the pawn can make
	 */
	public String[] findPawnMoves() {
		
		String moveSet = ""; //will create a list of target squares packed into a single string split by commas
		boolean whitePiece = piece.getPieceColor().equals("W");
		long pieceBitboard = currentBoard.getGeneralPos();
		int xCoord = Integer.parseInt("" + clickedPieceCoords.charAt(0));
		int yCoord = Integer.parseInt("" + clickedPieceCoords.charAt(1));
		
		int checkingX1, checkingX2, checkingY1, checkingY2;
		//check one and two spaces ahead for empty squares
		long pieceToCheck = 1L << (xCoord + (8*yCoord));
		long squareToCheck, squareToCheck2;
		if(whitePiece) {
			squareToCheck = pieceToCheck << (8);
			squareToCheck2 = pieceToCheck << (16);
			
			checkingY1 = yCoord + 1;
			checkingX1 = xCoord;
			
			checkingY2 = yCoord + 2;
			checkingX2 = xCoord;
		}else {
			squareToCheck = pieceToCheck >> (8);
			squareToCheck2 = pieceToCheck >> (16);
			
			checkingY1 = yCoord - 1;
			checkingX1 = xCoord;
			
			checkingY2 = yCoord - 2;
			checkingX2 = xCoord;
		}
		
		boolean isKingInCheck1 = false;
		boolean isKingInCheck2 = false;
        
		if(checkingY1 >= 0 && checkingY1 <= 7) {
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX1, checkingY1, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			
			isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
		}
		
		if(checkingY2 >= 0 && checkingY2 <= 7) {
			ChessBoard checkBoard2 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece2 = new Piece(null, checkingY1, checkingY2, "TDOT");
			checkBoard2.updateBitboard(piece, tempPiece2);
			checkBoard2.updateAttackedSquares();
			
			isKingInCheck2 = isKingInCheck(checkBoard2, piece.getPieceColor());
		}
		
        
        if((pieceBitboard & squareToCheck) != 0) {
        	//nothing
        } else {//square ahead is empty, check two squares ahead
        	if((pieceBitboard & squareToCheck2) == 0 && piece.canMoveTwo && ((whitePiece && yCoord < 7) || (!whitePiece && yCoord > 1))) {
        		if(whitePiece) {
        			
        			//check for pinned pieces
        			if(!isKingInCheck1) {
        				moveSet += "" + (xCoord) + (yCoord+1) + ",";
        			}
        			if(!isKingInCheck2) {
        				moveSet += "" + (xCoord) + (yCoord+2) + ",";
        			}
        			
        			
        		}else{
        			
        			if(!isKingInCheck1) {
        				moveSet += "" + (xCoord) + (yCoord-1) + ",";
        			}
        			if(!isKingInCheck2) {
        				moveSet += "" + (xCoord) + (yCoord-2) + ",";
        			}
        			
        			
        		}
        		
        	}else {
        		if(whitePiece && yCoord != 7) {
        			
        			if(!isKingInCheck1) {
        				moveSet += "" + (xCoord) + (yCoord+1) + ",";
        			}
        			
        		}else if(yCoord != 0){
        			
        			if(!isKingInCheck1) {
        				moveSet += "" + (xCoord) + (yCoord-1) + ",";
        			}
        			
        		}
        		
        	}
        }
        
		
        long oponentBitboard;
		//check diagonal attacks and set opponent bitboard to the opposite color
        if(whitePiece) {
        	squareToCheck = pieceToCheck << 9;
        	squareToCheck2 = pieceToCheck << 7;
        	oponentBitboard = currentBoard.getBlackBoard();
        	
        	checkingX1 = xCoord + 1;
        	checkingY1 = yCoord + 1;
        	
        	checkingX2 = xCoord - 1;
        	checkingY2 = yCoord + 1;
        }else {
        	squareToCheck = pieceToCheck >> 9;
        	squareToCheck2 = pieceToCheck >> 7;
        	oponentBitboard = currentBoard.getWhiteBoard();
        	
        	checkingX1 = xCoord - 1;
        	checkingY1 = yCoord - 1;
        	
        	checkingX2 = xCoord + 1;
        	checkingY2 = yCoord - 1;
        }
        
        isKingInCheck1 = false;
        isKingInCheck2 = false;
        
        if(checkingY1 >= 0 && checkingY1 <= 7 && checkingX1 >= 0 && checkingX1 <= 7) {
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX1, checkingY1, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			
			isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
		}
        
        if(checkingY2 >= 0 && checkingY2 <= 7 && checkingX2 >= 0 && checkingX2 <= 7) {
			ChessBoard checkBoard2 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece2 = new Piece(null, checkingX2, checkingY2, "TDOT");
			checkBoard2.updateBitboard(piece, tempPiece2);
			checkBoard2.updateAttackedSquares();
			
			isKingInCheck2 = isKingInCheck(checkBoard2, piece.getPieceColor());
		}
        
        if((oponentBitboard & squareToCheck) != 0) {
        	if(whitePiece && xCoord != 7) {
        		//attacking diagonal right
        		if(!isKingInCheck1) {
        			moveSet += "" + (xCoord + 1) + (yCoord + 1) + ",";
        		}
        		
        	}
        	if(!whitePiece && xCoord != 0) {
        		//attacking diagonal 
        		if(!isKingInCheck1) {
        			moveSet += "" + (xCoord - 1) + (yCoord - 1) + ",";
        		}
        		
        	}
        }
        if((oponentBitboard & squareToCheck2) != 0) {
        	if(whitePiece && xCoord != 0) {
        		//attacking diagonal left
        		if(!isKingInCheck2) {
        			moveSet += "" + (xCoord - 1) + (yCoord + 1) + ",";
        		}
        		
        	}
        	if(!whitePiece && xCoord != 7) {
        		//attacking diagonal right
        		if(!isKingInCheck2) {
        			moveSet += "" + (xCoord + 1) + (yCoord - 1) + ",";
        		}
        		
        	}
        }
        
        if(whitePiece) {
        	checkingX1 = xCoord - 1;
        	checkingY1 = yCoord + 1;
        	
        	checkingX2 = xCoord + 1;
        	checkingY2 = yCoord + 1;
        }else {
        	checkingX1 = xCoord - 1;
        	checkingY1 = yCoord - 1;
        	
        	checkingX2 = xCoord + 1;
        	checkingY2 = yCoord - 1;
        }
        
        isKingInCheck1 = false;
        isKingInCheck2 = false;
        if(checkingY1 >= 0 && checkingY1 <= 7 && checkingX1 >= 0 && checkingX1 <= 7) {
			ChessBoard checkBoard1 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece1 = new Piece(null, checkingX1, checkingY1, "TDOT");
			checkBoard1.updateBitboard(piece, tempPiece1);
			checkBoard1.updateAttackedSquares();
			
			isKingInCheck1 = isKingInCheck(checkBoard1, piece.getPieceColor());
		}
        
        if(checkingY2 >= 0 && checkingY2 <= 7 && checkingX2 >= 0 && checkingX2 <= 7) {
			ChessBoard checkBoard2 = new ChessBoard(currentBoard);
			//update checkboard to move we are thinking of
			Piece tempPiece2 = new Piece(null, checkingX2, checkingY2, "TDOT");
			checkBoard2.updateBitboard(piece, tempPiece2);
			checkBoard2.updateAttackedSquares();
			
			isKingInCheck2 = isKingInCheck(checkBoard2, piece.getPieceColor());
		}
        
		
		//check enPassant rules
        if(xCoord != 0 && xCoord != 7) {
        	pieceToCheck = 1L << (xCoord + (8*yCoord));
        	long leftCheck = pieceToCheck >> 1;
			long rightCheck = pieceToCheck << 1;
			
			if((leftCheck & oponentBitboard) != 0) {
				//get piece to left and check if it is enPassantable
				Piece leftPiece = pieceList[(xCoord-1) + (8 * yCoord)];
				if(leftPiece != null && leftPiece.enPassantable) {
					if(whitePiece) {
						if(!isKingInCheck1) {
							moveSet += "" + (xCoord - 1) + (yCoord + 1) + ",";
						}
						
					}else {
						if(!isKingInCheck1) {
							moveSet += "" + (xCoord - 1) + (yCoord - 1) + ",";
						}
						
					}
					
				}
			}
			if((rightCheck & oponentBitboard) != 0) {
				//get piece to right
				Piece rightPiece = pieceList[(xCoord+1) + (8 * yCoord)];
				if(rightPiece != null && rightPiece.enPassantable) {
					if(whitePiece) {
						if(!isKingInCheck2) {
							moveSet += "" + (xCoord + 1) + (yCoord + 1) + ",";
						}
						
					}else {
						if(!isKingInCheck2) {
							moveSet += "" + (xCoord + 1) + (yCoord - 1) + ",";
						}
						
					}
					
				}
			}
        }else if(xCoord == 0) {
        	pieceToCheck = 1L << (xCoord + (8*yCoord));
			long rightCheck = pieceToCheck << 1;
			
			if((rightCheck & oponentBitboard) != 0) {
				//get piece to right and check if it is enPassantable
				Piece rightPiece = pieceList[(xCoord+1) + (8 * yCoord)];
				if(rightPiece != null && rightPiece.enPassantable) {
					if(whitePiece) {
						if(!isKingInCheck2) {
							moveSet += "" + (xCoord + 1) + (yCoord + 1) + ",";
						}
						
					}else {
						if(!isKingInCheck2) {
							moveSet += "" + (xCoord + 1) + (yCoord - 1) + ",";
						}
						
					}
					
				}
			}
        }else if(xCoord == 7) {
        	pieceToCheck = 1L << (xCoord + (8*yCoord));
			long leftCheck = pieceToCheck >> 1;
			
			if((leftCheck & oponentBitboard) != 0) {
				//get piece to left and check if it is enPassantable
				Piece leftPiece = pieceList[(xCoord-1) + (8 * yCoord)];
				if(leftPiece != null && leftPiece.enPassantable) {
					if(whitePiece) {
						if(!isKingInCheck1) {
							moveSet += "" + (xCoord - 1) + (yCoord + 1) + ",";
						}
						
					}else {
						if(!isKingInCheck1) {
							moveSet += "" + (xCoord - 1) + (yCoord - 1) + ",";
						}
						
					}
					
				}
			}
        }
		
		//check king check, if in check, return only moves that result in the king not being in check
	
        String[] movesArr = moveSet.split(",");
		return movesArr;
	}
	
	/**
	 * A function that checks if the king is in check with a given ChessBoard
	 * @param board A chessboard object to check
	 * @return A boolean if the king is in check for the given ChessBoard
	 */
	public boolean isKingInCheck(ChessBoard board, String color) {
		
		boolean isWhite = color.equals("W");
		Piece kingToCheck;
		
		long attackedSquares;
		
		if(isWhite) {
			kingToCheck = board.getKing("W");
			attackedSquares = board.getAttackedSquares("B");
		}else {
			kingToCheck = board.getKing("B");
			attackedSquares = board.getAttackedSquares("W");
		}
		
		int x = kingToCheck.getX();
		int y = kingToCheck.getY();
		
		long squareToCheck = 1L << (x + (8*y));
		
		if((squareToCheck & attackedSquares) != 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a boolean if the current king is in checkmate
	 * @param board
	 * @param color
	 * @param sameColoredPieces
	 * @return
	 */
	public boolean isKingCheckmated(ChessBoard board, String color, ArrayList<Piece> sameColoredPieces) {
		
		if(!(isKingInCheck(board, color))) {
			return false;
		}else {
			
			if(sameColoredPieces.isEmpty()) {
				return true;
			}else {
				return false;
			}
			
		}
		
	}
	
	
}
