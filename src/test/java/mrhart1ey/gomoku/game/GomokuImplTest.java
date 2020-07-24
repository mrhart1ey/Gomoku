package mrhart1ey.gomoku.game;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class GomokuImplTest extends GomokuTest {

    @Override
    protected Gomoku newInstance() {
        return new GomokuImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsRowNumberIsBellowZeroWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(-1, 7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsRowNumberIsMoreThanTheBoardsSizeWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(19, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsColumnNumberIsBellowZeroWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(7, -3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsColumnNumberIsMoreThanTheBoardsSizeWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(7, 19));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsComponentsAreTooLowWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(-1, -2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsComponentsAreMoreThanTheBoardsSizeWhenGettingThePositionContent() {
        newInstance().getPositionContent(new Position(19, 20));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsRowNumberIsBellowZeroWhenPlacingAPiece() {
        newInstance().move(new Position(-1, 7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsRowNumberIsMoreThanTheBoardsSizeWhenPlacingAPiece() {
        newInstance().move(new Position(19, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsColumnNumberIsBellowZeroWhenPlacingAPiece() {
        newInstance().move(new Position(7, -3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsColumnNumberIsMoreThanTheBoardsSizeWhenPlacingAPiece() {
        newInstance().move(new Position(7, 19));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsComponentsAreTooLowWhenPlacingAPiece() {
        newInstance().move(new Position(-1, -2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfThePositionsComponentsAreMoreThanTheBoardsSizeWhenPlacingAPiece() {
        newInstance().move(new Position(19, 20));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfAPieceIsPlacedOnAnExistingPieceOfTheSameColour() {
        newInstance()
                .move(new Position(0, 0)).move(new Position(0, 1))
                .move(new Position(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionIfAPieceIsPlacedOnAnExistingPieceOfADifferentColour() {
        newInstance()
                .move(new Position(0, 0)).move(new Position(0, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfWhiteTriesToPlaceAPieceAfterBlackHasWonTheSame() {
        newInstance()
                .move(new Position(0, 1)).move(new Position(5, 5))
                .move(new Position(0, 2)).move(new Position(6, 6))
                .move(new Position(0, 3)).move(new Position(7, 7))
                .move(new Position(0, 4)).move(new Position(8, 8))
                .move(new Position(0, 5)).move(new Position(9, 9));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfBlackTriesToPlaceAPieceAfterWhiteHasWonTheSame() {
        newInstance()
                .move(new Position(0, 1)).move(new Position(5, 5))
                .move(new Position(0, 2)).move(new Position(6, 6))
                .move(new Position(0, 3)).move(new Position(7, 7))
                .move(new Position(0, 4)).move(new Position(8, 8))
                .move(new Position(0, 10)).move(new Position(9, 9))
                .move(new Position(0, 11));
    }
    
    @Test
    public void shouldBeEqualToItself() {
        Gomoku sut = newInstance();
        
        assertEquals(sut, sut);
    }
    
    @Test
    public void shouldNotBeEqualToNull() {
        Gomoku sut = newInstance();
        
        assertNotEquals(sut, null);
    }
    
    @Test
    public void shouldNotBeEqualToSomeNonRelatedObject() {
        Gomoku sut = newInstance();
        
        List<Integer> someOtherObject = List.of(1, 2);
        
        assertNotEquals(sut, someOtherObject);
    }
    
    @Test
    public void shouldBeEqualWhenTheContentIsTheSame() {
        Gomoku sut1 = newInstance()
                .move(new Position(5, 7)).move(new Position(6, 7));
        
        Gomoku sut2 = newInstance()
                .move(new Position(5, 7)).move(new Position(6, 7));
        
        assertEquals(sut1, sut2);
    }
    
    @Test
    public void shouldNotBeEqualWhenTheContentIsTheSame() {
        Gomoku sut1 = newInstance()
                .move(new Position(5, 7)).move(new Position(6, 7));
        
        Gomoku sut2 = newInstance()
                .move(new Position(5, 7)).move(new Position(8, 8));
        
        assertNotEquals(sut1, sut2);
    }
    
    @Test
    public void shouldNotBeEqualToTheBoardThatItWasDerivedFrom() {
        Gomoku sut1 = newInstance().move(new Position(5, 7));
        
        Gomoku sut2 = sut1.move(new Position(8, 8));
        
        assertNotEquals(sut1, sut2);
    }
}
