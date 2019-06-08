
/**
 * Creates a board for the game, then mutates it depending on what the user has selected.
 * 
 * @author JackGuzzetta
 * @version (11/2/16)
 */
package hw3;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.Icon;
import api.Descriptor;
import api.Dot;
import api.Util;
import java.lang.Math; 

/**
 * This class encapsulates the game logic for a video game called Dots. The game
 * consists of a 2D array or grid of colored icons, called dots, along with an
 * ordered list that we will call the selection list. Intuitively, the selection
 * list represents a set of adjacent dots, all of the same color, that have been
 * selected by the player. When selection is complete (e.g. the mouse is
 * released), the selected dots disappear from the grid, and the dots above
 * shift down to take their places. Then new dots fill in each column from the
 * top. A point is scored for each dot that disappears from the grid. There is a
 * special rule for the case that the selected dots form a loop; then all dots
 * in the grid of the same color disappear too.
 */
public class DotsGame {
	private Descriptor selection;
	private Descriptor firstDot;
	private Descriptor lastDot;
	private Dot[][] grid;
	private Generator dotGenerator;
	private int score = 0;
	private ArrayList<Descriptor> selectionList;

	/**
	 * Constructs a game with the given number of columns and rows that will use
	 * the given Generator instance to create new icons. The dots in the initial
	 * grid are produced by the generator.
	 * 
	 * @param width
	 *            number of columns
	 * @param height
	 *            number of rows
	 * @param generator
	 *            generator for new icons
	 */
	public DotsGame(int width, int height, Generator generator) {
		grid = new Dot[height][width];
		selectionList = new ArrayList<Descriptor>();
		generator.initialize(grid);
		dotGenerator = generator;

	}

	/**
	 * Constructs a game based on the given string array according to the
	 * conventions of Util.createGridFromString. The given Generator instance is
	 * used to create new dots.
	 * 
	 * @param data
	 *            string indicating initial configuration of grid
	 * @param generator
	 *            generator for new icons
	 */
	public DotsGame(String[] data, Generator generator) {

		grid = Util.createGridFromString(data);
		dotGenerator = generator;
		selectionList = new ArrayList<Descriptor>();
	}

	/**
	 * Returns the Dot object at the given row and column.
	 * 
	 * @param row
	 *            row within the grid
	 * @param col
	 *            column within the grid
	 * @return Dot object at the given row and column
	 */
	public Dot getDot(int row, int col) {
		return grid[row][col];
	}

	/**
	 * Sets the Dot object at the given row and column.
	 * 
	 * @param row
	 *            row of the grid to be modified
	 * @param col
	 *            column of the grid to be modified
	 * @param dot
	 *            the given Dot object to set
	 */
	public void setDot(int row, int col, Dot dot) {
		grid[row][col] = dot;
	}

	/**
	 * Returns the number of columns in this game.
	 * 
	 * @return number of columns
	 */
	public int getWidth() {
		return grid[0].length;
	}

	/**
	 * Returns the number of rows in this game.
	 * 
	 * @return number of rows
	 */
	public int getHeight() {
		return grid.length;
	}

	/**
	 * Returns the current score for this game.
	 * 
	 * @return score for this game
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Attempts to select the dot at given position. A descriptor for the dot is
	 * added to the selection list provided that a) the given position is
	 * adjacent to the last one added to the selection list, and b) its type
	 * matches the type of those already in the selection list, and c) the given
	 * position is not already in the selection list OR it completes a loop.
	 * Completing a loop means that the given position matches the first one in
	 * the selection list, the list has length at least 3, and the given
	 * position does not already occur twice in the list.
	 * 
	 * @param row
	 *            row of the dot to be selected
	 * @param col
	 *            column of the dot to be selected
	 */
	public void select(int row, int col) {
		selection = new Descriptor(row, col, getDot(row, col));
		if (selectionList.size() == 0) {
			selectionList.add(selection);
		} else {
			lastDot = selectionList.get((selectionList.size()) - 1);
			firstDot = selectionList.get(0);
			if ((lastDot.row() == selection.row() && Math.abs(lastDot.col() - selection.col()) == 1)
					|| (lastDot.col() == selection.col() && Math.abs(lastDot.row() - selection.row()) == 1)) {
				boolean sel = false;
				for (Descriptor d : selectionList) {
					if (d.equals(selection)) {
						sel = true;
					}
				}
				if (sel) {
					if (selectionList.size() >= 4) {
						if (selection.equals(firstDot))
							selectionList.add(selection);
					}
				} else {
					if (selection.getDot().getType() == lastDot.getDot().getType()) {
						selectionList.add(selection);
					}
				}
			}
		}

	}

