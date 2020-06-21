package mazeGenerator;

import maze.Cell;

public class CellStorage {

    private String index;
    private Cell cell;
    private int mazeType;
    private CellStorage tunnel;
    private EdgeStorage[] arryEdges = new EdgeStorage[6];
    private EdgeStorage[] arryConnectedEdges = new EdgeStorage[6];
    public CellStorage[] neighbour = new CellStorage[7];
    private CellStorage previous;
    int pathLength = 0;

    public CellStorage(Cell cell){
        this.cell = cell;
        this.index = "[" + cell.c + "," + cell.r + "]";
    }
    public CellStorage(Cell cell, int type) {
        this.cell = cell;
        this.mazeType = type;
        index = "[" + cell.c + "," + cell.r + "]";
    }

    public String getIndex() {
        return index;
    }

    public void addArryEdge(EdgeStorage edge, int dir) {
        arryEdges[dir] = edge;
    }

    public void addArryConnectedEdge(int dir) {
        arryConnectedEdges[dir] = arryEdges[dir];
    }

    public CellStorage getCellNeighbour(int dir){
        return neighbour[dir];
    }
    public void setTunnel(CellStorage node) {
        tunnel = node;
    }
    public void setNeighbourCell(CellStorage cell, int index){
        neighbour[index] = cell;
    }

    public Cell getCell() {
        return cell;
    }
    public CellStorage getPreviousCell(){
        return this.previous;
    }
    public void setPrevious(CellStorage node){
        this.previous = node;
    }
    public void countPath(){
        if(previous != null){
            previous.countPath();
            pathLength++;
        }
    }



}