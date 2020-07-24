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
    public void boardShouldBeBlankAtCreration() {
        Gomoku sut = newInstance();
        
        for(int row = 0; row < Gomoku.SIZE; row++) {
            for(int column = 0; column < Gomoku.SIZE; column++) {
                assertTrue(!sut.getPositionContent(
                        new Position(row, column)).isOccupied());
            }
        }
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
    public void theWholeBoardShouldBeCopiedWhenPlacingAPiece() {
        Position topLeft = new Position(0, 0);
        Position topRight = new Position(0, 18);
        Position bottomLeft = new Position(18, 0);
        Position bottomRight = new Position(18, 18);
        
        Gomoku sut = newInstance()
                .move(topLeft).move(topRight)
                .move(bottomLeft).move(bottomRight);

        assertTrue(sut.getPositionContent(topLeft).isOccupied());
        assertTrue(sut.getPositionContent(topRight).isOccupied());
        assertTrue(sut.getPositionContent(bottomLeft).isOccupied());
        assertTrue(sut.getPositionContent(bottomRight).isOccupied());
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
    public void blackShouldBeAbleToWinAGame() {
        Gomoku sut = newInstance()
                .move(new Position(5, 1)).move(new Position(7, 7))
                .move(new Position(6, 1)).move(new Position(7, 8))
                .move(new Position(7, 1)).move(new Position(7, 9))
                .move(new Position(8, 1)).move(new Position(7, 10))
                .move(new Position(9, 1));

        assertEquals(GameState.VICTORY, sut.getGameState());
        assertEquals(PlayerName.WHITE, sut.getCurrentTurn());
    }

    @Test
    public void whiteShouldBeAbleToWinAGame() {
        Gomoku sut = newInstance()
                .move(new Position(5, 1)).move(new Position(7, 7))
                .move(new Position(6, 1)).move(new Position(7, 8))
                .move(new Position(7, 1)).move(new Position(7, 9))
                .move(new Position(8, 1)).move(new Position(7, 10))
                .move(new Position(17, 17)).move(new Position(7, 11));

        assertEquals(GameState.VICTORY, sut.getGameState());
        assertEquals(PlayerName.BLACK, sut.getCurrentTurn());
    }

    @Test
    public void shouldBeAbleToWinWithAVerticalOpenEndedLineWhenTheLastPieceWasPlacedAtOneEnd() {
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

    @Test
    public void shouldBeAbleToWinWithAVerticalLineThatEndsUpConnectingMoreThan5ConsecutivePieces() {
        Gomoku sut = newInstance()
                .move(new Position(4, 0)).move(new Position(9, 9))
                .move(new Position(5, 0)).move(new Position(7, 8))
                .move(new Position(6, 0)).move(new Position(7, 9))
                .move(new Position(7, 0)).move(new Position(10, 3))
                .move(new Position(9, 0)).move(new Position(18, 18))
                .move(new Position(8, 0));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalOpenEndedLineWhenTheLastPieceWasPlacedAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 7)).move(new Position(7, 9))
                .move(new Position(3, 8)).move(new Position(7, 10))
                .move(new Position(3, 9));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalOpenEndedLineWhenTheLastPieceWasPlacedInTheMiddle() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 9)).move(new Position(7, 9))
                .move(new Position(3, 8)).move(new Position(7, 10))
                .move(new Position(3, 7));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalLineWereAnOpponentsPieceIsAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(3, 4))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 9)).move(new Position(7, 9))
                .move(new Position(3, 8)).move(new Position(7, 10))
                .move(new Position(3, 7));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalLineWereAnOpponentsPieceIsAtBothEnds() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(3, 4))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 9)).move(new Position(7, 9))
                .move(new Position(3, 8)).move(new Position(3, 10))
                .move(new Position(3, 7));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalLineWereTheLineIsAtTheSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(3, 0)).move(new Position(3, 9))
                .move(new Position(3, 1)).move(new Position(7, 8))
                .move(new Position(3, 2)).move(new Position(7, 9))
                .move(new Position(3, 3)).move(new Position(3, 10))
                .move(new Position(3, 4));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalLineWereTheLineIsAgainstTheTopOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(0, 3)).move(new Position(3, 9))
                .move(new Position(0, 4)).move(new Position(7, 8))
                .move(new Position(0, 5)).move(new Position(7, 9))
                .move(new Position(0, 6)).move(new Position(3, 10))
                .move(new Position(0, 7));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithAHorizontalLineThatEndsUpConnectingMoreThan5ConsecutivePieces() {
        Gomoku sut = newInstance()
                .move(new Position(0, 3)).move(new Position(3, 9))
                .move(new Position(0, 4)).move(new Position(7, 8))
                .move(new Position(0, 5)).move(new Position(7, 9))
                .move(new Position(0, 6)).move(new Position(3, 10))
                .move(new Position(0, 8)).move(new Position(17, 17))
                .move(new Position(0, 7));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalOpenEndedLineWhenTheLastPieceWasPlacedAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(4, 6)).move(new Position(7, 8))
                .move(new Position(5, 7)).move(new Position(7, 13))
                .move(new Position(6, 8)).move(new Position(7, 10))
                .move(new Position(7, 9));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalOpenEndedLineWhenTheLastPieceWasPlacedInTheMiddle() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(7, 9)).move(new Position(7, 8))
                .move(new Position(5, 7)).move(new Position(7, 13))
                .move(new Position(6, 8)).move(new Position(7, 10))
                .move(new Position(4, 6));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineWereAnOpponentsPieceIsAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(7, 9)).move(new Position(7, 8))
                .move(new Position(5, 7)).move(new Position(7, 13))
                .move(new Position(6, 8)).move(new Position(2, 4))
                .move(new Position(4, 6));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineWereAnOpponentsPieceIsAtBothEnds() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(7, 9)).move(new Position(8, 10))
                .move(new Position(5, 7)).move(new Position(7, 13))
                .move(new Position(6, 8)).move(new Position(2, 4))
                .move(new Position(4, 6));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineWereItStartsOnTheLeftSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(5, 0)).move(new Position(7, 7))
                .move(new Position(6, 1)).move(new Position(8, 10))
                .move(new Position(7, 2)).move(new Position(7, 13))
                .move(new Position(8, 3)).move(new Position(2, 4))
                .move(new Position(9, 4));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineWereItStartsOnTheTopSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(0, 9)).move(new Position(7, 7))
                .move(new Position(1, 10)).move(new Position(8, 10))
                .move(new Position(2, 11)).move(new Position(7, 13))
                .move(new Position(3, 12)).move(new Position(2, 4))
                .move(new Position(4, 13));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineWereItIsBetweenBothSidesOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(14, 0)).move(new Position(7, 7))
                .move(new Position(15, 1)).move(new Position(8, 10))
                .move(new Position(16, 2)).move(new Position(7, 13))
                .move(new Position(17, 3)).move(new Position(2, 4))
                .move(new Position(18, 4));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithALeftDiagonalLineThatEndsUpConnectingMoreThan5ConsecutivePieces() {
        Gomoku sut = newInstance()
                .move(new Position(5, 0)).move(new Position(7, 7))
                .move(new Position(6, 1)).move(new Position(8, 10))
                .move(new Position(7, 2)).move(new Position(7, 13))
                .move(new Position(8, 3)).move(new Position(2, 4))
                .move(new Position(10, 5)).move(new Position(10, 10))
                .move(new Position(9, 4));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalOpenEndedLineWhenTheLastPieceWasPlacedAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(8, 12)).move(new Position(7, 14))
                .move(new Position(7, 13)).move(new Position(2, 4))
                .move(new Position(6, 14));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalOpenEndedLineWhenTheLastPieceWasPlacedInTheMiddle() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(6, 14)).move(new Position(7, 14))
                .move(new Position(7, 13)).move(new Position(2, 4))
                .move(new Position(8, 12));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineWereAnOpponentsPieceIsAtOneEnd() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(6, 14)).move(new Position(11, 9))
                .move(new Position(7, 13)).move(new Position(2, 4))
                .move(new Position(8, 12));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineWereAnOpponentsPieceIsAtBothEnds() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(6, 14)).move(new Position(11, 9))
                .move(new Position(7, 13)).move(new Position(5, 15))
                .move(new Position(8, 12));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineWereItStartsOnTheRightSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(8, 18)).move(new Position(7, 7))
                .move(new Position(9, 17)).move(new Position(8, 10))
                .move(new Position(10, 16)).move(new Position(11, 9))
                .move(new Position(11, 15)).move(new Position(5, 15))
                .move(new Position(12, 14));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineWereItStartsOnTheBottomSideOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(18, 10)).move(new Position(7, 7))
                .move(new Position(17, 11)).move(new Position(8, 10))
                .move(new Position(16, 12)).move(new Position(11, 9))
                .move(new Position(15, 13)).move(new Position(5, 15))
                .move(new Position(14, 14));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineWereItIsBetweenBothSidesOfTheBoard() {
        Gomoku sut = newInstance()
                .move(new Position(18, 14)).move(new Position(7, 7))
                .move(new Position(17, 15)).move(new Position(8, 10))
                .move(new Position(16, 16)).move(new Position(11, 9))
                .move(new Position(15, 17)).move(new Position(5, 15))
                .move(new Position(14, 18));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldBeAbleToWinWithARightDiagonalLineThatEndsUpConnectingMoreThan5ConsecutivePieces() {
        Gomoku sut = newInstance()
                .move(new Position(8, 18)).move(new Position(7, 7))
                .move(new Position(9, 17)).move(new Position(8, 10))
                .move(new Position(13, 13)).move(new Position(11, 9))
                .move(new Position(11, 15)).move(new Position(5, 15))
                .move(new Position(12, 14)).move(new Position(0, 0))
                .move(new Position(10, 16));

        assertEquals(GameState.VICTORY, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinAGameWithLessThan5ConsecutiveAdjacentPiecesOfTheSameColour() {
        Gomoku sut = newInstance()
                .move(new Position(1, 1)).move(new Position(7, 7))
                .move(new Position(2, 1)).move(new Position(7, 8))
                .move(new Position(3, 1)).move(new Position(7, 9))
                .move(new Position(4, 1));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithAVerticalLineIfThereIsAFreePositionBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(7, 7))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(8, 3)).move(new Position(7, 9))
                .move(new Position(9, 3)).move(new Position(7, 10))
                .move(new Position(10, 3));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithAVerticalLineIfThereIsADifferentColouredPieceBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(5, 3)).move(new Position(7, 7))
                .move(new Position(6, 3)).move(new Position(7, 8))
                .move(new Position(8, 3)).move(new Position(7, 9))
                .move(new Position(9, 3)).move(new Position(7, 3))
                .move(new Position(10, 3));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }
    
    @Test
    public void shouldNotBeAbleToWinWithAVerticalLineThatLoopsAroundToTheOtherSideOfTheBoardToMake5Pieces() {
        Gomoku sut = newInstance()
                .move(new Position(0, 5)).move(new Position(7, 7))
                .move(new Position(1, 5)).move(new Position(7, 8))
                .move(new Position(2, 5)).move(new Position(7, 9))
                .move(new Position(3, 5)).move(new Position(7, 10))
                .move(new Position(18, 5));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithAHorizontalLineIfThereIsAFreePositionBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 7)).move(new Position(7, 9))
                .move(new Position(3, 9)).move(new Position(7, 10))
                .move(new Position(3, 10));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithAHorizontalLineIfThereIsADifferentColouredPieceBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(3, 6)).move(new Position(7, 8))
                .move(new Position(3, 7)).move(new Position(7, 9))
                .move(new Position(3, 9)).move(new Position(3, 8))
                .move(new Position(3, 10));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }
    
    @Test
    public void shouldNotBeAbleToWinWithAHorizontalLineThatLoopsAroundToTheOtherSideOfTheBoardToMake5Pieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 0)).move(new Position(7, 7))
                .move(new Position(3, 1)).move(new Position(7, 8))
                .move(new Position(3, 2)).move(new Position(7, 9))
                .move(new Position(3, 3)).move(new Position(7, 10))
                .move(new Position(3, 18));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithALeftDiagonalLineIfThereIsAFreePositionBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(4, 6)).move(new Position(7, 8))
                .move(new Position(6, 8)).move(new Position(7, 13))
                .move(new Position(7, 9)).move(new Position(7, 10))
                .move(new Position(8, 10));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithALeftDiagonalLineIfThereIsADifferentColouredPieceBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 5)).move(new Position(7, 7))
                .move(new Position(4, 6)).move(new Position(7, 8))
                .move(new Position(6, 8)).move(new Position(7, 13))
                .move(new Position(7, 9)).move(new Position(5, 7))
                .move(new Position(8, 10));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }
    
    @Test
    public void shouldNotBeAbleToWinWithALeftDiagonalLineThatLoopsAroundToTheOtherSideOfTheBoardToMake5Pieces() {
        Gomoku sut = newInstance()
                .move(new Position(0, 0)).move(new Position(7, 7))
                .move(new Position(1, 1)).move(new Position(7, 8))
                .move(new Position(2, 2)).move(new Position(7, 13))
                .move(new Position(3, 3)).move(new Position(7, 10))
                .move(new Position(18, 18));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithARightDiagonalLineIfThereIsAFreePositionBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(7, 13)).move(new Position(7, 14))
                .move(new Position(6, 14)).move(new Position(2, 4))
                .move(new Position(5, 15));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }

    @Test
    public void shouldNotBeAbleToWinWithARightDiagonalLineIfThereIsADifferentColouredPieceBetweenThePieces() {
        Gomoku sut = newInstance()
                .move(new Position(10, 10)).move(new Position(7, 7))
                .move(new Position(9, 11)).move(new Position(8, 10))
                .move(new Position(7, 13)).move(new Position(7, 14))
                .move(new Position(6, 14)).move(new Position(8, 12))
                .move(new Position(5, 15));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }
    
    @Test
    public void shouldNotBeAbleToWinWithARightDiagonalLineThatLoopsAroundToTheOtherSideOfTheBoardToMake5Pieces() {
        Gomoku sut = newInstance()
                .move(new Position(3, 18)).move(new Position(7, 7))
                .move(new Position(4, 17)).move(new Position(8, 10))
                .move(new Position(5, 16)).move(new Position(7, 14))
                .move(new Position(6, 15)).move(new Position(2, 4))
                .move(new Position(18, 3));

        assertEquals(GameState.ONGOING, sut.getGameState());
    }
}
