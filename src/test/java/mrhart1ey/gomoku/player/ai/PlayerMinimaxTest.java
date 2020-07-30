package mrhart1ey.gomoku.player.ai;

import java.util.Set;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerMinimaxTest {

    private static final long VALUE_NEVER_USED = 0;

    private Gomoku initialBoard;
    private GomokuHeuristic heuristic;
    private PossibleMoveGenerator moveGenerator;

    @Before
    public void beforeTests() {
        initialBoard = mockOngoingBoard();
        when(initialBoard.getPositionContent(any())).thenReturn(PositionContent.FREE);

        heuristic = mock(GomokuHeuristic.class);
        when(heuristic.evaluate(initialBoard, Set.of())).thenReturn(heuristic);

        moveGenerator = mock(PossibleMoveGenerator.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPassedABoardThatIsNotOngoing() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 1);
        
        Gomoku board = mockVictoryBoard();
        
        sut.nextMove(board);
    }

    @Test
    public void shouldReturnTheOnlyPossibleMoveWhenOnlyOneMoveIsAvailable() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 1);

        Position onlyPossibleMove = new Position(5, 7);
        Gomoku boardAfterMove = mockOngoingBoard();
        when(initialBoard.move(onlyPossibleMove)).thenReturn(boardAfterMove);

        GomokuHeuristic heuristicAfterMove = mock(GomokuHeuristic.class);
        when(heuristicAfterMove.getScore()).thenReturn(5L);

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(onlyPossibleMove));

        when(heuristic.evaluate(boardAfterMove, Set.of(onlyPossibleMove)))
                .thenReturn(heuristicAfterMove);

        assertEquals(onlyPossibleMove, sut.nextMove(initialBoard));
    }

    @Test
    public void shouldReturnTheMoveWithTheHighestHeuristicValueFromTheFirstLevel() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 1);

        Position move1 = new Position(5, 7);
        Gomoku boardAfterMove1 = mockOngoingBoard();
        when(initialBoard.move(move1)).thenReturn(boardAfterMove1);
        GomokuHeuristic heuristicAfterMove1 = mockHeuristic(5);
        when(heuristic.evaluate(boardAfterMove1, Set.of(move1)))
                .thenReturn(heuristicAfterMove1);

        Position move2 = new Position(9, 9);
        Gomoku boardAfterMove2 = mockOngoingBoard();
        when(initialBoard.move(move2)).thenReturn(boardAfterMove2);
        GomokuHeuristic heuristicAfterMove2 = mockHeuristic(8);
        when(heuristic.evaluate(boardAfterMove2, Set.of(move2)))
                .thenReturn(heuristicAfterMove2);

        Position move3 = new Position(10, 11);
        Gomoku boardAfterMove3 = mockOngoingBoard();
        when(initialBoard.move(move3)).thenReturn(boardAfterMove3);
        GomokuHeuristic heuristicAfterMove3 = mockHeuristic(2L);
        when(heuristic.evaluate(boardAfterMove3, Set.of(move3)))
                .thenReturn(heuristicAfterMove3);

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(move1, move2, move3));

        assertEquals(move2, sut.nextMove(initialBoard));
    }

    @Test
    public void shouldReturnTheBestMoveThatMinimizesTheHeuristicValueOfTheOpponentsBestMove() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 2);

        Position myMove1 = new Position(5, 7);
        Gomoku boardAfterMyMove1 = mockOngoingBoard();
        when(initialBoard.move(myMove1)).thenReturn(boardAfterMyMove1);
        GomokuHeuristic heuristicAfterMyMove1 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristic.evaluate(boardAfterMyMove1, Set.of(myMove1)))
                .thenReturn(heuristicAfterMyMove1);

        Position opponentsMove1 = new Position(6, 2);
        Gomoku boardAfteropponentsMove1 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove1)).thenReturn(boardAfteropponentsMove1);
        GomokuHeuristic heuristicAfteropponentsMove1 = mockHeuristic(-5);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove1, Set.of(opponentsMove1)))
                .thenReturn(heuristicAfteropponentsMove1);

        Position opponentsMove2 = new Position(2, 6);
        Gomoku boardAfteropponentsMove2 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove2)).thenReturn(boardAfteropponentsMove2);
        GomokuHeuristic heuristicAfteropponentsMove2 = mockHeuristic(0);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove2, Set.of(opponentsMove2)))
                .thenReturn(heuristicAfteropponentsMove2);

        when(moveGenerator.generate(boardAfterMyMove1))
                .thenReturn(Set.of(opponentsMove1, opponentsMove2));

        Position myMove2 = new Position(9, 9);
        Gomoku boardAfterMyMove2 = mockOngoingBoard();
        when(initialBoard.move(myMove2)).thenReturn(boardAfterMyMove2);
        GomokuHeuristic heuristicAfterMyMove2 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristic.evaluate(boardAfterMyMove2, Set.of(myMove2)))
                .thenReturn(heuristicAfterMyMove2);

        Position opponentsMove3 = new Position(11, 11);
        Gomoku boardAfteropponentsMove3 = mockOngoingBoard();
        when(boardAfterMyMove2.move(opponentsMove3)).thenReturn(boardAfteropponentsMove3);
        GomokuHeuristic heuristicAfteropponentsMove3 = mockHeuristic(-7);
        when(heuristicAfterMyMove2.evaluate(boardAfteropponentsMove3, Set.of(opponentsMove3)))
                .thenReturn(heuristicAfteropponentsMove3);

        Position opponentsMove4 = new Position(18, 6);
        Gomoku boardAfteropponentsMove4 = mockOngoingBoard();
        when(boardAfterMyMove2.move(opponentsMove4)).thenReturn(boardAfteropponentsMove4);
        GomokuHeuristic heuristicAfteropponentsMove4 = mockHeuristic(-7);
        when(heuristicAfterMyMove2.evaluate(boardAfteropponentsMove4, Set.of(opponentsMove4)))
                .thenReturn(heuristicAfteropponentsMove4);

        when(moveGenerator.generate(boardAfterMyMove2))
                .thenReturn(Set.of(opponentsMove3, opponentsMove4));

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(myMove1, myMove2));

        assertEquals(myMove1, sut.nextMove(initialBoard));
    }

    @Test
    public void shouldReturnTheMoveThatResultsInVictoryForTheAi() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 2);

        Position myMove1 = new Position(5, 7);
        Gomoku boardAfterMyMove1 = mockOngoingBoard();
        when(initialBoard.move(myMove1)).thenReturn(boardAfterMyMove1);
        GomokuHeuristic heuristicAfterMyMove1 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristic.evaluate(boardAfterMyMove1, Set.of(myMove1)))
                .thenReturn(heuristicAfterMyMove1);

        Position opponentsMove1 = new Position(6, 2);
        Gomoku boardAfteropponentsMove1 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove1)).thenReturn(boardAfteropponentsMove1);
        GomokuHeuristic heuristicAfteropponentsMove1 = mockHeuristic(-5);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove1, Set.of(opponentsMove1)))
                .thenReturn(heuristicAfteropponentsMove1);

        Position opponentsMove2 = new Position(2, 6);
        Gomoku boardAfteropponentsMove2 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove2)).thenReturn(boardAfteropponentsMove2);
        GomokuHeuristic heuristicAfteropponentsMove2 = mockHeuristic(0);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove2, Set.of(opponentsMove2)))
                .thenReturn(heuristicAfteropponentsMove2);

        when(moveGenerator.generate(boardAfterMyMove1))
                .thenReturn(Set.of(opponentsMove1, opponentsMove2));

        Position myMove2 = new Position(9, 9);
        Gomoku boardAfterMyMove2 = mockVictoryBoard();
        when(initialBoard.move(myMove2)).thenReturn(boardAfterMyMove2);
        GomokuHeuristic heuristicAfterMyMove2 = mockHeuristic(99);
        when(heuristic.evaluate(boardAfterMyMove2, Set.of(myMove2)))
                .thenReturn(heuristicAfterMyMove2);

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(myMove1, myMove2));

        assertEquals(myMove2, sut.nextMove(initialBoard));
    }

    @Test
    public void shouldReturnTheMoveThatResultsInVictoryForTheAiEvenIfTheOpponentCanWinItAfterTheMove() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 2);

        Position myMove1 = new Position(5, 7);
        Gomoku boardAfterMyMove1 = mockOngoingBoard();
        when(initialBoard.move(myMove1)).thenReturn(boardAfterMyMove1);
        GomokuHeuristic heuristicAfterMyMove1 = mockHeuristic(5);
        when(heuristic.evaluate(boardAfterMyMove1, Set.of(myMove1)))
                .thenReturn(heuristicAfterMyMove1);

        Position opponentsMove1 = new Position(6, 2);
        Gomoku boardAfteropponentsMove1 = mockVictoryBoard();
        when(boardAfterMyMove1.move(opponentsMove1)).thenReturn(boardAfteropponentsMove1);
        GomokuHeuristic heuristicAfteropponentsMove1 = mockHeuristic(-99);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove1, Set.of(opponentsMove1)))
                .thenReturn(heuristicAfteropponentsMove1);

        Position opponentsMove2 = new Position(2, 6);
        Gomoku boardAfteropponentsMove2 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove2)).thenReturn(boardAfteropponentsMove2);
        GomokuHeuristic heuristicAfteropponentsMove2 = mockHeuristic(-5);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove2, Set.of(opponentsMove2)))
                .thenReturn(heuristicAfteropponentsMove2);

        when(moveGenerator.generate(boardAfterMyMove1))
                .thenReturn(Set.of(opponentsMove1, opponentsMove2));

        Position myMove2 = new Position(9, 9);
        Gomoku boardAfterMyMove2 = mockVictoryBoard();
        when(initialBoard.move(myMove2)).thenReturn(boardAfterMyMove2);
        GomokuHeuristic heuristicAfterMyMove2 = mockHeuristic(99);
        when(heuristic.evaluate(boardAfterMyMove2, Set.of(myMove2)))
                .thenReturn(heuristicAfterMyMove2);

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(myMove1, myMove2));

        assertEquals(myMove2, sut.nextMove(initialBoard));
    }

    @Test
    public void shouldFindTheBestMoveForTheAiMoreThan2MovesInTheFuture() {
        PlayerMinimax sut = new PlayerMinimax(heuristic, initialBoard,
                moveGenerator, 3);

        Position myMove1 = new Position(5, 7);
        Gomoku boardAfterMyMove1 = mockOngoingBoard();
        when(initialBoard.move(myMove1)).thenReturn(boardAfterMyMove1);
        GomokuHeuristic heuristicAfterMyMove1 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristic.evaluate(boardAfterMyMove1, Set.of(myMove1)))
                .thenReturn(heuristicAfterMyMove1);

        // Go down the tree
        Position opponentsMove1 = new Position(6, 2);
        Gomoku boardAfteropponentsMove1 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove1)).thenReturn(boardAfteropponentsMove1);
        GomokuHeuristic heuristicAfteropponentsMove1 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove1, Set.of(opponentsMove1)))
                .thenReturn(heuristicAfteropponentsMove1);

        // Go down the tree
        Position myMove2 = new Position(2, 2);
        Gomoku boardAfterMyMove2 = mockOngoingBoard();
        when(boardAfteropponentsMove1.move(myMove2)).thenReturn(boardAfterMyMove2);
        GomokuHeuristic heuristicAfterMyMove2 = mockHeuristic(3);
        when(heuristicAfteropponentsMove1.evaluate(boardAfterMyMove2, Set.of(myMove2)))
                .thenReturn(heuristicAfterMyMove2);

        Position myMove3 = new Position(2, 3);
        Gomoku boardAfterMyMove3 = mockOngoingBoard();
        when(boardAfteropponentsMove1.move(myMove3)).thenReturn(boardAfterMyMove3);
        GomokuHeuristic heuristicAfterMyMove3 = mockHeuristic(5);
        when(heuristicAfteropponentsMove1.evaluate(boardAfterMyMove3, Set.of(myMove3)))
                .thenReturn(heuristicAfterMyMove3);

        when(moveGenerator.generate(boardAfteropponentsMove1))
                .thenReturn(Set.of(myMove2, myMove3));

        // Go up the tree
        Position opponentsMove2 = new Position(5, 9);
        Gomoku boardAfteropponentsMove2 = mockOngoingBoard();
        when(boardAfterMyMove1.move(opponentsMove2)).thenReturn(boardAfteropponentsMove2);
        GomokuHeuristic heuristicAfteropponentsMove2 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristicAfterMyMove1.evaluate(boardAfteropponentsMove2, Set.of(opponentsMove2)))
                .thenReturn(heuristicAfteropponentsMove2);

        // Go down the tree
        Position myMove4 = new Position(10, 11);
        Gomoku boardAfterMyMove4 = mockOngoingBoard();
        when(boardAfteropponentsMove2.move(myMove4)).thenReturn(boardAfterMyMove4);
        GomokuHeuristic heuristicAfterMyMove4 = mockHeuristic(6);
        when(heuristicAfteropponentsMove2.evaluate(boardAfterMyMove4, Set.of(myMove4)))
                .thenReturn(heuristicAfterMyMove4);

        Position myMove5 = new Position(0, 9);
        Gomoku boardAfterMyMove5 = mockOngoingBoard();
        when(boardAfteropponentsMove2.move(myMove5)).thenReturn(boardAfterMyMove5);
        GomokuHeuristic heuristicAfterMyMove5 = mockHeuristic(9);
        when(heuristicAfteropponentsMove2.evaluate(boardAfterMyMove5, Set.of(myMove5)))
                .thenReturn(heuristicAfterMyMove5);

        // Go up the tree
        // Go up the tree
        when(moveGenerator.generate(boardAfteropponentsMove2))
                .thenReturn(Set.of(myMove4, myMove5));

        when(moveGenerator.generate(boardAfterMyMove1))
                .thenReturn(Set.of(opponentsMove1, opponentsMove2));

        Position myMove6 = new Position(12, 8);
        Gomoku boardAfterMyMove6 = mockOngoingBoard();
        when(initialBoard.move(myMove6)).thenReturn(boardAfterMyMove6);
        GomokuHeuristic heuristicAfterMyMove6 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristic.evaluate(boardAfterMyMove6, Set.of(myMove6)))
                .thenReturn(heuristicAfterMyMove6);

        // Go down the tree
        Position opponentsMove3 = new Position(9, 8);
        Gomoku boardAfteropponentsMove3 = mockOngoingBoard();
        when(boardAfterMyMove6.move(opponentsMove3)).thenReturn(boardAfteropponentsMove3);
        GomokuHeuristic heuristicAfteropponentsMove3 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristicAfterMyMove6.evaluate(boardAfteropponentsMove3, Set.of(opponentsMove3)))
                .thenReturn(heuristicAfteropponentsMove3);

        // Go down the tree
        Position myMove7 = new Position(2, 0);
        Gomoku boardAfterMyMove7 = mockOngoingBoard();
        when(boardAfteropponentsMove3.move(myMove7)).thenReturn(boardAfterMyMove7);
        GomokuHeuristic heuristicAfterMyMove7 = mockHeuristic(1);
        when(heuristicAfteropponentsMove3.evaluate(boardAfterMyMove7, Set.of(myMove7)))
                .thenReturn(heuristicAfterMyMove7);

        Position myMove8 = new Position(0, 0);
        Gomoku boardAfterMyMove8 = mockOngoingBoard();
        when(boardAfteropponentsMove3.move(myMove8)).thenReturn(boardAfterMyMove8);
        GomokuHeuristic heuristicAfterMyMove8 = mockHeuristic(2);
        when(heuristicAfteropponentsMove3.evaluate(boardAfterMyMove8, Set.of(myMove8)))
                .thenReturn(heuristicAfterMyMove8);

        when(moveGenerator.generate(boardAfteropponentsMove3))
                .thenReturn(Set.of(myMove7, myMove8));

        // Go up the tree
        Position opponentsMove4 = new Position(9, 4);
        Gomoku boardAfteropponentsMove4 = mockOngoingBoard();
        when(boardAfterMyMove6.move(opponentsMove4)).thenReturn(boardAfteropponentsMove4);
        GomokuHeuristic heuristicAfteropponentsMove4 = mockHeuristic(VALUE_NEVER_USED);
        when(heuristicAfterMyMove6.evaluate(boardAfteropponentsMove4, Set.of(opponentsMove4)))
                .thenReturn(heuristicAfteropponentsMove4);

        // Go down the tree
        Position myMove9 = new Position(18, 18);
        Gomoku boardAfterMyMove9 = mockOngoingBoard();
        when(boardAfteropponentsMove4.move(myMove9)).thenReturn(boardAfterMyMove9);
        GomokuHeuristic heuristicAfterMyMove9 = mockHeuristic(0);
        when(heuristicAfteropponentsMove4.evaluate(boardAfterMyMove9, Set.of(myMove9)))
                .thenReturn(heuristicAfterMyMove9);

        Position myMove10 = new Position(2, 16);
        Gomoku boardAfterMyMove10 = mockOngoingBoard();
        when(boardAfteropponentsMove4.move(myMove10)).thenReturn(boardAfterMyMove10);
        GomokuHeuristic heuristicAfterMyMove10 = mockHeuristic(-1);
        when(heuristicAfteropponentsMove4.evaluate(boardAfterMyMove10, Set.of(myMove10)))
                .thenReturn(heuristicAfterMyMove10);

        // Go up the tree
        // Go up the tree
        when(moveGenerator.generate(boardAfteropponentsMove4))
                .thenReturn(Set.of(myMove9, myMove10));

        when(moveGenerator.generate(boardAfterMyMove6))
                .thenReturn(Set.of(opponentsMove3, opponentsMove4));

        when(moveGenerator.generate(initialBoard))
                .thenReturn(Set.of(myMove1, myMove6));

        assertEquals(myMove1, sut.nextMove(initialBoard));
    }

    private Gomoku mockOngoingBoard() {
        Gomoku board = mock(Gomoku.class);

        when(board.getGameState()).thenReturn(GameState.ONGOING);

        return board;
    }

    private Gomoku mockVictoryBoard() {
        Gomoku board = mock(Gomoku.class);

        when(board.getGameState()).thenReturn(GameState.VICTORY);

        return board;
    }

    private GomokuHeuristic mockHeuristic(long score) {
        GomokuHeuristic heuristic = mock(GomokuHeuristic.class);

        when(heuristic.getScore()).thenReturn(score);

        return heuristic;
    }
}
