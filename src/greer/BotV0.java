package greer;

import java.util.ArrayList;
import java.util.HashMap;

public class BotV0 extends Bot{
	
	MoveLogic logic;
	
	public BotV0() {
		logic = new MoveLogic();
	}

	/**
	 * Returns a hashmap of the piece and the move that the bot has selected
	 * @param board the board that the bot is selecting a move from
	 * @param pieces an array of the pieces on the board
	 * @parm toPlay the color that the bot is playing for
	 */
	@Override
	public HashMap<Piece, String> selectMove(ChessBoard board, Piece[] pieces, String toPlay) {
		
		//loop through every piece of the same color as toPlay and find all possible moves
		int length = pieces.length;
		
		//store the pieces that are on the same color to use to search the hashmap
		ArrayList<Piece> sameColoredPieces = new ArrayList<Piece>();
		
		//the piece is the key which corresponds to a set of moves
		HashMap<Piece, String[]> possibleMoves = new HashMap<Piece, String[]>();
		
		for(int i = 0; i < length; i++) {
			if(pieces[i] != null && pieces[i].getPieceColor().equals(toPlay)) {
				
				String[] moves = logic.findMoveList(pieces[i], board, pieces);
				
				if(moves[0] == "" || moves.length == 0 || moves == null) {
					//do not add bc no possible moves
				}else {
					sameColoredPieces.add(pieces[i]);
					
					//map the pieces as the key as well as the moves for that piece
					possibleMoves.put(pieces[i], logic.findMoveList(pieces[i], board, pieces));
				}
				
				
				
			}
		}
		
		
		//logic for selecting a random move
		
		int piecesLength = sameColoredPieces.size();
		int randomSelection = (int) (Math.random() * piecesLength);
		
		String[] moves = possibleMoves.get(sameColoredPieces.get(randomSelection));
		
		
		int movesAmountLength = moves.length;
		int randomSelectedMove = (int) (Math.random() * movesAmountLength);
		
		HashMap<Piece, String> selectedMove = new HashMap<Piece, String>();
		selectedMove.put(sameColoredPieces.get(randomSelection), moves[randomSelectedMove]);
		
		return selectedMove;
		
		
		
	}
	
	/**
	 * The method that dictates what piece to promote to once a pawn can promote
	 * @param piece the pice to promote
	 * @param currentBoard the board that the piece is promoting on
	 */
	@Override
	public String pickPromotionPiece(Piece piece, ChessBoard currentBoard) {
		
		//select randomly
		int randNum = (int) (Math.random() * 4);
		
		if(randNum == 0) {
			return "R";
		}else if(randNum == 1) {
			return "N";
		}else if(randNum == 2) {
			return "B";
		}else {
			return "Q";
		}
		
	}
}
