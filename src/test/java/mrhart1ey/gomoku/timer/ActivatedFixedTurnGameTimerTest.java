package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;

public class ActivatedFixedTurnGameTimerTest extends ActivatedGameTimerTest {

    private static final Duration SOME_DURATION = Duration.ofSeconds(10);

    private final Clock clock = Clock.systemUTC();

    @Override
    protected ActivatedGameTimer newInstance(Duration timeLeft,
            Instant turnStartTimestamp) {
        return new ActivatedFixedTurnGameTimer(timeLeft,
                turnStartTimestamp);
    }

    @Test
    public void shouldResetTheTimerOnTheClockToTheTimePerTurnWhenItStopsTimingATurn() {
        Instant turnStart = clock.instant();

        ActivatedGameTimer sut = newInstance(Duration.ofSeconds(20), turnStart);

        Duration timeAllowedPerTurn = sut.getTimeLeft(turnStart);

        Instant endOfTurn = turnStart.plusSeconds(5);

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(endOfTurn);

        assertEquals(deactivatedTimer.getTimeLeft(endOfTurn),
                timeAllowedPerTurn);
    }

    @Test
    public void shouldResetTheTimerOnTheClockToTheTimePerTurnWhenItStopsTimingATurnEvenIfNoTimeHasElapsed() {
        Instant turnStart = clock.instant();

        ActivatedGameTimer sut = newInstance(SOME_DURATION, turnStart);

        Duration timeAllowedPerTurn = sut.getTimeLeft(turnStart);

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(turnStart);

        assertEquals(deactivatedTimer.getTimeLeft(turnStart),
                timeAllowedPerTurn);
    }

    @Test
    public void shouldBeAFiniteTimer() {
        assertTrue(newInstance(SOME_DURATION, clock.instant()).isFinite());
    }
}
