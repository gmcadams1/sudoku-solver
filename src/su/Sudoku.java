/**
 * Filename: Sudoku.java
 * @author Gregory McAdams
 * Date: October 7, 2013
 * E-mail: gmcadams1@comcast.net
 **/
package su;

import java.util.ArrayList;

public class Sudoku {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private Square[][] squares;
	public static int numRegions;
	private Square lastSquare; // Last square assigned
	
	public Sudoku()
	{
		this.squares = new Square[numRegions][numRegions];
		for (int i = 0; i < numRegions; i++)
			for (int j = 0; j < numRegions; j++)
				this.squares[i][j] = new Square(i, j);
	}
	
	public Sudoku(Sudoku aCopy)
	{
		this.squares = new Square[numRegions][numRegions];
		for (int i = 0; i < numRegions; i++)
			for (int j = 0; j < numRegions; j++)
				this.squares[i][j] = new Square(aCopy.getSquare(i, j));
	}
	
	public void setSquare(int row, int col, int digit)
	{
		this.squares[row][col].setDigit(digit);
	}
	
	public int getSquareDigit(int row, int col)
	{
		return this.squares[row][col].getDigit();
	}
	
	public Square getSquare(int row, int col)
	{
		return this.squares[row][col];
	}
	
	public Square getLastSquare()
	{
		return this.lastSquare;
	}
	
	public boolean finalGoal()
	{
		boolean complete = true;
		for (int i = 0; i < numRegions; i++)
			for (int j = 0; j < numRegions; j++)
				if (squares[i][j].getDigit() == 0)
					return false;
		return complete;
	}
	
	// Generate all children from this board
	// ONLY GENERATE CHILDREN FOR NEXT MOVE, BACKTRACK IF DEAD END
	public ArrayList<Sudoku> generateChildren()
	{
		ArrayList<Sudoku> children = new ArrayList<Sudoku>();
		
		// ROW+COL THAT ALL CHILDREN MUST BE
		// Choose by using most constrained variable heuristic
		int row = 0, col = 0, smallest = Sudoku.numRegions + 1;
		for (int i = 0; i < numRegions; i++)
			for (int j = 0; j < numRegions; j++)
				if (squares[i][j].getDomainSize() < smallest && squares[i][j].getDigit() == 0) {
					smallest = squares[i][j].getDomainSize();
					row = i;
					col = j;
				}
		
		// GENERATE A CHILD FOR EACH DOMAIN IN THIS NEXT MOVE
		ArrayList<Integer> domain = new ArrayList<Integer>(squares[row][col].getDomain());
		// If dead-end found here, do not add
		if (domain.size() > 0)
		{
			// For each domain, add a child
			while (domain.size() > 0) 
			{
				Sudoku parent = new Sudoku(this);
				parent.lastSquare = new Square(parent.squares[row][col]);
				parent.squares[row][col].setDigit(domain.get(0));
				parent.lastSquare.setDigitNoModify(domain.get(0));
				domain.remove(0);
				children.add(parent);
			}
		
			return children;
		}
		else
			return null; // NO VALID CHILDREN FOR THIS MOVE; DEAD END
	}
}
