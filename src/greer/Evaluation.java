package greer;

import java.util.ArrayList;

public class Evaluation {
	
	/**
	 * Pawns: 1
	 * Bishops: 3
	 * Knights: 3
	 * Rooks: 5
	 * Queens: 9
	 * 
	 * Returns an evaluation for the given position
	 * @param board A board for the position to be evaluated
	 * @return a positive number for a favor of white, a negative number for in favor of black. The magnitude of the number depends on how likely they are to win
	 */
	public double getEvaluation(ChessBoard board, Piece[] pieces, String toPlay) {
		
		double whiteCount = 0;
		double blackCount = 0;
		
		for(int i = 0; i < 64; i++) {
			long squareToCheck = 1L << i;
			
			if((squareToCheck & board.generalPos) != 0) {//piece exists
				
				if((squareToCheck & board.whitePieces) != 0) {//white piece
					
					if((squareToCheck & board.whitePawns) != 0) {//pawn
						
						int value = 1;
						
						//check if pawn is on the eigth row. If so, return higher number
						//***************************************************************
						if((squareToCheck & board.eigthRank) != 0) {
							value *= 9;
							System.out.println("Pawn on the eighth rank");
						}else if((squareToCheck & board.seventhRank) != 0) {
							value *= 2;
						}
						
						
						
						if((board.whiteAttackedSquares & squareToCheck) == 0 && (board.blackAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							whiteCount += 0;
						}else if((board.whiteAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							whiteCount += (value * .98);
						}else {
							whiteCount += value;
						}
						
						
						
					}else if((squareToCheck & board.whiteBishops) != 0) {//bishop
						
						int value = 3;
						
						if((board.whiteAttackedSquares & squareToCheck) == 0 && (board.blackAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							whiteCount += 0;
						}else if((board.whiteAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							whiteCount += (value * .98);
						}else {
							whiteCount += value;
						}
						
					}else if((squareToCheck & board.whiteKnights) != 0) {//knight
						
						int value = 3;
						
						if((board.whiteAttackedSquares & squareToCheck) == 0 && (board.blackAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							whiteCount += 0;
						}else if((board.whiteAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							whiteCount += (value * .98);
						}else {
							whiteCount += value;
						}
						
					}else if((squareToCheck & board.whiteRooks) != 0) {//rook
						
						int value = 5;
						
						if((board.whiteAttackedSquares & squareToCheck) == 0 && (board.blackAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							whiteCount += 0;
						}else if((board.whiteAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							whiteCount += (value * .98);
						}else {
							whiteCount += value;
						}
						
					}else if((squareToCheck & board.whiteQueens) != 0) {//queen

						int value = 9;
						
						if((board.whiteAttackedSquares & squareToCheck) == 0 && (board.blackAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							whiteCount += 0;
						}else if((board.whiteAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							whiteCount += (value * .98);
						}else {
							whiteCount += value;
						}
						
					}
					
					
				}else {//black piece
					
					if((squareToCheck & board.blackPawns) != 0) {//pawn
						
						
						
						int value = 1;
						
						//check if pawn is on the first rank. If so, return higher number
						//***************************************************************
						if((squareToCheck & board.firstRank) != 0) {
							value *= 9;
							System.out.println("Pawn on the first rank");
						}else if((squareToCheck & board.secondRank) != 0) {
							value *= 2;
						}
						
						if((board.blackAttackedSquares & squareToCheck) == 0 && (board.whiteAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							blackCount += 0;
						}else if((board.blackAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							blackCount += (value * .98);
						}else {
							blackCount += value;
						}
						
					}else if((squareToCheck & board.blackBishops) != 0) {//bishop
						
						int value = 3;
						
						if((board.blackAttackedSquares & squareToCheck) == 0 && (board.whiteAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							blackCount += 0;
						}else if((board.blackAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							blackCount += (value * .98);
						}else {
							blackCount += value;
						}
						
					}else if((squareToCheck & board.blackKnights) != 0) {//knight
						
						int value = 3;
						
						if((board.blackAttackedSquares & squareToCheck) == 0 && (board.whiteAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							blackCount += 0;
						}else if((board.blackAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							blackCount += (value * .98);
						}else {
							blackCount += value;
						}
						
					}else if((squareToCheck & board.blackRooks) != 0) {//rook
						
						int value = 5;
						
						if((board.blackAttackedSquares & squareToCheck) == 0 && (board.whiteAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							blackCount += 0;
						}else if((board.blackAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							blackCount += (value * .98);
						}else {
							blackCount += value;
						}
						
					}else if((squareToCheck & board.blackQueens) != 0) {//queen
	
						int value = 9;
						
						if((board.blackAttackedSquares & squareToCheck) == 0 && (board.whiteAttackedSquares & squareToCheck) != 0) {
							//piece is hanging
							blackCount += 0;
						}else if((board.blackAttackedSquares & squareToCheck) == 0) {
							//piece is unprotected but isnt attacked
							blackCount += (value * .98);
						}else {
							blackCount += value;
						}
						
					}
					
				}
				
			}
		}
		
		return whiteCount - blackCount;
		
	}
	

	/**
	 * Helper function that generates an ArrayList of pieces that are the same color as the playing piece and have valid moves
	 * @param board
	 * @param pieces
	 * @param toPlay
	 * @return an arraylist of pieces that are the same color as toPlay and have valid moves
	 */
	public ArrayList<Piece> generateSameColoredPieces(ChessBoard board, Piece[] pieces, String toPlay, MoveLogic logic){
		
		if(pieces == null) {
			return null;
		}
		
		Piece[] piecesClone = pieces.clone();
		
		ArrayList<Piece> sameColoredPieces = new ArrayList<Piece>();
		int length = pieces.length;
		
		for(int i = 0; i < length; i++) {
			if(piecesClone[i] != null && piecesClone[i].getPieceColor().equals(toPlay)) {
				
				String[] moves = logic.findMoveList(piecesClone[i], board, piecesClone);
				
				if(moves[0] == "" || moves.length == 0 || moves == null) {
					//do not add bc no possible moves
				}else {
					sameColoredPieces.add(piecesClone[i]);
				}
			}
		}
		
		return sameColoredPieces;
	}
	
	/**
	 * Updates the chess board. Used by the bots to check future positions
	 * @param cloneBoard The board to update
	 * @param pieceToMove The piece to move on the board
	 * @param moveToX The x position to move to
	 * @param moveToY The y position to move to
	 * @return A chessboard with the updated position
	 */
	public ChessBoard updateChessBoard(ChessBoard cloneBoard, Piece pieceToMove, int moveToX, int moveToY) {
		
		Piece tempPiece = new Piece(null, moveToX, moveToY, "TDOT");
		
		cloneBoard.updateBitboard(pieceToMove, tempPiece);
		cloneBoard.updateAttackedSquares();
		
		return cloneBoard;
		
	}
	
	/**
	 * Updates the piece list
	 * @param originalPieceList THe original piece list to update
	 *  @param pieceToMove The piece to move on the board
	 * @param moveToX The x position to move to
	 * @param moveToY The y position to move to
	 * @return a piece list that has been updated
	 */
	public Piece[] updatePieceList(Piece[] originalPieceList, Piece pieceToMove, int moveToX, int moveToY) {
		
		//make a clone of the piece list
		Piece[] updatedPieceList = originalPieceList.clone();
		Piece pieceToMoveClone = new Piece(pieceToMove, moveToX, moveToY);
		
		//change array so that it is accurate to the move we are checking
		int indexInArray = findIndex(pieceToMove, updatedPieceList);
		
		//change values in array
		updatedPieceList[indexInArray] = null;
		updatedPieceList[moveToY * 8 + moveToX] = pieceToMoveClone;
		
		return updatedPieceList;
	}
	
	public int findIndex(Piece pieceToFind, Piece[] pieceArray) {
		for(int i = 0; i < pieceArray.length; i++) {
			if(pieceArray[i] == pieceToFind) {
				return i;
			}
		}
		
		return -1;
	}
}
