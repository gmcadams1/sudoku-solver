/**
 * Filename: Square.java
 * @author Gregory McAdams
 * Date: October 7, 2013
 * E-mail: gmcadams1@comcast.net
 **/
package su;

import java.util.ArrayList;

public class Square implements Comparable<Square> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private ArrayList<Integer> domain;
	private int digit;
	private int row;
	private int col;
	
	// Initially domain is 1-9
	public Square(int row, int col)
	{
		this.domain = new ArrayList<Integer>();
		for (int i = 1; i <= Sudoku.numRegions; i++)
			this.domain.add(i);
		this.digit = 0;
		this.row = row;
		this.col = col;
	}
	
	public Square(Square aCopy)
	{
		this.domain = new ArrayList<Integer>(aCopy.domain);
		this.digit = aCopy.digit;
		this.row = aCopy.row;
		this.col = aCopy.col;
	}
	
	public int getDomainSize()
	{
		return this.domain.size();
	}
	
	public int getDigit()
	{
		return this.digit;
	}
	
	public int getRow()
	{
		return this.row;
	}
	
	public int getCol()
	{
		return this.col;
	}
	
	public void setDomain(ArrayList<Integer> domain)
	{
		this.domain = new ArrayList<Integer>(domain);
	}
	
	public ArrayList<Integer> getDomain()
	{
		return this.domain;
	}
	
	// Implicitly reduces the domain to only this digit
	public void setDigit(int digit)
	{
		this.digit = digit;
		if (digit > 0 && domain.size() > 1)
		{
			ArrayList<Integer> newDomain = new ArrayList<Integer>();
			newDomain.add(digit);
			setDomain(newDomain);
		}
		return;
	}
	
	// Does not modify domain -- used for lastDigit in Sudoku for value heuristic
	public void setDigitNoModify(int digit)
	{
		this.digit = digit;
	}

	@Override
	public int compareTo(Square comp) 
	{
		if (this.domain.size() > comp.domain.size())
			return 1;
		else if (this.domain.size() < comp.domain.size())
			return -1;
		else
			return 0;
	}
}
