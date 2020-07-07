/**
 * Filename: ArcReduce.java
 * @author Gregory McAdams
 * Date: October 7, 2013
 * E-mail: gmcadams1@comcast.net
 **/
package su;

import java.util.ArrayList;
import java.util.Stack;

public class ArcReduce {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
		
	// Takes in a CSP and returns that CSP but with a smaller domain
	public ArcReduce()
	{
		
	}

	// Arc reduce a given csp
	public Sudoku arcReduce(Sudoku csp)
	{		
		boolean finished = false;
		while (finished == false)
		{
			Stack<Square> worklist = new Stack<Square>();
			int numChanges = 0;

			// Push all squares onto the stack
			// The squares are only used to find the row, col used to find a Square in the CSP
			for (int i = 0; i < Sudoku.numRegions; i++)
				for (int j = 0; j < Sudoku.numRegions; j++)
					worklist.push(new Square(i, j));
					
			while (worklist.size() > 0)
			{
				boolean changed = false;
				Square next = worklist.pop();
	
				// Row neighbors; iterate on columns
				// 'Remove' an arc
				for (int i = 0; i < Sudoku.numRegions; i++)
				{
					// No arc to itself
					if (next.getCol() == i)
						continue;
					Square neighbor = new Square(next.getRow(), i);
					changed = propogation(csp, next, neighbor);
					if (changed)
					{
						if (csp.getSquare(next.getRow(), next.getCol()).getDomainSize() == 0)
							return null;
						if (csp.getSquare(neighbor.getRow(), neighbor.getCol()).getDomainSize() == 0)
							return null;
						numChanges++;
					}
				}
				
				// Col neighbors
				for (int i = 0; i < Sudoku.numRegions; i++)
				{
					// No arc to itself
					if (next.getRow() == i)
						continue;
					Square neighbor = new Square(i, next.getCol());
					changed = propogation(csp, next, neighbor);
					if (changed)
					{
						if (csp.getSquare(next.getRow(), next.getCol()).getDomainSize() == 0)
							return null;
						if (csp.getSquare(neighbor.getRow(), neighbor.getCol()).getDomainSize() == 0)
							return null;
						numChanges++;
					}
				}
				
				// Region neighbors
				int sqrt = (int) Math.sqrt(Sudoku.numRegions);
				int row = next.getRow() - (next.getRow() % sqrt);
				int col = next.getCol() - (next.getCol() % sqrt);
				for (int i = 0; i < Math.sqrt(Sudoku.numRegions); i++)
				{
					for (int j = 0; j < Math.sqrt(Sudoku.numRegions); j++)
					{
						if (i == next.getRow() % sqrt && j == next.getCol() % sqrt) {
							col++;
							continue;
						}
	
						Square neighbor = new Square(row, col);
						changed = propogation(csp, next, neighbor);
						if (changed)
						{
							if (csp.getSquare(next.getRow(), next.getCol()).getDomainSize() == 0)
								return null;
							if (csp.getSquare(neighbor.getRow(), neighbor.getCol()).getDomainSize() == 0)
								return null;
							numChanges++;
						}
						col++;
					}
					row++;
					col = next.getCol() - (next.getCol() % sqrt);
				}
			}
			if (numChanges == 0)
				finished = true;
		}
		
		// Check the domain for each square
		// If any domain is empty, return null
		for (int i = 0; i < Sudoku.numRegions; i++)
			for (int j = 0; j < Sudoku.numRegions; j++)
				if (csp.getSquare(i, j).getDomainSize() == 0)
					return null;
		
		return csp;
	}
	
	private boolean propogation(Sudoku csp, Square source, Square neighbor)
	{
		// Basically just do:
		// If neighbor has 1 in its domain, remove 1 from source
		// So remove a domain of source if it appears in neighbor
		boolean change = false;
		ArrayList<Integer> nDomain = csp.getSquare(neighbor.getRow(), neighbor.getCol()).getDomain();
		ArrayList<Integer> sDomain = csp.getSquare(source.getRow(), source.getCol()).getDomain();
		// Remove from source domain
		for (int i = 0; i < nDomain.size(); i++)
		{
			for (int j = 0; j < sDomain.size(); j++)
			{
				if (nDomain.get(i) == sDomain.get(j) && nDomain.size() == 1) {
					sDomain.remove(j);
					change = true;
					break;
				}
			}
		}
		
		// Remove from neighbor domain
		for (int i = 0; i < nDomain.size(); i++)
		{
			for (int j = 0; j < sDomain.size(); j++)
			{
				if (nDomain.get(i) == sDomain.get(j) && sDomain.size() == 1) {
					nDomain.remove(i);
					change = true;
					break;
				}
			}
		}
		
		csp.getSquare(neighbor.getRow(), neighbor.getCol()).setDomain(nDomain);
		csp.getSquare(source.getRow(), source.getCol()).setDomain(sDomain);
		return change;
	}
	
	// Arc reduce a list of children and return the new
	public ArrayList<Sudoku> arcReduce(ArrayList<Sudoku> children)
	{
		ArrayList<Sudoku> newChildren = new ArrayList<Sudoku>();
		
		for (int i = 0; i < children.size(); i++)
		{
			Sudoku next = arcReduce(children.get(i));
			if (next != null)
				newChildren.add(next);
		}
		
		return newChildren;
	}
}
