package mazeGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import maze.*;

public class KruskalGenerator implements MazeGenerator {

	ArrayList<EdgeStorage> edge = new ArrayList<EdgeStorage>();
	ArrayList<CellStorage> node = new ArrayList<CellStorage>();
	ArrayList<HashMapConnectedNodes> hshMConnectedNodes = new ArrayList<HashMapConnectedNodes>();
	int x = 0;
	int j = 0;
	boolean status = true;
	int i = 0;

	@Override
	public void generateMaze(Maze maze) {
		// create nodes for each cell
		if (maze.type == 1) {
			// Normal Maze
			createNodesNormal(maze);
		} else {
			// Tunnel Maze
			createNodesTunnel(maze);
		}

		// create a new edge for each connected cells
		createEdge(maze);

		// loop through all the listed edges in random order and determine if the
		// cell is already connected or not, if not create a new connection.
		while (!edge.isEmpty()) {
			Random randomObj = new Random();
			x = randomObj.nextInt(edge.size());
			j = edge.get(x).getTargetDirection();
			HashMapConnectedNodes from = null;
			HashMapConnectedNodes to = null;
			HashMapConnectedNodes obj = new HashMapConnectedNodes();
			HashMapConnectedNodes toSet = null;
			boolean loop = true;
			// validateConnection method returns true if a connection is found
			status = validateConnection(edge.get(x).getNodeFrom(), edge.get(x).getNodeTo());
			if (status == false) {
				// Calls addConnectedEdge method and add to arryConnectedEdges for both of the
				// nodes
				addConnectedEdge();

				// Search hshMConnectedNodes for the current node to the target node
				for (int counter = 0; counter < hshMConnectedNodes.size(); counter++) {
					if (hshMConnectedNodes.get(counter).searchNode(edge.get(x).getNodeFrom()))
						from = hshMConnectedNodes.get(counter);
					if (hshMConnectedNodes.get(counter).searchNode(edge.get(x).getNodeTo()))
						to = hshMConnectedNodes.get(counter);
				}
				// If both don't have any connection, create a new hshMConnectedNodes and add
				// both
				// node to the list.

				if (from == null && to == null) {
					obj.hmConnectedNodesObj.put(edge.get(x).getNodeFrom().getIndex(), edge.get(x).getNodeFrom());
					obj.hmConnectedNodesObj.put(edge.get(x).getNodeTo().getIndex(), edge.get(x).getNodeTo());
					hshMConnectedNodes.add(obj);
				}
				// If only one of them have connection, combine and merge it.
				else if (from == null)
					to.hmConnectedNodesObj.put(edge.get(x).getNodeFrom().getIndex(), edge.get(x).getNodeFrom());
				else
					from.hmConnectedNodesObj.put(edge.get(x).getNodeTo().getIndex(), edge.get(x).getNodeTo());
			}
			/*
			 * If both nodes have connection, check for a loop by calling isLoop function.
			 */
			else {
				// check if there is a loop in the current node
				for (int counter = 0; counter < hshMConnectedNodes.size(); counter++) {
					if (hshMConnectedNodes.get(counter).searchNode(edge.get(x).getNodeTo()))
						toSet = hshMConnectedNodes.get(counter);
				}
				if (toSet.hmConnectedNodesObj.containsValue(edge.get(x).getNodeFrom())) {
					loop = true;
				} else
					loop = false;

				// if no calls addConnectedEdge method and add to arryConnectedEdges for both of
				// the nodes
				if (loop == false) {
					addConnectedEdge();
					for (int counter = 0; counter < hshMConnectedNodes.size(); counter++) {
						if (hshMConnectedNodes.get(counter).searchNode(edge.get(x).getNodeFrom()))
							from = hshMConnectedNodes.get(counter);
						if (hshMConnectedNodes.get(counter).searchNode(edge.get(x).getNodeTo()))
							to = hshMConnectedNodes.get(counter);
						;
					}

					obj.hmConnectedNodesObj.putAll(to.hmConnectedNodesObj);
					obj.hmConnectedNodesObj.putAll(from.hmConnectedNodesObj);
					hshMConnectedNodes.remove(to);
					hshMConnectedNodes.remove(from);
					hshMConnectedNodes.add(obj);
				}
			}
			edge.remove(x);
		}
	}

