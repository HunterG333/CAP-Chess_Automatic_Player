package greer;

import java.util.ArrayList;
import java.util.HashMap;

public class BotV3 extends Bot{
	
	MoveLogic logic;
	Evaluation eval;
	
	public BotV3() {
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
		
		//store the pieces that are on the same color and have possible moves
		ArrayList<Piece> sameColoredPieces = eval.generateSameColoredPieces(board, pieces, toPlay, logic);
		
		//ArrayList of the trees
		ArrayList<K_ary_Tree> treeList = new ArrayList<K_ary_Tree>();
		
		//lopp through every piece and move to do work on that move
		for(int i = 0; i < sameColoredPieces.size(); i++) {
			
			Piece pieceToMove = sameColoredPieces.get(i);
			
			String[] moves = logic.findMoveList(pieceToMove, board, pieces);
			
			for(int j = 0; j < moves.length; j++) {//for every move create a tree
				
				int moveToX = Integer.parseInt("" + moves[j].charAt(0));
				int moveToY = Integer.parseInt("" + moves[j].charAt(1));
				
				//updated board for the move we are checking
				ChessBoard updatedBoard = eval.updateChessBoard(new ChessBoard(board), pieceToMove, moveToX, moveToY);
				
				//updated pieces array for the move we are checking
				Piece[] updatedPieceList = eval.updatePieceList(pieces, pieceToMove, moveToX, moveToY);
				
				
				//create the tree object
				//toPlay represents the color for the moves we are checking to play
				try {
					
					K_ary_Tree newTree = new K_ary_Tree(updatedBoard, updatedPieceList, eval, logic, toPlay, pieceToMove, moves[j]);
					treeList.add(newTree);
					
				}catch(Exception e) {
					System.out.println("Tree was unable to be created");
				}
				
			}
		}
		
		//search and update tree until time has run out
		//the following will be inside a time structure loop
		//****************
		
		//pieces[] array not getting updated properly?
		//debug and track the moves until the error occurs
		
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		
		for(int depth = 1; depth <= 1; depth++) {
			
			for(int i = 0; i < treeList.size(); i++) {
				
				K_ary_Tree treeBeingSearched = treeList.get(i);
				
				Thread searchThread = new Thread(treeBeingSearched);
				
				threadList.add(searchThread);
				
				searchThread.start();
				
				//treeBeingSearched.newSearch();
				
				
			}
			
		}
		
		
		//continue loop until all threads are closed
		for(int i = 0; i < threadList.size(); i++) {
				
			Thread threadToLookAt = threadList.get(i);
			if(threadToLookAt.isAlive()) {
				i = 0;
			}
			
		}
		System.out.println("Threads completed");
			
		
		//****************
		
		
		//pick the best move
		double bestEval;
		K_ary_Tree bestTree = null;
		if(toPlay.equals("W")) {
			bestEval = -999;
		}else {
			bestEval = 999;
		}
		for(int i = 0; i < treeList.size(); i++) {
			
			K_ary_Tree treeBeingSearched = treeList.get(i);
			double treeBestEval = treeBeingSearched.getEvaluation();
			
			System.out.println("Tree being searched has an evaluation of: " + treeBestEval);
			
			//whatever eval is the best for our color will get picked
			if(toPlay.equals("W") && bestEval < treeBestEval) {
				bestEval = treeBestEval;
				bestTree = treeBeingSearched;
			}else if(toPlay.equals("B") && bestEval > treeBestEval) {
				bestEval = treeBestEval;
				bestTree = treeBeingSearched;
			}
		}		
		
		if(bestTree == null) {
			bestTree = treeList.get(0);
		}
		
		System.out.println("Selecting a move with an eval of " + bestTree.getEvaluation());
		
		return bestTree.getHashMap();
	}
	
	/**
	 * Picks a promotion piece. 
	 * TODO: check for possible checkmates or stalemates with selecting other pieces. If it is not beneficial to pick another piece, QUEEN
	 * @param piece Piece getting promoted
	 * @param currentBoard The current board state
	 * @return A string of the promotion piece that we have selected
	 */
	@Override
	public String pickPromotionPiece(Piece piece, ChessBoard chessBoard) {
		return "Q";
	}
	
	

}
