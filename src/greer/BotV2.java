package greer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BotV2 extends Bot{
	
	MoveLogic logic;
	Evaluation eval;
	BotV1 botV1;
	
	boolean isCheckmated = false;
	boolean isStalemated = false;
	
	public BotV2() {
		logic = new MoveLogic();
		eval = new Evaluation();
		botV1 = new BotV1();
	}

	/**
	 * Returns a hashmap of the piece and the move that the bot has selected
	 * @param board the board that the bot is selecting a move from
	 * @param pieces an array of the pieces on the board
	 * @param toPlay the color that the bot is playing for
	 */
	@Override
	public HashMap<Piece, String> selectMove(ChessBoard board, Piece[] pieces, String toPlay) {
		
		//need to clone pieces
		Piece[] piecesClone = pieces.clone();
		
		//loop through every piece of the same color as toPlay and find all possible moves
		int length = pieces.length;
		
		//store the pieces that are on the same color to use to search the hashmap
		ArrayList<Piece> sameColoredPieces = new ArrayList<Piece>();
		
		//the piece is the key which corresponds to a set of moves
		HashMap<Piece, String[]> possibleMoves = new HashMap<Piece, String[]>();
		
		for(int i = 0; i < length; i++) {
			if(piecesClone[i] != null && piecesClone[i].getPieceColor().equals(toPlay)) {
				
				String[] moves = logic.findMoveList(piecesClone[i], board, piecesClone);
				
				if(moves[0] == "" || moves.length == 0 || moves == null) {
					//do not add bc no possible moves
				}else {
					sameColoredPieces.add(piecesClone[i]);
					
					//map the pieces as the key as well as the moves for that piece
					possibleMoves.put(piecesClone[i], logic.findMoveList(piecesClone[i], board, piecesClone));
				}
				
				
				
			}
		}
		
		
		//logic for selecting a move. 
		
		int piecesLength = sameColoredPieces.size(); //the amount of same colored pieces to search through
		
		HashMap<Piece, String> selectedMove = new HashMap<Piece, String>();
		
		
		double highestEval;
		if(toPlay.equals("W")) {
			highestEval = -999;
		}else {
			highestEval = 999;
		}
		
		for(int i = 0; i < piecesLength; i++) {//loop through every piece and scan the moves for the best evaluation
			
			Piece pieceToMove = sameColoredPieces.get(i);
			
			String[] moves = possibleMoves.get(pieceToMove);
			
			for(int j = 0; j < moves.length; j++) {//for every move
				
				double evalForPos = 6969;
				
				ChessBoard cloneBoard = new ChessBoard(board);
				//update cloneBoard to move we are looking at
				
				int x = Character.getNumericValue(moves[j].charAt(0));
				int y = Character.getNumericValue(moves[j].charAt(1));
				
				Piece tempPiece1 = new Piece(null, x, y, "TDOT");
				
				cloneBoard.updateBitboard(pieceToMove, tempPiece1);
				cloneBoard.updateAttackedSquares();
				
				Piece[] checkingPiecesClone = piecesClone.clone();
				
				//change array so that it is accurate to the move we are checking
				int indexInArray = findIndex(pieceToMove, checkingPiecesClone);
				
				checkingPiecesClone[indexInArray] = null;
				checkingPiecesClone[y * 8 + x] = pieceToMove;
				
				if(toPlay.equals("W")) {
					
					//gets the response move for the move we are checking
					HashMap<Piece, String> moveToPlay = botV1.selectMove(cloneBoard, checkingPiecesClone, "B");
					
					if(moveToPlay == null) {
						//is mated or stalemated so cannot respond
						if(botV1.isCheckmated) {
							evalForPos = 500;
						}else {
							evalForPos = 0;
						}
						
					}else {
						//update the cloneBoard to reflect the bots response to our move we are checking
						Set<Piece> pieceSet = moveToPlay.keySet();
						List<Piece> pieceList = new ArrayList<>(pieceSet);
						Piece responseMovePiece = pieceList.get(0);
						String tempDotCoords = moveToPlay.get(responseMovePiece);
						Piece tempPiece = createTempPiece(tempDotCoords);
						
						cloneBoard.updateBitboard(responseMovePiece, tempPiece);
						cloneBoard.updateAttackedSquares();
					}
					
				}else {
					
					//gets the response move for the move we are checking
					HashMap<Piece, String> moveToPlay = botV1.selectMove(cloneBoard, checkingPiecesClone, "W");
					if(moveToPlay == null) {
						//is mated or stalematesd so cannot respond
						if(botV1.isCheckmated) {
							evalForPos = -500;
							selectedMove.put(sameColoredPieces.get(i), moves[j]);
						}else {
							evalForPos = 0;
						}
						
					}else {
						//update the cloneBoard to reflect the bots response to our move we are checking
						Set<Piece> pieceSet = moveToPlay.keySet();
						List<Piece> pieceList = new ArrayList<>(pieceSet);
						Piece responseMovePiece = pieceList.get(0);
						String tempDotCoords = moveToPlay.get(responseMovePiece);
						Piece tempPiece = createTempPiece(tempDotCoords);
						
						cloneBoard.updateBitboard(responseMovePiece, tempPiece);
						cloneBoard.updateAttackedSquares();
					}
					
					
				}
				
				if(evalForPos == 6969) {
					evalForPos = eval.getEvaluation(cloneBoard, pieces, toPlay);
				}
				
				
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
			
			try {
				moves = possibleMoves.get(sameColoredPieces.get(randomSelection));
			}catch(Exception e) {
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
			int randomNumber = (int) (Math.random() * pieceListLength);
			
			Piece randomPiece = pieceList.get(randomNumber);
		
			
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
		System.out.println(randNum);
		
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
	
	/**
	 * Creates a temp piece to use as a piece object
	 * @param xy the x and y corrdinate for the piece getting created
	 * @return the piece we created
	 */
	public Piece createTempPiece(String xy) {
		int x = Integer.parseInt("" + xy.charAt(0));
		int y = Integer.parseInt("" + xy.charAt(1));
		
		Piece tempPiece = new Piece(null, x, y, "TDOT");
		
		return tempPiece;
	}
	
	/**
	 * Finds the index of a piece in the array
	 * @param pieceToFind The piece to find
	 * @param pieceArray The array to find the piece in
	 * @return The index that the piece is in
	 */
	public int findIndex(Piece pieceToFind, Piece[] pieceArray) {
		for(int i = 0; i < pieceArray.length; i++) {
			if(pieceArray[i] == pieceToFind) {
				return i;
			}
		}
		
		return -1;
	}
}
