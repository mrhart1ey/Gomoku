package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

final class ActivatedFixedTurnGameTimer implements ActivatedGameTimer {

    private final Duration turnTime;
    private final Instant turnStartTimestamp;

    public ActivatedFixedTurnGameTimer(Duration turnTime,
            Instant turnStartTimestamp) {
        this.turnTime = turnTime;
        this.turnStartTimestamp = turnStartTimestamp;
    }

    @Override
    public DeactivatedGameTimer stopTimingTurn(Instant timestamp) {
        return new FixedTurnGameTimer(turnTime);
    }

    @Override
    public Duration getTimeLeft(Instant timestamp) {
        if (timestamp.isBefore(turnStartTimestamp)) {
            throw new IllegalArgumentException("Passed in timestamp is before "
                    + "the timestamp that the turn started with.");
        }

        Duration timeSinceTurnStart
                = Duration.between(turnStartTimestamp, timestamp);

        Duration turnTimeLeft = turnTime.minus(timeSinceTurnStart);

        if (turnTimeLeft.isNegative()) {
            return Duration.ZERO;
        }

        return turnTimeLeft;
    }

    @Override
    public boolean didTimeRunOut(Instant timeStamp) {
        return getTimeLeft(timeStamp).isZero();
    }

    @Override
    public boolean isFinite() {
        return true;
    }

}
