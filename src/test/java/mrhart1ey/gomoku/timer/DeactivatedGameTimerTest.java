package mrhart1ey.gomoku.timer;

import java.time.Instant;
import java.time.Clock;
import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public abstract class DeactivatedGameTimerTest {
    private static final Duration SOME_DURATION = Duration.ofSeconds(10);
    
    private final Clock clock = Clock.systemUTC();

    protected abstract DeactivatedGameTimer newInstance(Duration timeLeft);

    @Test
    public void theTimeLeftShouldNotGoDown() {
        GameTimer sut = newInstance(SOME_DURATION);

        Instant time1 = clock.instant();

        Duration duration1 = sut.getTimeLeft(time1);

        Instant time2 = time1.plusSeconds(5);

        Duration duration2 = sut.getTimeLeft(time2);

        assertEquals(duration1, duration2);
    }
    
    @Test
    public void shouldReturnAnActivatedTimerThatKeepsTrackOfTime() {
        DeactivatedGameTimer sut = newInstance(SOME_DURATION);
        
        Instant turnStartTimestamp = clock.instant();
        
        Duration initialTimeOnTheClock = sut.getTimeLeft(turnStartTimestamp);
        
        ActivatedGameTimer activatedTimer = sut.startTimingTurn(turnStartTimestamp);
        
        Instant someTimeIntoTheTurn = turnStartTimestamp.plusSeconds(1);
        
        Duration turnTimeLeft = activatedTimer.getTimeLeft(someTimeIntoTheTurn);
        
        // turnTimeLeft should be less than initialTimeOnTheClock
        assertTrue(turnTimeLeft.compareTo(initialTimeOnTheClock) == -1);
    }
    
    @Test
    public void theActivatedTimerShouldNotAffectTheTimeLeftOnTheDeactivatedTimer() {
        DeactivatedGameTimer sut = newInstance(SOME_DURATION);
        
        Instant turnStartTimestamp = clock.instant();
        
        Duration initialTimeOnTheClock = sut.getTimeLeft(turnStartTimestamp);
        
        sut.startTimingTurn(turnStartTimestamp);
        
        Instant someTimeIntoTheTurn = turnStartTimestamp.plusSeconds(1);
        
        assertEquals(initialTimeOnTheClock, sut.getTimeLeft(someTimeIntoTheTurn));
    }

    @Test
    public void whenTimeIsLeftOnTheClockItShouldSayTimeHasNotRunOut() {
        GameTimer sut = newInstance(SOME_DURATION);
        
        assertFalse(sut.didTimeRunOut(clock.instant()));
    }

    @Test
    public void shouldNeverAccessThePassedInTimestampForGettingTheTimeLeft() {
        GameTimer sut = newInstance(SOME_DURATION);

        Instant timestamp = mock(Instant.class);

        sut.getTimeLeft(timestamp);

        verifyNoInteractions(timestamp);
    }

    @Test
    public void shouldNeverAccessThePassedInTimestampForSeeingIfTimeRanOut() {
        GameTimer sut = newInstance(SOME_DURATION);

        Instant timestamp = mock(Instant.class);

        sut.didTimeRunOut(timestamp);

        verifyNoInteractions(timestamp);
    }

}
