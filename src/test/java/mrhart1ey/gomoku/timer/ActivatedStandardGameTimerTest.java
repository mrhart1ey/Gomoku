package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;
import static org.junit.Assert.*;

public class ActivatedStandardGameTimerTest extends ActivatedGameTimerTest {

    private static final Duration SOME_DURATION = Duration.ofSeconds(10);
    private static final Duration TIME_ADDED_PER_TURN = Duration.ofSeconds(20);

    private final Clock clock = Clock.systemUTC();

    @Override
    protected ActivatedGameTimer newInstance(Duration timeLeft,
            Instant turnStartTimestamp) {
        return new ActivatedStandardGameTimer(timeLeft,
                TIME_ADDED_PER_TURN, turnStartTimestamp);
    }

    @Test
    public void shouldAddTimeToTheClockAfterATurnHasEnded() {
        Instant turnStart = clock.instant();

        Duration initialTimeLeft = Duration.ofSeconds(20);
        
        ActivatedGameTimer sut = newInstance(initialTimeLeft, turnStart);

        Instant endOfTurn = turnStart.plusSeconds(5);

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(endOfTurn);

        Duration timeLeft = deactivatedTimer.getTimeLeft(endOfTurn);
        
        assertEquals(Duration.ofSeconds(35),
                timeLeft);
    }

    @Test
    public void shouldAddTimeToTheClockAfterATurnHasEndedEvenIfNoTimeHasElapsed() {
        Instant turnStart = clock.instant();

        Duration initialTimeLeft = Duration.ofSeconds(20);
        
        ActivatedGameTimer sut = newInstance(initialTimeLeft, turnStart);

        Instant endOfTurn = turnStart;

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(endOfTurn);

        Duration timeLeft = deactivatedTimer.getTimeLeft(endOfTurn);
        
        assertEquals(Duration.ofSeconds(40),
                timeLeft);
    }
    
    @Test
    public void shouldAddTimeToTheClockAfterATurnEvenIfTheTimeRanOutForTheTurn() {
        Instant turnStart = clock.instant();

        Duration initialTimeLeft = Duration.ofSeconds(20);
        
        ActivatedGameTimer sut = newInstance(initialTimeLeft, turnStart);

        Instant endOfTurn = turnStart.plusSeconds(initialTimeLeft.toSeconds());

        DeactivatedGameTimer deactivatedTimer = sut.stopTimingTurn(endOfTurn);

        Duration timeLeft = deactivatedTimer.getTimeLeft(endOfTurn);
        
        assertEquals(Duration.ofSeconds(20),
                timeLeft);
    }

    @Test
    public void shouldBeAFiniteTimer() {
        assertTrue(newInstance(SOME_DURATION, clock.instant()).isFinite());
    }
}
