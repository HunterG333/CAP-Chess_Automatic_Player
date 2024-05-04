package greer;

public class GameLogic {
	
	Piece clickedPiece;
	ChessBoard currentBoard;
	MoveLogic moveLogic;
	
	public GameLogic() {
		moveLogic = new MoveLogic();
	}
	
	public void setClickedPiece(Piece piece) {
		this.clickedPiece = piece;
	}
	
	/**
	 * Gets the legal moves for the piece that has been clicked
	 * @param currentBoard The board to search on
	 * @param pieceList The list of pieces that are on the board
	 * @return An array of possible moves
	 */
	public String[] getClickedPieceLegalMoves(ChessBoard currentBoard, Piece[] pieceList) {
		this.currentBoard = currentBoard;
		String []moveList = moveLogic.findMoveList(clickedPiece, currentBoard, pieceList);
		return moveList;
	}
	
	/**
	 * Returns if the player has legal moves
	 * @param board The board to check
	 * @param toPlay The string of the color to play
	 * @param pieceList an array of pieces that are on the board
	 * @return a boolean if the piece can move
	 */
	public boolean canPlayerMove(ChessBoard board, String toPlay, Piece[] pieceList) {
		
		for(int i = 0; i < pieceList.length; i++) {
			
			if(pieceList[i] != null && pieceList[i].getPieceColor().equals(toPlay)) {
				
				String moves[] = moveLogic.findMoveList(pieceList[i], board, pieceList);
				if(moves[0] == "" || moves.length == 0 || moves == null) {
					//do not do anything bc no possible moves
				}else {
					return true;
				}
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * returns if the king is checkmates
	 * @param board The board to check
	 * @param toPlay The string of the color to play
	 * @param pieceList an array of pieces that are on the board
	 * @return a boolean of if the king has been mated or not
	 */
	public boolean isKingMated(ChessBoard board, String toPlay, Piece[] pieceList) {
		
		if(moveLogic.isKingInCheck(board, toPlay)) {
			
			return !canPlayerMove(board, toPlay, pieceList);
			
		}
		
		return false;
		
	}
	
	/**
	 * returns if the king is stalemated
	 * @param board The board to check
	 * @param toPlay The string of the color to play
	 * @param pieceList an array of pieces that are on the board
	 * @return a boolean of if the king has been stalemated or not
	 */
	public boolean isKingStaleMated(ChessBoard board, String toPlay, Piece[] pieceList) {
		
		return !canPlayerMove(board, toPlay, pieceList);
		
	}
	
	/**
	 * 
	 * @param board The chess board object
	 * @param toPlay A string of whose move it is. W for white and B for black
	 * @return a boolean of if the game is over
	 */
	public boolean isGameOver(ChessBoard board, String toPlay, Piece[] pieceList) {
		
		if(isKingStaleMated(board, toPlay, pieceList) || isKingMated(board, toPlay, pieceList)) {
			return true;
		}
		return false;
		
	}

}