	public void addConnectedEdge() {
		edge.get(x).getNodeFrom().addArryConnectedEdge(j);
		if (j > 2) {
			i = j - 3;
		} else {
			i = j + 3;
		}
		edge.get(x).getNodeTo().addArryConnectedEdge(i);
		edge.get(x).getNodeFrom().getCell().wall[j].drawn = false;
		edge.get(x).getNodeFrom().getCell().wall[j].present = false;
	}

	//create nodes for normal maze
	public void createNodesNormal(Maze input) {
		for (int counter1 = 0; counter1 < input.sizeR; counter1++) {
			for (int counter2 = 0; counter2 < input.sizeC; counter2++) {
				node.add(new CellStorage(input.map[counter1][counter2], 0));
			}
		}
	}

	//create nodes for tunnel maze
	public void createNodesTunnel(Maze input) {
		for (int i = 0; i < input.sizeR; i++) {
			for (int j = 0; j < input.sizeC; j++) {
				node.add(new CellStorage(input.map[i][j], 1));
			}
		}
	}

	public void createEdge(Maze input) {
		for (CellStorage i : node) {
			// making new edge for each different direction
			for (int h = 0; h < input.NUM_DIR; h++) {
				if (i.getCell().neigh[h] != null) {
					Cell destCell = i.getCell().neigh[h];
					CellStorage destNode = null;
					// Looking for the destinated node to store EdgeStorage class
					for (int j = 0; j < node.size(); j++) {
						if (node.get(j).getCell() == destCell) {
							destNode = node.get(j);
							break;
						}
					}
					if (destNode != null) {
						EdgeStorage setEdges = new EdgeStorage(i, destNode, h);
						edge.add(setEdges);
						i.addArryEdge(setEdges, h);
					}
				}
			}
			if (input.type == 1) {
				tunnel(); // calls tunnel method to create tunnel maze if tunnel maze is chosen
			}
		}
	}

	// tunnel method to generate tunnel in maze
	public void tunnel() {
		boolean status = true;
		for (CellStorage i : node) {
			if (i.getCell().tunnelTo != null) {
				CellStorage tunnelTo = null;
				// Searching for destinated node from the tunnel
				for (int k = 0; k < node.size(); k++) {
					if (node.get(k).getCell() == i.getCell().tunnelTo) {
						tunnelTo = node.get(k);
						break;
					}
				}
				i.setTunnel(tunnelTo);

				// Calling validateConnection method to make sure that no duplicated connection
				// is made.
				// Create new connection if no duplicate.

				status = validateConnection(i, tunnelTo);
				if (status == false) {
					HashMapConnectedNodes tunnelSet = new HashMapConnectedNodes();
					tunnelSet.hmConnectedNodesObj.put(i.getIndex(), i);
					tunnelSet.hmConnectedNodesObj.put(tunnelTo.getIndex(), tunnelTo);
					hshMConnectedNodes.add(tunnelSet);
				}
			}
		}
	}

	//A method to check if 2 nodes have connection
	public boolean validateConnection(CellStorage from, CellStorage to) {
		HashMapConnectedNodes nodeFrom = null;
		HashMapConnectedNodes nodeTo = null;
		if (hshMConnectedNodes.size() == 0) {
			return false;
		}
		for (int counter = 0; counter < hshMConnectedNodes.size(); counter++) {
			if (hshMConnectedNodes.get(counter).searchNode(from))
				nodeFrom = hshMConnectedNodes.get(counter);
			if (hshMConnectedNodes.get(counter).searchNode(to))
				nodeTo = hshMConnectedNodes.get(counter);
		}
		if (nodeFrom == null || nodeTo == null)
			return false;
		else
			return true;
	}

	// private class to store hash map of connected nodes
	private class HashMapConnectedNodes {
		private HashMap<String, CellStorage> hmConnectedNodesObj = new HashMap<String, CellStorage>();

		public boolean searchNode(CellStorage value) {
			if (hmConnectedNodesObj.containsValue(value))
				return true;
			else
				return false;
		}
	}


} // end of class KruskalGenerator
