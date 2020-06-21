package mazeGenerator;

import maze.Maze;
import maze.Cell;
import maze.TunnelMaze;

import java.util.ArrayList;
import java.util.Random;

public class HuntAndKillGenerator implements MazeGenerator{

	// declaring all variables
	protected Random randomGenerator;
	protected int rowIndex = 0;
	protected int colIndex = 0;
	protected int[] directions = {0, 2, 3, 5};
	protected int directionIndex = 0;
	protected int aDirection = 0;
	protected boolean scan = true;
	protected boolean stopHunt;
	// mark visited cells
	protected ArrayList<Cell> visitedCells = new ArrayList<>();


	@Override
	public void generateMaze(Maze maze){


		// Step 1:
		// Pick a random starting cell
		randomGenerator = new Random();
		rowIndex = randomGenerator.nextInt(maze.sizeR);
		colIndex = randomGenerator.nextInt(maze.sizeC);


		// Step 2:
		// From the randomly selected cell, perform the walk
		walk(maze, rowIndex, colIndex);


		// Step 3:
		// Enter the "hunt" mode
		// until the mode scans the entire grid and finds no visited cells
		while (scan) {
			hunt(maze);
		}


	}// end of maze generator



	// Randomly select an unvisited neighbouring cell and carve a passage to the neighbour
	// Repeat this until the cell has no unvisited neighbours
	public void walk(Maze maze, int rowIndex, int colIndex){


		// Start from the randomly selected cell
		// add the cell into visited cells
		Cell startingCell = maze.map[rowIndex][colIndex];
		visitedCells.add(startingCell);


		/** For tunnel maze */
		if(maze instanceof TunnelMaze){
			Cell tunnelToCell = startingCell.tunnelTo;
			if(tunnelToCell != null && !(visitedCells.contains(tunnelToCell)))
				walk(maze, tunnelToCell.r, tunnelToCell.c);
		}


		// Pick a random direction
		directionIndex = randomGenerator.nextInt(directions.length);
		aDirection = directions[directionIndex];


		// In that random direction
		// If the neighbour is not null or is not visited
		// perform the walk again
		if(startingCell.neigh[aDirection] != null && !(visitedCells.contains(startingCell.neigh[aDirection]))){
			startingCell.wall[aDirection].drawn = false;
			startingCell.wall[aDirection].present = false;
			// move the starting cell to it's neighbour at the chosen direction, call the function again
			walk(maze, rowIndex + maze.deltaR[aDirection], colIndex + maze.deltaC[aDirection]);
		}
	}




	// Scan the grid searching for a cell that is:
	// Unvisited, adjacent to a visited cell
	public void hunt(Maze maze){

		stopHunt = false;

		while(!stopHunt){
			for(int i = 0; i < maze.sizeR; i ++) {
				for (int j = 0; j < maze.sizeC; j++) {

					Cell huntCell = maze.map[i][j];

					// if the cell is unvisited
					if (!visitedCells.contains(huntCell)) {

						// if one of it's neighbour in a direction(4) is visited
						// from that cell perform the walk again
						for (int k = 0; k < 4; k++) {
							if (visitedCells.contains(huntCell.neigh[directions[k]])) {
								huntCell.wall[directions[k]].drawn = false;
								huntCell.wall[directions[k]].present = false;
								walk(maze, i, j);
								stopHunt = true;
								break;
							}
						}
					}
					// break out of the for loop
					if(stopHunt)
						break;
				}
			}
			// stop hunt until there are no unvisited cells in the maze
			if(!stopHunt)
				scan = false;
			stopHunt = true;

		}// end of wile loop
	}// end of hunt method


}// end of class HuntAndKillGenerator
