package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

/**
 * A timer that starts with an amount of time, and on completion of a turn will 
 * have an amount of time added to the timer.
 * 
 * For the standard timer each player should have their own instance, otherwise
 * they would be sharing the amount of time left.
 * 
 * When time is "added" to a timer on completion of a turn, the added time
 * will be in the new timer object, as all timers are immutable. 
 * 
 * For example if the timer start with 300 seconds, and on completion of a turn 
 * 20 seconds are added on, here is a run through of 3 turns that the timer times.
 * 
 * Turn        Time taken for player to decide on a move       Time on the timer
 * 1           15                                              305
 * 2           40                                              285
 * 3           10                                              295
 */
public final class StandardGameTimer implements DeactivatedGameTimer {
    private final Duration timeLeft;
    private final Duration timeAddedPerTurn;
    
    /**
     * Creates a new timer that has an initial amount of time on it(reserveTime),
     * and that gets time added onto it after a turn(timeAddedPerTurn).
     * 
     * @param reserveTime The amount of time on the timer
     * @param timeAddedPerTurn The amount of time added to the timer on completion
     * of a turn
     */
    public StandardGameTimer(Duration reserveTime, Duration timeAddedPerTurn) {
        if(reserveTime.isNegative()) {
            throw new IllegalArgumentException("The reserve time can not be negative");
        }else if(timeAddedPerTurn.isNegative()) {
            throw new IllegalArgumentException("The time added per turn can not be negative");
        }
        
        this.timeLeft = reserveTime;
        this.timeAddedPerTurn = timeAddedPerTurn;
    }
    
    @Override
    public ActivatedGameTimer startTimingTurn(Instant timestamp) {
        return new ActivatedStandardGameTimer(timeLeft, 
                timeAddedPerTurn, timestamp);
    }

    @Override
    public Duration getTimeLeft(Instant timestamp) {
        return timeLeft;
    }

    @Override
    public boolean didTimeRunOut(Instant timestamp) {
        return getTimeLeft(timestamp).isZero();
    }

    
    /**
     * If the time left reaches zero then didTimeRunOut will return true, so it is
     * not an infinite timer.
     * 
     * @return True
     */
    @Override
    public boolean isFinite() {
        return true;
    }
    
    /**
     * @return The amount of time added after each turn is completed
     */
    public Duration getTimeAddedPerTurn() {
        return timeAddedPerTurn;
    }
    
}
