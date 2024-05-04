package greer;

import javax.swing.JLabel;

public class Piece {
	
	JLabel pieceLabel;
	int x;
	int y;
	String pieceInfo;
	String pieceType;
	String pieceColor;
	//boolean for if pawns can move twice
	public boolean canMoveTwo;
	//enPassant Boolean
	public boolean enPassantable;
	//castle rights for king piece
	public boolean castleRights;
	
	
	/**
	 * Constructor for the Piece type
	 * @param pieceLabel The JLabel for the piece
	 * @param x The x coordinate for the piece
	 * @param y The y coordinate for the piece
	 * @param pieceInfo The piece type in String format. First char is color: "W" white or "B" Black.
	 * Second char is piece type: "P" pawn, "R" rook, "N" knight, "B" bishop, "Q" queen, "K" king 
	 * 
	 * Note: Special Cases for pieceInfo include "Dot" or "TDot"
	 */
	public Piece(JLabel pieceLabel, int x, int y, String pieceInfo) {
		this.pieceLabel = pieceLabel;
		this.x = x;
		this.y = y;
		this.pieceInfo = pieceInfo; 
		this.pieceType = "" + pieceInfo.charAt(1);
		this.pieceColor = "" + pieceInfo.charAt(0);
		
		if(pieceColor.equals("W") && y == 1) {
			canMoveTwo = true;
		}else if(pieceColor.equals("B") && y == 6){
			canMoveTwo = true;
		}else {
			canMoveTwo = false;
		}
		
		if(pieceType.equals("K") || pieceType.equals("R")) {
			castleRights = true;
		}else {
			castleRights = false;
		}
		
		enPassantable = false;
	}
	
	/**
	 * The constructor for the piece if we are building off another piece
	 * @param other The other piece to build off of
	 * @param x The new x for the piece
	 * @param y The new y for the piece
	 */
	public Piece(Piece other, int x, int y) {
		this.pieceLabel = other.pieceLabel;
		this.x = x;
		this.y = y;
		this.pieceInfo = other.pieceInfo;
		this.pieceType = "" + pieceInfo.charAt(1);
		this.pieceColor = "" + pieceInfo.charAt(0);
		this.canMoveTwo = other.canMoveTwo;
		this.castleRights = other.castleRights;
		this.enPassantable = other.enPassantable;
	}
	
	/**
	 * Promotes the piece to the given type
	 * @param newPieceLabel The label to turn the piece to
	 * @param newPieceType The piece type to turn it into
	 */
	public void promotePiece(JLabel newPieceLabel, String newPieceType) {
		this.pieceLabel.setIcon(newPieceLabel.getIcon());
		this.pieceType = newPieceType;
		this.pieceInfo = pieceColor + pieceType;
	}
	
	/**
	 * Moves the piece to the given square
	 * @param pieceToMoveTo
	 */
	public void moveTo(Piece pieceToMoveTo) {
		this.x = pieceToMoveTo.getX();
		this.y = pieceToMoveTo.getY();
	}
	
	public JLabel getJLabel() {
		return pieceLabel;
	}
	
	public String getPieceType() {
		return pieceType;
	}
	
	public String getPieceColor() {
		return pieceColor;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public long getPostionBitboard() {
		return 1L << (x + 8 * y);
	}
	
	/**
	 * To string method to help visualize the array
	 */
	public String toString() {
		return pieceInfo + " at " + x + "," + y;
	}
	
	
	

}
