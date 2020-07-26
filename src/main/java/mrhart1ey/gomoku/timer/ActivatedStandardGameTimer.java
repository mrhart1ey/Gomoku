package mrhart1ey.gomoku.timer;

import java.time.Duration;
import java.time.Instant;

final class ActivatedStandardGameTimer implements ActivatedGameTimer {
    private final Duration turnStartReserveTime;
    private final Duration timeAddedPerTurn;

    private final Instant turnStartTimestamp;

    public ActivatedStandardGameTimer(Duration timeLeft,
            Duration timeAddedPerTurn, Instant turnStartTimestamp) {
        this.turnStartReserveTime = timeLeft;
        this.timeAddedPerTurn = timeAddedPerTurn;
        this.turnStartTimestamp = turnStartTimestamp;
    }

    @Override
    public DeactivatedGameTimer stopTimingTurn(Instant timestamp) {
        if (timestamp.isBefore(turnStartTimestamp)) {
            throw new IllegalArgumentException("Passed in timestamp is before "
                    + "the timestamp that the turn started with.");
        }

        Duration newGameTimeLeft = getTimeLeft(timestamp).plus(timeAddedPerTurn);

        return new StandardGameTimer(newGameTimeLeft, timeAddedPerTurn);
    }

    @Override
    public Duration getTimeLeft(Instant timestamp) {
        if (timestamp.isBefore(turnStartTimestamp)) {
            throw new IllegalArgumentException("Passed in timestamp is before "
                    + "the timestamp that the turn started with.");
        }

        Duration timeSinceTurnStart
                = Duration.between(turnStartTimestamp, timestamp);

        Duration turnTimeLeft = turnStartReserveTime.minus(timeSinceTurnStart);

        if (turnTimeLeft.isNegative()) {
            return Duration.ZERO;
        }

        return turnTimeLeft;
    }

    @Override
    public boolean didTimeRunOut(Instant timestamp) {
        return getTimeLeft(timestamp).isZero();
    }

    @Override
    public boolean isFinite() {
        return true;
    }

}
