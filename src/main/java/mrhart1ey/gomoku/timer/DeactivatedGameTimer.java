package mrhart1ey.gomoku.timer;

import java.time.Instant;

/**
 * A deactivated game timer is not keeping track of time.
 * 
 * While a timer is deactivated getTimeLeft, and didTimeRunOut will continue
 * to return the same values, so the passed in timestamps are not used.
 */
public interface DeactivatedGameTimer extends GameTimer {
    
    /**
     * @param timestamp The timestamp of when the returned activated timer 
     * started keeping track of time again
     * @return A timer that will be keeping track of time
     */
    public ActivatedGameTimer startTimingTurn(Instant timestamp);
}
