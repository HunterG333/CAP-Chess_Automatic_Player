package greer;

import java.util.HashMap;

public class K_ary_Tree implements Runnable{
	
	//the root of the tree
	Node root = null;
	
	Piece pieceMoved;
	String move;
	
	String searchingFor;
	
	//the evaluation that gets updated during and after a search
	public double updatedEvaluation;
	
	/**
	 * I think it is important to point out that the Tree stores Nodes which store the positions that are in response to a particular move
	 * So the root node is the position of the best response to the move we are looking at
	 * 
	 * Initialize tree with root node being the current move we are looking at choosing
	 * @param currentPosition A chessboard object for the move we are looking at
	 * @param pieceListForPosition A list of the pieces that are on the board for this position
	 * @param evalObject An Evaluation object so we dont have to create a billion different objects
	 * @param logic A MoveLogic object so we can access move generation without creating a ton of object
	 * @param searchingFor A string for what color we are searching for
	 * @throws Exception when the root has a position of null
	 */
	public K_ary_Tree(ChessBoard currentPosition, Piece[] pieceListForPosition, Evaluation evalObject, MoveLogic logic, String searchingFor, Piece pieceMoved, String move) throws Exception {
		this.pieceMoved = pieceMoved;
		this.move = move;
		this.searchingFor = searchingFor;
		updatedEvaluation = evalObject.getEvaluation(currentPosition, pieceListForPosition, searchingFor);
		root = new Node(currentPosition, pieceListForPosition, evalObject, logic, searchingFor, 1);
		updatedEvaluation = root.positionEvaluation;
		
		if(root.currentPosition == null) {
			throw new Exception("Root position is null");
		}
		
	}
	
	@Override
	public void run() {
		newSearch();
	}
	
	public double getEvaluation() {
		return updatedEvaluation;
	}
	
	public HashMap<Piece, String> getHashMap(){
		HashMap<Piece, String> temp = new HashMap<Piece, String>();
		
		temp.put(pieceMoved, move);
		
		return temp;
	}
	
	
	/**
	 * Connects to a Node and generates a new depth of children
	 */
	public void newSearch() {
		
		this.updatedEvaluation = root.search();
		
	}
	
	public String toString() {
		return "Evaluation of Tree Position is " + updatedEvaluation;
	}

}
