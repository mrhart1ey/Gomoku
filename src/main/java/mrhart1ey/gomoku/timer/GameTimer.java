package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

/**
 * The game timer keeps track of how much time a player has left of their turn to
 * make a move on the board.
 * 
 * All instances of a GameTimer should be immutable and thread safe. 
 */
public interface GameTimer {
    /**
     * @param timestamp The current time
     * @return The amount of time that the player has left to make a move, 
     * the duration will always be more than or equal to zero
     */
    public Duration getTimeLeft(Instant timestamp);
    
    /**
     * @return True if the time that is left is 0, false otherwise
     * @param timestamp The current time
     */
    public boolean didTimeRunOut(Instant timestamp);
    
    /**
     * @return True if it is possible for didTimeRunOut to return true, false otherwise
     */
    public boolean isFinite();
}
