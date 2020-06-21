package mazeGenerator;

public class EdgeStorage {
    private CellStorage nodeFrom;
    private CellStorage nodeTo;
    private int targetDirection;

    public EdgeStorage(CellStorage nodeFrom, CellStorage nodeTo, int targetDirection) {
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.targetDirection = targetDirection;
    }

    public int getTargetDirection() {
        return targetDirection;
    }

    public CellStorage getNodeFrom() {
        return nodeFrom;
    }

    public CellStorage getNodeTo() {
        return nodeTo;
    }
}