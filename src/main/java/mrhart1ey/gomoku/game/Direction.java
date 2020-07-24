package mrhart1ey.gomoku.game;

/**
 * An interface for transforming a position.
 */
public interface Direction {
    /**
     * @param pos The position to apply the function to
     * @return A new position that was transformed from the passed in position
     */
    public Position apply(Position pos);
    
    /**
     * @return The opposite direction to the direction instance it was called on
     */
    public Direction getOpposite();
}
