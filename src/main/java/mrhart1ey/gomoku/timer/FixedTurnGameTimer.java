package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

/**
 * A game timer that gives the same length of time every time for each turn.
 * 
 * It does not matter how much time was on the timer before the timer was told to 
 * stop timing the turn, the amount of time given for the new turn will always be the same.
 */
public final class FixedTurnGameTimer implements DeactivatedGameTimer {
    private final Duration turnTime;

    /**
     * @param turnTime The amount of time that will be allowed for each turn
     * @throws IllegalArgumentException If the turn time is not positive
     */
    public FixedTurnGameTimer(Duration turnTime) {
        if(turnTime.isNegative() || turnTime.isZero()) {
            throw new IllegalArgumentException("The turn time must be positive");
        }
        
        this.turnTime = turnTime;
    }

    @Override
    public ActivatedGameTimer startTimingTurn(Instant timestamp) {
        return new ActivatedFixedTurnGameTimer(turnTime, timestamp);
    }

    @Override
    public Duration getTimeLeft(Instant timeStamp) {
        return turnTime;
    }

    @Override
    public boolean didTimeRunOut(Instant timeStamp) {
        return getTimeLeft(timeStamp).isZero();
    }

    /**
     * If the amount of time given for a turn to be completed passes,
     * then didTimeRunOut will return true.
     * 
     * @return True
     */
    @Override
    public boolean isFinite() {
        return true;
    }
}
