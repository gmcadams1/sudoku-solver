/**
 * Filename: Driver.java
 * @author Gregory McAdams
 * Date: October 7, 2013
 * E-mail: gmcadams1@comcast.net
 **/
package su;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Sudoku.numRegions = 9; // 9x9
		Sudoku board = new Sudoku();
		
		// Read in input
		BufferedReader br = null;
		try {
			
			String aLine;
			br = new BufferedReader(new FileReader("input.txt"));
			
			int row = 0;
			while ((aLine = br.readLine()) != null)
			{
				String theLine = "";

				// Process one line (i.e. make *=0)
				for (int i = 0; i < aLine.length(); i++) 
				{
					if (aLine.charAt(i) == '*')
						theLine += '0';
					else
						theLine += aLine.charAt(i);
				}
				
				String temp = "";
				
				for (int i = 0; i < Sudoku.numRegions; i++) 
				{
					temp += theLine.charAt(i);
					if (temp.equals("A"))
						temp = "10";
					else if (temp.equals("B"))
						temp = "11";
					else if (temp.equals("C"))
						temp = "12";
					else if (temp.equals("D"))
						temp = "13";
					else if (temp.equals("E"))
						temp = "14";
					else if (temp.equals("F"))
						temp = "15";
					else if (temp.equals("G"))
						temp = "16";
					board.setSquare(row, i, Integer.parseInt(temp));
					temp = "";
				}
				row++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Includes ac3 algorithm
		ArcReduce arcReducer = new ArcReduce();
		// Arc reduce the initial state
		board = arcReducer.arcReduce(board);
		
		ArrayList<Sudoku> nextGen = new ArrayList<Sudoku>();
		Stack<Sudoku> stack = new Stack<Sudoku>();
		// Generate children, then arc reduce them (some are eliminated)
		nextGen = arcReducer.arcReduce(board.generateChildren());
		Sudoku goalNode;

		// Contains the search part
		while (true)
		{
			Collections.reverse(nextGen);
			stack.addAll(nextGen);
			if (stack.size() < 1) {
				System.out.println("Problem not solvable, exiting.");
				System.exit(1);
			}
			goalNode = stack.pop();
			if (goalNode.finalGoal() == true)
				break;
 			nextGen = arcReducer.arcReduce(goalNode.generateChildren());
		}
		
		System.out.println("Solved:");
		printBoard(goalNode);
	}
	
	private static void printBoard(Sudoku finalBoard)
	{
		if (finalBoard == null)
			System.out.println("No Solution.");
		else
		{
			for (int i = 0; i < Sudoku.numRegions; i++)
			{
				for (int j = 0; j < Sudoku.numRegions; j++)
					System.out.printf("%-5s", finalBoard.getSquareDigit(i, j));
				System.out.println("");
			}
		}
	}
}
