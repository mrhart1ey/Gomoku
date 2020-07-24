package mrhart1ey.gomoku.game;

import org.junit.Test;
import static org.junit.Assert.*;

public abstract class GomokuTest {

    private static final Position SOME_POSITION1 = new Position(5, 6);
    private static final Position SOME_POSITION2 = new Position(13, 12);

    protected abstract Gomoku newInstance();

    @Test
    public void gameStateShouldBeOngoingAtCreation() {
        Gomoku sut = newInstance();

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void itShouldBeBlacksTurnAtCreation() {
        Gomoku sut = newInstance();

        assertEquals(PlayerName.BLACK, sut.getCurrentTurn());
    }

    @Test
    public void placingAPieceShouldAddItToThePassedInLocation() {
        Gomoku sut = newInstance();

        Gomoku sut2 = sut.move(SOME_POSITION1);

        assertTrue(sut2.getPositionContent(SOME_POSITION1).isOccupied());
    }

    @Test
    public void placingAPieceShouldNotAffectTheOldBoardInstance() {
        Gomoku sut = newInstance();

        sut.move(SOME_POSITION1);

        assertFalse(sut.getPositionContent(SOME_POSITION1).isOccupied());
    }

    @Test
    public void itShouldBeWhitesTurnAfterBlack() {
        Gomoku sut = newInstance();

        Gomoku sut2 = sut.move(SOME_POSITION1);

        assertEquals(PlayerName.WHITE, sut2.getCurrentTurn());
    }

    @Test
    public void itShouldBeBlacksTurnAfterWhite() {
        Gomoku sut = newInstance();

        Gomoku sut2 = sut.move(SOME_POSITION1);

        Gomoku sut3 = sut2.move(SOME_POSITION2);

        assertEquals(PlayerName.BLACK, sut3.getCurrentTurn());
    }

    @Test
    public void ifItIsBlacksTurnThePlacedPieceShouldBeBlack() {
        Gomoku sut = newInstance();

        Gomoku sut2 = sut.move(SOME_POSITION1);

        assertEquals(PositionContent.BLACK, sut2.getPositionContent(SOME_POSITION1));
    }

    @Test
    public void ifItIsWhitesTurnThePlacedPieceShouldBeWhite() {
        Gomoku sut = newInstance();

        Gomoku sut2 = sut.move(SOME_POSITION1);

        Gomoku sut3 = sut2.move(SOME_POSITION2);

        assertEquals(PositionContent.WHITE, sut3.getPositionContent(SOME_POSITION2));
    }

    @Test
    public void shouldBeAbleToWinWith5ConsecutiveAdjacentPiecesOfTheSameColour() {
        Gomoku sut = newInstance()
                .move(new Position(1, 1)).move(new Position(7, 7))
                .move(new Position(2, 1)).move(new Position(7, 8))
                .move(new Position(3, 1)).move(new Position(7, 9))
                .move(new Position(4, 1)).move(new Position(7, 10))
                .move(new Position(5, 1));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalOpenEndedLineWhenTheLastPieceWasPlacedAtTheEnd() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(7, 7))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(7, 3)).move(new Position(7, 9))
                .move(new Position(8, 3)).move(new Position(7, 10))
                .move(new Position(9, 3));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalOpenEndedLineWhenTheLastPieceWasPlacedInTheMiddle() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(7, 7))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(9, 3)).move(new Position(7, 9))
                .move(new Position(8, 3)).move(new Position(7, 10))
                .move(new Position(7, 3));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalLineWereAnOpponentsPieceIsAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(7, 7))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(7, 3)).move(new Position(7, 9))
                .move(new Position(8, 3)).move(new Position(10, 3))
                .move(new Position(9, 3));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalLineWereAnOpponentsPieceIsAtBothEnds() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(4, 3))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(7, 3)).move(new Position(7, 9))
                .move(new Position(8, 3)).move(new Position(10, 3))
                .move(new Position(9, 3));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalLineWereTheLineIsAtTheTopOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(0, 3)).move(new Position(9, 9))
                .move(new Position(1, 3)).move(new Position(7, 8))
                .move(new Position(2, 3)).move(new Position(7, 9))
                .move(new Position(3, 3)).move(new Position(10, 3))
                .move(new Position(4, 3));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalLineWereTheLineIsAgainstTheSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(4, 0)).move(new Position(9, 9))
                .move(new Position(5, 0)).move(new Position(7, 8))
                .move(new Position(6, 0)).move(new Position(7, 9))
                .move(new Position(7, 0)).move(new Position(10, 3))
                .move(new Position(8, 0));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }
}
