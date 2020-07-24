package mrhart1ey.gomoku.game;

/**
 * The possible content of a position on a Gomoku board. 
 */
public enum PositionContent {
    FREE(false), BLACK(true), WHITE(true);
    
    private PositionContent(boolean occupied) {
        this.occupied = occupied;
    }
    
    private final boolean occupied;
    
    /**
     * @return True if there is a piece in the position, false otherwise
     */
    public boolean isOccupied() {
        return occupied;
    }
}
