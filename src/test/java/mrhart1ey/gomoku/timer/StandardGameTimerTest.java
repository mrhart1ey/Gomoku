package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

public class StandardGameTimerTest extends DeactivatedGameTimerTest {

    private static final Duration SOME_DURATION = Duration.ofSeconds(10);

    private final Clock clock = Clock.systemUTC();

    @Override
    protected DeactivatedGameTimer newInstance(Duration timeLeft) {
        return new StandardGameTimer(timeLeft,
                Duration.ofSeconds(20));
    }

    @Test
    public void durationPassedToTheConstructorShouldBeTheTimeThatIsLeft() {
        GameTimer sut = newInstance(SOME_DURATION);

        assertEquals(SOME_DURATION, sut.getTimeLeft(clock.instant()));
    }

    @Test
    public void shouldAllowATimerWithNoTimeLeft() {
        GameTimer sut = newInstance(Duration.ZERO);

        assertEquals(Duration.ZERO, sut.getTimeLeft(clock.instant()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowATimerWithANegativeTurnTimeLeft() {
        Duration negativeTurnTime = Duration.ZERO.minusSeconds(10);

        GameTimer sut = newInstance(negativeTurnTime);
    }

    @Test
    public void shouldAllowTheTimeAddedPerTurnToBeZero() {
        StandardGameTimer sut = new StandardGameTimer(SOME_DURATION,
                Duration.ZERO);

        assertEquals(Duration.ZERO, sut.getTimeAddedPerTurn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowTheTimeAddedPerTurnToBeNegative() {
        Duration negativeTimeAddedPerTurn = Duration.ZERO.minusSeconds(10);

        StandardGameTimer sut = new StandardGameTimer(SOME_DURATION,
                negativeTimeAddedPerTurn);
    }

    @Test
    public void shouldBeAFiniteTimer() {
        assertTrue(newInstance(SOME_DURATION).isFinite());
    }

}
