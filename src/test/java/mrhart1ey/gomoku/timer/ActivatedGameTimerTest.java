package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;

public abstract class ActivatedGameTimerTest {

    private static final Duration SOME_DURATION = Duration.ofSeconds(10);

    private final Clock clock = Clock.systemUTC();

    protected abstract ActivatedGameTimer
            newInstance(Duration timeLeft, Instant turnStartTimestamp);

    @Test
    public void theTimeLeftShouldGoDown() {
        Instant turnStartTimestamp = clock.instant();

        GameTimer sut = newInstance(Duration.ofSeconds(10), turnStartTimestamp);

        Instant time = turnStartTimestamp.plusSeconds(3);

        Duration duration = sut.getTimeLeft(time);

        assertEquals(Duration.ofSeconds(7), duration);
    }

    @Test
    public void shouldNeverReturnANegativeDuration() {
        Instant turnStartTimestamp = clock.instant();

        GameTimer sut = newInstance(Duration.ofSeconds(10), turnStartTimestamp);

        Instant time = turnStartTimestamp.plusSeconds(20);

        Duration duration = sut.getTimeLeft(time);

        assertEquals(Duration.ofSeconds(0), duration);
    }

    @Test
    public void shouldReturnAnDeativatedTimerThatDoesNotKeepTrackOfTime() {
        Instant turnStartTimestamp = clock.instant();

        ActivatedGameTimer sut
                = newInstance(Duration.ofSeconds(10), turnStartTimestamp);

        Instant endOfTurn = turnStartTimestamp.plusSeconds(5);

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(endOfTurn);

        Duration timeLeftAfterTurn = deactivatedTimer.getTimeLeft(endOfTurn);

        Instant afterEndOfTurn = endOfTurn.plusSeconds(5);

        Duration turnTimeLeft = deactivatedTimer.getTimeLeft(afterEndOfTurn);

        assertEquals(timeLeftAfterTurn, turnTimeLeft);
    }

    @Test
    public void theDeactivatedTimerShouldNotAffectTheTimeLeftOnTheActivatedTimer() {
        Instant turnStartTimestamp = clock.instant();

        ActivatedGameTimer sut
                = newInstance(Duration.ofSeconds(10), turnStartTimestamp);

        Instant endOfTurn = turnStartTimestamp.plusSeconds(5);

        sut.stopTimingTurn(endOfTurn);

        Instant afterEndOfTurn = endOfTurn.plusSeconds(2);
        
        assertEquals(Duration.ofSeconds(3), sut.getTimeLeft(afterEndOfTurn));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldsThrowAnExceptionIfThePassedInTimestampWhenGettingTheTimeLeftIsBeforeTheTurnStarted() {
        Instant turnStartTimestamp = clock.instant();

        GameTimer sut = newInstance(SOME_DURATION, turnStartTimestamp);

        Instant beforeTurnStart = turnStartTimestamp.minusSeconds(1);

        sut.getTimeLeft(beforeTurnStart);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldsThrowAnExceptionIfThePassedInTimestampWhenSeeingIfTimeRanOutIsBeforeTheTurnStarted() {
        Instant turnStartTimestamp = clock.instant();

        GameTimer sut = newInstance(SOME_DURATION, turnStartTimestamp);

        Instant beforeTurnStart = turnStartTimestamp.minusSeconds(1);

        sut.didTimeRunOut(beforeTurnStart);
    }
}