	/**
	 * Returns a list of descriptors for currently selected dots.
	 * 
	 * @return the selection list
	 */
	public ArrayList<Descriptor> getSelectionList() {

		return selectionList;
	}

	/**
	 * If the selection list has at least two elements, replaces all selected
	 * positions with null, clears the selection list, and updates the score. If
	 * the selection list does not contain at least two elements, no positions
	 * are nulled but the selection list is still cleared. If the selection list
	 * includes a completed loop, then all dots of matching type are also nulled
	 * and the score is updated accordingly. The method returns a list
	 * containing all nulled positions. (The list is in no particular order but
	 * should not contain duplicates.)
	 * 
	 * @return list of descriptors for cells that are nulled as a result of this
	 *         operation
	 */
	public ArrayList<Descriptor> release() {

		ArrayList<Descriptor> list = new ArrayList<Descriptor>();
		if ((selectionList.size() >= 4) && (selection.equals(firstDot))) {
			// if selected dots completes a loop all other dots of the same type
			// get nulled
			Dot temp = selectionList.get(0).getDot();
			list = new ArrayList<Descriptor>();
			for (int i = 0; i < grid.length-1; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					if (grid[i][j].equals(temp)) {
						score += 1;
						list.add(new Descriptor(i, j, grid[i][j]));
						grid[i][j] = null;

					}
				}
			}
		}

		else if (selectionList.size() >= 3) {

			list = new ArrayList<Descriptor>();
			for (int i = 0; i < selectionList.size(); i++) {
				Descriptor desc = selectionList.get(i);
				list.add(desc);
				grid[desc.row()][desc.col()] = null;
			}
			score += Math.pow(selectionList.size(),2);
		}
		selectionList.clear();
		return list;
	}

	/**
	 * Collapses the dots in the given column of the current game grid such that
	 * all null dots, if any, are at the top of the column and non-null dots are
	 * shifted toward the bottom (i.e., as if by gravity). The returned list
	 * contains Descriptors representing dots that were moved (if any) with
	 * their new row and column; moreover, each Descriptor's
	 * <code>getPreviousRow</code> method returns the original row of the dot.
	 * The returned list is in no particular order.
	 * 
	 * @param col
	 *            column to be collapsed
	 * @return list of descriptors for moved dots
	 */
	public ArrayList<Descriptor> collapseColumn(int col) {
		ArrayList<Descriptor> list = new ArrayList<Descriptor>();
		int count = 0;
		for (int i = grid.length - 1; i > 0; i--) {
			if (grid[i][col] == null) {
				count++;
			}
		}
		for (int j = 0; j <= count; j++) {
			for (int i = grid.length - 1; i > 0; i--) {
				if (grid[i][col] == null) {

					if (grid[i - 1][col] != null) {
						Dot temp = grid[i - 1][col];
						grid[i - 1][col] = null;
						grid[i][col] = temp;
						Descriptor des = new Descriptor(i, col, grid[i][col]);
						list.add(des);
					}
				}
			}
		}
		return list;
	}

	

	/**
	 * Fills the null grid positions (if any) at the top of the given column in
	 * the current game grid. The returned list contains Descriptors
	 * representing new dots added to the column with their new row and column.
	 * The previous row for all descriptors is set to -1. The new dots are
	 * produced by the generator's <code>generate</code> method. The list is in
	 * no particular order.
	 * 
	 * @param col
	 *            column to be filled
	 * @return list of new descriptors for dots added to the column
	 */
	public ArrayList<Descriptor> fillColumn(int col) {
		ArrayList<Descriptor> list = new ArrayList<Descriptor>();

		for (int i = 0; i < grid.length; i++) {
			if (grid[i][col] == null) {
				grid[i][col] = dotGenerator.generate();
				Descriptor d = new Descriptor(i, col, grid[i][col]);

				list.add(d);
				d.setPreviousRow(i);
			}

			else
				break;

		}

		return list;

	}

}