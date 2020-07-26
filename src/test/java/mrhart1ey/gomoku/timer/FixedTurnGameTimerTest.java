/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrhart1ey.gomoku.timer;

import java.time.Clock;
import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

public class FixedTurnGameTimerTest extends DeactivatedGameTimerTest {
    private static final Duration SOME_DURATION = Duration.ofSeconds(10);
    
    private final Clock clock = Clock.systemUTC();
    
    @Override
    protected DeactivatedGameTimer newInstance(Duration timeLeft) {
        return new FixedTurnGameTimer(timeLeft);
    }
    
    @Test
    public void durationPassedToTheConstructorShouldBeTheTimeThatIsLeft() {
        GameTimer sut = newInstance(SOME_DURATION);
        
        assertEquals(SOME_DURATION, sut.getTimeLeft(clock.instant()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowATimerWithATurnTimeOfZero() {
        GameTimer sut = newInstance(Duration.ZERO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowATimerWithANegativeTurnTime() {
        Duration negativeTurnTime = Duration.ZERO.minusSeconds(10);
        
        GameTimer sut = newInstance(negativeTurnTime);
    }
    
    @Test
    public void shouldBeAFiniteTimer() {
        assertTrue(newInstance(SOME_DURATION).isFinite());
    }
}
