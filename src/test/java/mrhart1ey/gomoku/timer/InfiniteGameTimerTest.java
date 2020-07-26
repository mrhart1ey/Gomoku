package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Instant;
import static org.mockito.Mockito.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class InfiniteGameTimerTest {

    private final Clock clock = Clock.systemUTC();

    @Test
    public void timerShouldNotBeFinite() {
        GameTimer sut = new InfiniteGameTimer();

        assertFalse(sut.isFinite());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowAnExceptionWhenGettingTheTimeLeft() {
        GameTimer sut = new InfiniteGameTimer();

        sut.getTimeLeft(clock.instant());
    }

    @Test
    public void shouldNeverAcessThePassedInTimestampWhenDeactivated() {
        GameTimer sut = new InfiniteGameTimer();

        Instant timestamp = mock(Instant.class);

        sut.didTimeRunOut(timestamp);

        verifyNoInteractions(timestamp);
    }

    @Test
    public void shouldNeverAcessThePassedInTimestampWhenActivated() {
        Instant creationTimestamp = mock(Instant.class);

        GameTimer sut = new InfiniteGameTimer().startTimingTurn(creationTimestamp);

        verifyNoInteractions(creationTimestamp);
    }

    @Test
    public void shouldNeverAcessThePassedInTimestampWhenSeeingIfTimeRanOutWhenActive() {
        Instant creationTimestamp = clock.instant();

        GameTimer sut = new InfiniteGameTimer().startTimingTurn(creationTimestamp);

        Instant timestamp = mock(Instant.class);

        sut.didTimeRunOut(timestamp);

        verifyNoInteractions(timestamp);
    }

    @Test()
    public void shouldNotRunOutOfTimeWhenDeactivated() {
        GameTimer sut = new InfiniteGameTimer();

        assertFalse(sut.didTimeRunOut(clock.instant()));
    }

    @Test()
    public void shouldNotRunOutOfTimeWhenActivated() {
        GameTimer sut = new InfiniteGameTimer().startTimingTurn(clock.instant());

        assertFalse(sut.didTimeRunOut(clock.instant()));
    }
}
