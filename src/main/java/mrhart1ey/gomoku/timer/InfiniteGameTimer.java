package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

/**
 * A timer that will never run out of time.
 * 
 * The passed in timestamps can be null, as they are never used
 */
public final class InfiniteGameTimer implements
        ActivatedGameTimer, DeactivatedGameTimer {
    
    /**
     * Creates a new infinite game timer
     */
    public InfiniteGameTimer() {
        
    }
    
    /**
     * @param timestamp The current time
     * @return An InfiniteGameTimer object
     */
    @Override
    public ActivatedGameTimer startTimingTurn(Instant timestamp) {
        return this;
    }

    /**
     * @param timestamp The current time
     * @return An InfiniteGameTimer object
     */
    @Override
    public DeactivatedGameTimer stopTimingTurn(Instant timestamp) {
        return this;
    }

    /**
     * The returned duration would be infinite, so a UnsupportedOperationException
     * exception is thrown
     * 
     * @param timestamp The current time
     * @throws UnsupportedOperationException When called
     * @return It will never return a value
     */
    @Override
    public Duration getTimeLeft(Instant timestamp) {
        throw new UnsupportedOperationException("Infinite time left");
    }

    /**
     * Time never runs out for this timer
     * 
     * @param timestamp The current time
     * @return false
     */
    @Override
    public boolean didTimeRunOut(Instant timestamp) {
        return false;
    }
    
    /**
     * @return false
     */
    @Override
    public boolean isFinite() {
        return false;
    }

}
