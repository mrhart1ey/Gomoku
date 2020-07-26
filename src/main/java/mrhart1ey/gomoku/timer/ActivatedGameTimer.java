package mrhart1ey.gomoku.timer;

import java.time.Instant;

/**
 * A timer that is keeping track of time.
 */
public interface ActivatedGameTimer extends GameTimer {
    
    /**
     * @param timestamp The current time
     * @return A timer that is no longer keeping track of time
     */
    public DeactivatedGameTimer stopTimingTurn(Instant timestamp);
}
