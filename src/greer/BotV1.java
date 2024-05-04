package greer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BotV1 extends Bot{
	
	MoveLogic logic;
	Evaluation eval;
	
	public boolean isStalemated = false;
	public boolean isCheckmated = false;
	
	public BotV1() {
		logic = new MoveLogic();
		eval = new Evaluation();
	}

	/**
	 * Returns a hashmap of the piece and the move that the bot has selected
	 * @param board the board that the bot is selecting a move from
	 * @param pieces an array of the pieces on the board
	 * @param toPlay the color that the bot is playing for
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
		
		
		//logic for selecting a move. Single depth search
		
		int piecesLength = sameColoredPieces.size(); //the amount of same colored pieces to search through
		
		HashMap<Piece, String> selectedMove = new HashMap<Piece, String>();
		
		
		double highestEval;
		if(toPlay.equals("W")) {
			highestEval = -999;
		}else {
			highestEval = 999;
		}
		
		for(int i = 0; i < piecesLength; i++) {//loop through every piece and scan the moves for the best evaluation
			
			String[] moves = possibleMoves.get(sameColoredPieces.get(i));
			
			for(int j = 0; j < moves.length; j++) {
				
				ChessBoard cloneBoard = new ChessBoard(board);
				//update cloneBoard to move we are looking at
				
				int x = Character.getNumericValue(moves[j].charAt(0));
				int y = Character.getNumericValue(moves[j].charAt(1));
				
				
				Piece tempPiece1 = new Piece(null, x, y, "TDOT");
				
				cloneBoard.updateBitboard(sameColoredPieces.get(i), tempPiece1);
				cloneBoard.updateAttackedSquares();
				
				double evalForPos = eval.getEvaluation(cloneBoard, pieces, toPlay);
				
				if(toPlay.equals("W")) {//picks the highest eval from the list
					
					if(evalForPos > highestEval) {
						
						if(!selectedMove.isEmpty()){
							selectedMove.clear();
						}
						
						selectedMove.put(sameColoredPieces.get(i), moves[j]);
						highestEval = evalForPos;
					}else if(evalForPos == highestEval) {
						
						//logic for adding a move to selected moves
						selectedMove.put(sameColoredPieces.get(i), moves[j]);
						
					}
					
				}else {
					
					if(evalForPos < highestEval) {
						
						if(!selectedMove.isEmpty()){
							selectedMove.clear();
						}
						
						selectedMove.put(sameColoredPieces.get(i), moves[j]);
						highestEval = evalForPos;
					}else if(evalForPos == highestEval) {
						
						//logic for adding a move to selected moves
						selectedMove.put(sameColoredPieces.get(i), moves[j]);
						
					}
					
				}
				
			}		
			
		}
		
		//check if selected moves is greater than 1 item. If so, select a random move
		
		
		if(selectedMove.isEmpty()) { //pick a random move
			
			
			int randomSelection = (int) (Math.random() * piecesLength);
			
			
			String[] moves;
			
			try{
				moves = possibleMoves.get(sameColoredPieces.get(randomSelection));
			}catch(Exception e){
				if(board.isKingInCheck(toPlay)) {
					System.out.println("Possible moves is null due to checkmate");
					isCheckmated = true;
				}else {
					System.out.println("Possible moves is null due to stalemate");
					isStalemated = true;
				}
				
				return null;
			}
			
			
			int movesAmountLength = moves.length;
			int randomSelectedMove = (int) (Math.random() * movesAmountLength);
			
			selectedMove.put(sameColoredPieces.get(randomSelection), moves[randomSelectedMove]);
			
			return selectedMove;
		}else if(selectedMove.size() > 1) {//select random move from moves that have the same evaluation
			
			HashMap<Piece, String> tempSelectedMove = new HashMap<Piece, String>();
			
			Set<Piece> pieceSet = selectedMove.keySet();
			List<Piece> pieceList = new ArrayList<>(pieceSet);
			
			int pieceListLength = pieceList.size();
			Piece randomPiece = pieceList.get((int)( Math.random() * pieceListLength));
			
			tempSelectedMove.put(randomPiece, selectedMove.get(randomPiece));
			
			selectedMove = tempSelectedMove;
		}
		
		return selectedMove;
		
		
		
	}
	
	/**
	 * Picks a promotion piece. 
	 * TODO: check for possible checkmates or stalemates with selecting other pieces. If it is not beneficial to pick another piece, QUEEN
	 * @param piece Piece getting promoted
	 * @param currentBoard The current board state
	 * @return A string of the promotion piece that we have selected
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
