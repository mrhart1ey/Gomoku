package mrhart1ey.gomoku.player.ai;

import java.util.Set;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GomokuHeuristicImplTest {

    @Test
    public void shouldGiveAnEmptyBoardAScoreOfZero() {
        Gomoku board = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(board, PlayerName.BLACK);

        assertEquals(0, sut.evaluate(board, Set.of()).getScore());
    }

    @Test
    public void shouldGiveABoardWithOneOfMyOpenPiecesABetterScoreThanIfItHadNone() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMove = mockEmptyBoard();
        Position move = new Position(1, 2);
        when(boardAfterMove.getPositionContent(move))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterMove, Set.of(move)).getScore()
                > sut.evaluate(initialBoard, Set.of()).getScore());
    }

    @Test
    public void shouldGiveABoardWithOneOfMyPartiallyClosedPiecesAWorseScoreThanIfItWasOpen() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMove = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        when(boardAfterMove.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterMoves = mockEmptyBoard();
        Position move2 = new Position(1, 3);
        when(boardAfterMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves.getPositionContent(move2))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves, Set.of(move1, move2)).getScore()
                < sut.evaluate(boardAfterMove, Set.of(move1)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyTwoOpenConsecutivePiecesABetterScoreThanIfItHadOne() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMove = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        when(boardAfterMove.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterTwoMoves = mockEmptyBoard();
        Position move2 = new Position(1, 3);
        when(boardAfterTwoMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterTwoMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterTwoMoves, Set.of(move1, move2)).getScore()
                > sut.evaluate(boardAfterMove, Set.of(move1)).getScore());
    }

    @Test
    public void shouldGiveABoardWithTwoOfMyPartiallyClosedPiecesAWorseScoreThanIfItWasOpen() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move3 = new Position(1, 1);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3)).getScore()
                < sut.evaluate(boardAfterMoves1, Set.of(move1, move2)).getScore());
    }

    @Test
    public void shouldGiveABoardWithTwoOfMyClosedPiecesAWorseScoreThanIfItWasPartiallyClosed() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.WHITE);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move4 = new Position(1, 1);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.WHITE);
        when(boardAfterMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3)).getScore()
                < sut.evaluate(boardAfterMoves1, Set.of(move1, move2)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyTwoConsecutivePiecesABetterScoreThanIfThePiecesAreSpreadOut() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterTwoMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(10, 2);
        when(boardAfterTwoMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterTwoMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterTwoMoves2 = mockEmptyBoard();
        Position move3 = new Position(1, 3);
        when(boardAfterTwoMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterTwoMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterTwoMoves2, Set.of(move1, move3)).getScore()
                > sut.evaluate(boardAfterTwoMoves1, Set.of(move1, move2)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyThreeOpenConsecutivePiecesABetterScoreThanItHadTwo() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterTwoMoves = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        when(boardAfterTwoMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterTwoMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterThreeMoves = mockEmptyBoard();
        Position move3 = new Position(1, 4);
        when(boardAfterThreeMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterThreeMoves, Set.of(move1, move2, move3)).getScore()
                > sut.evaluate(boardAfterTwoMoves, Set.of(move1, move2)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyThreePartiallyClosedConsecutivePiecesAWorseScoreThanItThetWasOpen() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move4 = new Position(1, 5);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves1, Set.of(move1, move2, move3)).getScore()
                > sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3, move4)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyThreeClosedConsecutivePiecesAWorseScoreThanIfItWasPartiallyClosed() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        Position move4 = new Position(1, 5);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move4))
                .thenReturn(PositionContent.WHITE);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move5 = new Position(1, 1);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.WHITE);
        when(boardAfterMoves2.getPositionContent(move5))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves1, Set.of(move1, move2, move3, move4)).getScore()
                > sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3, move4, move5)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyThreeConsecutivePiecesABetterScoreThanIfThePiecesAreSpreadOut() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterThreeMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 12);
        when(boardAfterThreeMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterThreeMoves2 = mockEmptyBoard();
        Position move4 = new Position(1, 4);
        when(boardAfterThreeMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterThreeMoves2, Set.of(move1, move2, move3, move4)).getScore()
                > sut.evaluate(boardAfterThreeMoves1, Set.of(move1, move2, move3)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyFourConsecutivePiecesABetterScoreThanItHadThree() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterThreeMoves = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        when(boardAfterThreeMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterThreeMoves.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterFourMoves = mockEmptyBoard();
        Position move4 = new Position(1, 5);
        when(boardAfterFourMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterFourMoves, Set.of(move1, move2, move3, move4)).getScore()
                > sut.evaluate(boardAfterThreeMoves, Set.of(move1, move2, move3)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyFourPartiallyClosedConsecutivePiecesAWorseScoreThanItThetWasOpen() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        Position move4 = new Position(1, 5);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move5 = new Position(1, 6);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move5))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves1, Set.of(move1, move2, move3, move4)).getScore()
                > sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3, move4, move5)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyFourClosedConsecutivePiecesAWorseScoreThanIfItWasPartiallyClosed() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        Position move4 = new Position(1, 5);
        Position move5 = new Position(1, 6);
        when(boardAfterMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves1.getPositionContent(move5))
                .thenReturn(PositionContent.WHITE);

        Gomoku boardAfterMoves2 = mockEmptyBoard();
        Position move6 = new Position(1, 1);
        when(boardAfterMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterMoves2.getPositionContent(move5))
                .thenReturn(PositionContent.WHITE);
        when(boardAfterMoves2.getPositionContent(move6))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(boardAfterMoves1, Set.of(move1, move2, move3, move4, move5)).getScore()
                > sut.evaluate(boardAfterMoves2, Set.of(move1, move2, move3, move4, move5, move6)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyFourConsecutivePiecesABetterScoreThanIfThePiecesAreSpreadOut() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterFourMoves1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        Position move4 = new Position(9, 9);
        when(boardAfterFourMoves1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves1.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterFourMoves2 = mockEmptyBoard();
        Position move5 = new Position(1, 5);
        when(boardAfterFourMoves2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves2.getPositionContent(move5))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterFourMoves2, Set.of(move1, move2, move3, move5)).getScore()
                > sut.evaluate(boardAfterFourMoves1, Set.of(move1, move2, move3, move4)).getScore());
    }

    @Test
    public void shouldGiveABoardWithMyFiveConsecutivePiecesABetterScoreThanItHadFour() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku boardAfterFourMoves = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        Position move4 = new Position(1, 5);
        when(boardAfterFourMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFourMoves.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);

        Gomoku boardAfterFiveMoves = mockEmptyBoard();
        Position move5 = new Position(1, 6);
        when(boardAfterFiveMoves.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFiveMoves.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFiveMoves.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFiveMoves.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(boardAfterFiveMoves.getPositionContent(move5))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(boardAfterFiveMoves, Set.of(move1, move2, move3, move4, move5)).getScore()
                > sut.evaluate(boardAfterFourMoves, Set.of(move1, move2, move3, move4)).getScore());
    }

    @Test
    public void shouldGiveABoardWithTheSameAmountOfConsecutivePiecesTheSameScoreNoMatterTheOrientation() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku horizontalLineBoard = mockEmptyBoard();
        Position move1 = new Position(5, 10);
        Position move2 = new Position(5, 11);
        Position move3 = new Position(5, 12);
        when(horizontalLineBoard.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(horizontalLineBoard.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(horizontalLineBoard.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        Gomoku verticalLineBoard = mockEmptyBoard();
        Position move4 = new Position(8, 5);
        Position move5 = new Position(9, 5);
        Position move6 = new Position(10, 5);
        when(verticalLineBoard.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(verticalLineBoard.getPositionContent(move5))
                .thenReturn(PositionContent.BLACK);
        when(verticalLineBoard.getPositionContent(move6))
                .thenReturn(PositionContent.BLACK);

        Gomoku leftDiagonalBoard = mockEmptyBoard();
        Position move7 = new Position(9, 9);
        Position move8 = new Position(10, 10);
        Position move9 = new Position(11, 11);
        when(leftDiagonalBoard.getPositionContent(move7))
                .thenReturn(PositionContent.BLACK);
        when(leftDiagonalBoard.getPositionContent(move8))
                .thenReturn(PositionContent.BLACK);
        when(leftDiagonalBoard.getPositionContent(move9))
                .thenReturn(PositionContent.BLACK);

        Gomoku rightDiagonalBoard = mockEmptyBoard();
        Position move10 = new Position(7, 7);
        Position move11 = new Position(8, 6);
        Position move12 = new Position(9, 5);
        when(rightDiagonalBoard.getPositionContent(move10))
                .thenReturn(PositionContent.BLACK);
        when(rightDiagonalBoard.getPositionContent(move11))
                .thenReturn(PositionContent.BLACK);
        when(rightDiagonalBoard.getPositionContent(move12))
                .thenReturn(PositionContent.BLACK);

        assertEquals(sut.evaluate(horizontalLineBoard, Set.of(move1, move2, move3)).getScore(),
                sut.evaluate(verticalLineBoard, Set.of(move4, move5, move6)).getScore());

        assertEquals(sut.evaluate(horizontalLineBoard, Set.of(move1, move2, move3)).getScore(),
                sut.evaluate(leftDiagonalBoard, Set.of(move7, move8, move9)).getScore());

        assertEquals(sut.evaluate(horizontalLineBoard, Set.of(move1, move2, move3)).getScore(),
                sut.evaluate(rightDiagonalBoard, Set.of(move10, move11, move12)).getScore());
    }

    @Test
    public void shouldGiveANegativeScoreIfMyOpponentInInBetterPositionsThanMe() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku board = mockEmptyBoard();
        Position myMove1 = new Position(1, 2);
        Position myMove2 = new Position(1, 3);
        when(board.getPositionContent(myMove1))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(myMove2))
                .thenReturn(PositionContent.BLACK);
        Position opponentsMove1 = new Position(5, 5);
        Position opponentsMove2 = new Position(5, 6);
        Position opponentsMove3 = new Position(5, 7);
        when(board.getPositionContent(opponentsMove1))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(opponentsMove2))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(opponentsMove3))
                .thenReturn(PositionContent.WHITE);

        assertTrue(sut.evaluate(board, Set.of(myMove1, myMove2, opponentsMove1,
                opponentsMove2, opponentsMove3)).getScore() < 0);
    }

    @Test
    public void shouldTakeIntoAccountAllMyPositionsWhenGivingAScore() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku board1 = mockEmptyBoard();
        Position move1 = new Position(1, 2);
        Position move2 = new Position(1, 3);
        Position move3 = new Position(1, 4);
        when(board1.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(board1.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(board1.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        Gomoku board2 = mockEmptyBoard();
        Position move4 = new Position(10, 10);
        Position move5 = new Position(11, 11);
        when(board2.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(board2.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(board2.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);
        when(board2.getPositionContent(move4))
                .thenReturn(PositionContent.BLACK);
        when(board2.getPositionContent(move5))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(board2, Set.of(move1, move2, move3, move4, move5)).getScore() 
                > sut.evaluate(board1, Set.of(move1, move2, move3)).getScore());
    }

    @Test
    public void shouldOnlyTakeIntoAccountTheSetOfMovesPassedToEvaluateWhenCreatingTheScore() {
        Gomoku initialBoard = mockEmptyBoard();

        GomokuHeuristic sut = new GomokuHeuristicImpl(initialBoard, PlayerName.BLACK);

        Gomoku board = mockEmptyBoard();
        Position move1 = new Position(5, 5);
        Position move2 = new Position(5, 6);
        Position move3 = new Position(5, 7);
        when(board.getPositionContent(move1))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(move2))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(move3))
                .thenReturn(PositionContent.BLACK);

        assertTrue(sut.evaluate(board, Set.of(move1, move2, move3)).getScore()
                > sut.evaluate(board, Set.of(move1, move2)).getScore());
    }

    private Gomoku mockEmptyBoard() {
        Gomoku board = mock(Gomoku.class);

        when(board.getPositionContent(any())).thenReturn(PositionContent.FREE);

        return board;
    }
}
