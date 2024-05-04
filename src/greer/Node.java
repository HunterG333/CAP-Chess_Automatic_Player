package greer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Node {
	
	//so we dont have to create an object a million times we pass it through and use the same object
	Evaluation evalObject;
	MoveLogic logic;
	
	public int depth;
	
	String ourColor;
	
	//positions after best response is played
	ChessBoard currentPosition;
	public double positionEvaluation;
	public Piece[] pieceListForPosition;
	
	Node parentNode;
	ArrayList<Node> childrenNodes = new ArrayList<Node>();
	
	boolean deadPosition = false;
	
	/**
	 * Creates a Node from the inputs given
	 * @param currentPosition The current position for the node
	 * @param pieceListForPosition The piece list for the node
	 * @param evalObject The eval object for the node
	 * @param logic The logic object for the node
	 * @param searchingFor The color we are searching for
	 * @param depth the depth that the node resides on
	 */
	public Node(ChessBoard currentPosition, Piece[] pieceListForPosition, Evaluation evalObject, MoveLogic logic, String searchingFor, int depth) {
		
		this.depth = depth;
		this.evalObject = evalObject;
		this.logic = logic;
		this.pieceListForPosition = pieceListForPosition;
		ourColor = searchingFor;
		
		//compute best response and update the position
		if(searchingFor.equals("W")) {
			//find best response for black
			int responseNum = findBestResponse(currentPosition, pieceListForPosition, "B");
			
			if(responseNum == -1) {
				deadPosition = true;
			}
		}else {
			//find best response for white
			int responseNum = findBestResponse(currentPosition, pieceListForPosition, "W");
			
			if(responseNum == -1) {
				deadPosition = true;
			}
		}
		
		
		
	}

	/**
	 * Searches the node to check if it has children or if the children need to be created
	 * @return a double for the best evaluated position from its children
	 */
	public double search() {
		
		if(deadPosition) {
			return positionEvaluation;
		}
		
		if(childrenNodes.isEmpty()) {
			//add children to this node
			addChildren();
			
			//loop through all children and return the best evaluation
			double bestEvaluation;
			if(ourColor.equals("W")) {
				bestEvaluation = -999;
			}else {
				bestEvaluation = 999;
			}
			
			for(int i = 0; i < childrenNodes.size(); i++) {
				Node nodeToLookAt = childrenNodes.get(i);
				
				if(ourColor.equals("W") && nodeToLookAt.positionEvaluation > bestEvaluation) {
					bestEvaluation = nodeToLookAt.positionEvaluation;
				}else if(ourColor.equals("B") && nodeToLookAt.positionEvaluation < bestEvaluation) {
					bestEvaluation = nodeToLookAt.positionEvaluation;
				}
			}
			return bestEvaluation;
			
		}else{ //loop through all the children and recursively call this function till the children have been added so that the evaluation gets updated
			
			double bestEvaluation;
			if(ourColor.equals("W")) {
				bestEvaluation = -999;
			}else {
				bestEvaluation = 999;
			}
			
			//loop through every child node to find every node for the depth we are looking for
			for(int i = 0; i < childrenNodes.size(); i++) {
				
				Node nodeToSearch = childrenNodes.get(i);
				
				//recursively search through all the children until a layer does not have children. We will then look at possible moves
				double bestReturnedEvaluation = nodeToSearch.search();
				
				if(ourColor.equals("W") && bestReturnedEvaluation > bestEvaluation) {
					bestEvaluation = bestReturnedEvaluation;
				}else if(ourColor.equals("B") && bestReturnedEvaluation < bestEvaluation) {
					bestEvaluation = bestReturnedEvaluation;
				}
			}
			
			return bestEvaluation;
			
		}
		
		
		
	}
	
	/**Adds the children for the node by looking at potential responses to a position
	 * TODO FILL OUT
	 */
	public void addChildren() {
		
		//create a list of pieces that have valid moves
		ArrayList<Piece> sameColoredPieces = evalObject.generateSameColoredPieces(currentPosition, pieceListForPosition, ourColor, logic);
		
		//generate a list of possible moves
		for(int i = 0; i < sameColoredPieces.size(); i++) {
			
			Piece pieceToMove = sameColoredPieces.get(i);
			String[] moves = logic.findMoveList(pieceToMove, currentPosition, pieceListForPosition);
			
			
			for(int j = 0; j < moves.length && !(moves[j].equals("")); j++) {//for every create a new node
				
				int moveToX = Integer.parseInt("" + moves[j].charAt(0));
				int moveToY = Integer.parseInt("" + moves[j].charAt(1));
				
				//updated board for the move we are checking
				ChessBoard updatedBoard = evalObject.updateChessBoard(new ChessBoard(currentPosition), pieceToMove, moveToX, moveToY);
				
				Piece[] newPieceList = evalObject.updatePieceList(pieceListForPosition, pieceToMove, moveToX, moveToY);
				
				//create new node
				Node childNode = new Node(updatedBoard, newPieceList, evalObject, logic, ourColor, depth+1);
				
				//add node to ArrayList
				childrenNodes.add(childNode);
				
			}
		}
	
		
	}
	
	/** COMPLETED I THINK
	 * Best response to a position is BotV2's response to the position
	 * @param position
	 * @param pieceList
	 * @param toSearchFor
	 */
	private int findBestResponse(ChessBoard position, Piece[] pieceList, String toSearchFor) {
		
		
		BotV2 botResponse = new BotV2();
		HashMap<Piece, String> moveToPlay;
		
		Piece[] pieceListClone = pieceList.clone();
		
		moveToPlay = botResponse.selectMove(position, pieceListClone, toSearchFor);
		
		if(moveToPlay == null) {
			//is mated or stalemated so cannot respond
			if(botResponse.isCheckmated) {
				if(toSearchFor.equals("W")) {
					this.positionEvaluation = 500;
				}else {
					this.positionEvaluation = -500;
				}
			}else {
				this.positionEvaluation = 0;
			}
			
			return -1;
			
		}else {
			//update the cloneBoard to reflect the bots response to our move we are checking
			Set<Piece> pieceSet = moveToPlay.keySet();
			List<Piece> pieceListClones = new ArrayList<>(pieceSet);
			Piece responseMovePiece = pieceListClones.get(0);
			String tempDotCoords = moveToPlay.get(responseMovePiece);
			Piece tempPiece = botResponse.createTempPiece(tempDotCoords);
			
			ChessBoard cloneBoard = new ChessBoard(position);
			cloneBoard.updateBitboard(responseMovePiece, tempPiece);
			cloneBoard.updateAttackedSquares();
			
			//update cloned array
			int x = Character.getNumericValue(tempDotCoords.charAt(0));
			int y = Character.getNumericValue(tempDotCoords.charAt(1));
			
			int indexInArray = findIndex(responseMovePiece, pieceListClone);
			pieceListClone[indexInArray] = null;
			pieceListClone[y * 8 + x] = responseMovePiece;
			
			
			this.positionEvaluation = evalObject.getEvaluation(cloneBoard, pieceList, toSearchFor);
			this.currentPosition = cloneBoard;
			this.pieceListForPosition = pieceListClone;
		}
		
		return 0;
		
		
	}
	
	/**
	 * Prints out the node
	 */
	public String toString() {
		System.out.println("Node Print Out:");
		System.out.println("Evaluation: " + positionEvaluation);
		currentPosition.toString();
		return "";
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
