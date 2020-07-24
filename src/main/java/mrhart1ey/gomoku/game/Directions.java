package mrhart1ey.gomoku.game;

/**
 * A collection of directions, that represent the 8 standard points on a compass.
 */
public enum Directions implements Direction {
    NORTH(-1, 0), NORTH_EAST(-1, 1), EAST(0, 1), SOUTH_EAST(1, 1), SOUTH(1, 0), 
    SOUTH_WEST(1, -1), WEST(0, -1), NORTH_WEST(-1, -1);
    
    private final int changeInRow;
    private final int changeInColumn;
    
    private Direction opposite;
    
    static {
        NORTH.opposite = SOUTH;
        NORTH_EAST.opposite = SOUTH_WEST;
        EAST.opposite = WEST;
        SOUTH_EAST.opposite = NORTH_WEST;
        SOUTH.opposite = NORTH;
        SOUTH_WEST.opposite = NORTH_EAST;
        WEST.opposite = EAST;
        NORTH_WEST.opposite = SOUTH_EAST;
    }
    
    private Directions(int changeInRow, int changeInColumn) {
        this.changeInRow = changeInRow;
        this.changeInColumn = changeInColumn;
    }

    @Override
    public Position apply(Position pos) {
        return new Position(pos.row + changeInRow, pos.column + changeInColumn);
    }
    
    @Override
    public Direction getOpposite() {
        return opposite;
    }
}
