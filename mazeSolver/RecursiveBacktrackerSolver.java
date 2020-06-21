package mazeSolver;
import mazeGenerator.CellStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import maze.Maze;

public class RecursiveBacktrackerSolver implements MazeSolver {

	HashMap<String, CellStorage> cellVisited = new HashMap<String,CellStorage>();
	HashMap<String, CellStorage> hshMapCells = new HashMap<String,CellStorage>();
	ArrayList<Integer> availableDirection = new ArrayList<Integer>();
	CellStorage currentTargetCell;
	CellStorage endCell;
	boolean allCellVisited = false;
	boolean available = false;
	Random random = new Random();
	int lengthOfPath = 0;

	@Override
	public void solveMaze(Maze input) {

		// create nodes for each cell
		createNodes(input);

		//set connection for each connected cells
		for(CellStorage i : hshMapCells.values()){
			//Creating new edge for each direction
			for(int index = 0; index < input.NUM_DIR; index++){
				if(i.getCell().neigh[index] != null){
					if(i.getCell().wall[index].present == false && i.getCell().wall[index].drawn == false){
						CellStorage destinationCell = hshMapCells.get("[" + i.getCell().neigh[index].c + "," + i.getCell().neigh[index].r + "]");
						if(destinationCell != null)
							i.setNeighbourCell(destinationCell, index);
					}
				}
			}
			// if maze is a tunnel maze
			if(input.type == 1){
				tunnel(input);
			}
		}

		// Find the starting point of the maze
		currentTargetCell = hshMapCells.get("[" + input.entrance.c + "," + input.entrance.r + "]");
		endCell = hshMapCells.get("[" + input.exit.c + "," + input.exit.r + "]");

		while(currentTargetCell != endCell){
			input.drawFtPrt(currentTargetCell.getCell());

			// If Cell is a tunnel and it has not been visited
			if(currentTargetCell.getCellNeighbour(6) != null && !cellVisited.containsKey(currentTargetCell.getCellNeighbour(6).getIndex())){
				currentTargetCell.getCellNeighbour(6).setPrevious(currentTargetCell);
				cellVisited.put(currentTargetCell.getIndex(), currentTargetCell);

				currentTargetCell = currentTargetCell.getCellNeighbour(6);
			}
			else{
				// Checks if there is available Cells to visit.
				allCellVisited = true;
				for(int index = 0; index<7; index++){
					if(currentTargetCell.getCellNeighbour(index) != null){
						if(!cellVisited.containsKey(currentTargetCell.getCellNeighbour(index).getIndex())){
							allCellVisited = false;
						}
					}
				}
				// If all nodes are visited, go back a step
				if(allCellVisited){
					cellVisited.put(currentTargetCell.getIndex(), currentTargetCell);
					currentTargetCell = currentTargetCell.getPreviousCell();
				}
				// If there are available nodes to visit.
				else{
					available = false;
					// Starts a loop until available nodes to visit is visited.
					while(!available){
						// Get available direction
						for(int index = 0 ; index < input.NUM_DIR ; index++){
							if(currentTargetCell.neighbour[index] != null){
								if(!cellVisited.containsKey(currentTargetCell.neighbour[index].getIndex())){
									availableDirection.add(index);
								}
							}
						}
						int randomDirection;
						randomDirection = random.nextInt(availableDirection.size());
						int direction = availableDirection.get(randomDirection);

						if(currentTargetCell.getCellNeighbour(direction) != null){
							if(!cellVisited.containsKey(currentTargetCell.getCellNeighbour(direction).getIndex())){
								CellStorage nextNode = hshMapCells.get(currentTargetCell.getCellNeighbour(direction).getIndex());
								nextNode.setPrevious(currentTargetCell);
								cellVisited.put(currentTargetCell.getIndex(), currentTargetCell);
								currentTargetCell = nextNode;
								available = true;
							}
						}

					}
				}
			}
		}
		// Draw the exit node and add it to visited.
		input.drawFtPrt(currentTargetCell.getCell());
		cellVisited.put(currentTargetCell.getIndex(), currentTargetCell);
		// Count the path length
		currentTargetCell.countPath();
		System.out.println("Path length of the solution is " + lengthOfPath);
	} // end of solveMaze()

	public void tunnel(Maze input) {
		for(CellStorage i : hshMapCells.values()){
			if(i.getCell().tunnelTo != null){
				CellStorage tunnelDestination = hshMapCells.get("[" + i.getCell().tunnelTo.c + "," + i.getCell().tunnelTo.r + "]");
				i.setNeighbourCell(tunnelDestination, 6);
			}

		}
	}
	public void createNodes(Maze input ) {
		for(int i = 0; i < input.sizeR; i++){
			for(int x = 0; x < input.sizeC; x++){
				CellStorage newCell = new CellStorage(input.map[i][x]);
				hshMapCells.put(newCell.getIndex(), newCell);
			}
		}
	}
	@Override
	public boolean isSolved() {
		// The maze is solved when the solveMaze function stopped
		return true;
	} // end if isSolved()

	@Override
	public int cellsExplored() {
		return cellVisited.size();
	} // end of cellsExplored()
} // end of class RecursiveBackTrackerSolver
