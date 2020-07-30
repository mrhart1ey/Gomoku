package mrhart1ey.gomoku.player.ai;

import java.util.Random;
import java.util.Set;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PossibleMoveGeneratorAdjacentTest {

    @Test
    public void shouldChooseARandomPositionWhenPassedABoardWithNoOccupiedPositions() {
        Random random = mock(Random.class);
        when(random.nextInt(anyInt())).thenReturn(3).thenReturn(5);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku emptyBoard = mockEmptyBoard();

        assertEquals(Set.of(new Position(3, 5)), sut.generate(emptyBoard));
    }
    
    @Test
    public void shouldReturnsNoPositionsWhenNoPositionsAreFree() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku fullBoard = mockFullBoard();

        assertEquals(Set.of(), sut.generate(fullBoard));
    }

    @Test
    public void shouldReturn8AdjacentEmptyPositionsForASinglePosition() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);

        assertEquals(Set.of(
                new Position(7, 8), new Position(7, 9),
                new Position(8, 9), new Position(9, 9),
                new Position(9, 8), new Position(9, 7),
                new Position(8, 7), new Position(7, 7)), sut.generate(board));
    }

    @Test
    public void shouldNotReturnPositionsThatAreNotOnTheBoard() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(0, 0)))
                .thenReturn(PositionContent.BLACK);

        assertEquals(Set.of(
                new Position(0, 1), new Position(1, 1),
                new Position(1, 0)), sut.generate(board));
    }

    @Test
    public void shouldBeAbleToReturnPositionsAtTheBottomOfTheBoard() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(18, 18)))
                .thenReturn(PositionContent.BLACK);

        assertEquals(Set.of(
                new Position(17, 18), new Position(17, 17),
                new Position(18, 17)), sut.generate(board));
    }

    @Test
    public void shouldReturn16PositionsWhenTheTwoPositionsOnTheBoardAreNotNearEachother() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(12, 12)))
                .thenReturn(PositionContent.BLACK);

        assertEquals(Set.of(
                new Position(7, 8), new Position(7, 9),
                new Position(8, 9), new Position(9, 9),
                new Position(9, 8), new Position(9, 7),
                new Position(8, 7), new Position(7, 7),
                new Position(11, 12), new Position(11, 13),
                new Position(12, 13), new Position(13, 13),
                new Position(13, 12), new Position(13, 11),
                new Position(12, 11), new Position(11, 11)), sut.generate(board));
    }

    @Test
    public void shouldReturnLessThan16PositionsWhenTheTwoPositionsOnTheBoardTouchingEachotherHorizontally() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(8, 9)))
                .thenReturn(PositionContent.WHITE);

        assertEquals(Set.of(
                new Position(7, 8), new Position(7, 9),
                new Position(7, 10), new Position(8, 10),
                new Position(9, 10), new Position(9, 9),
                new Position(9, 8), new Position(9, 7),
                new Position(8, 7), new Position(7, 7)), sut.generate(board));
    }

    @Test
    public void shouldReturnLessThan16PositionsWhenTheTwoPositionsOnTheBoardTouchingEachotherVertically() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(9, 8)))
                .thenReturn(PositionContent.WHITE);

        assertEquals(Set.of(
                new Position(7, 7), new Position(7, 8),
                new Position(7, 9), new Position(8, 9),
                new Position(9, 9), new Position(10, 9),
                new Position(10, 8), new Position(10, 7),
                new Position(9, 7), new Position(8, 7)), sut.generate(board));
    }

    @Test
    public void shouldReturnLessThan16PositionsWhenTheTwoPositionsOnTheBoardTouchingEachotherWithALeftDiagonal() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(9, 9)))
                .thenReturn(PositionContent.WHITE);

        assertEquals(Set.of(
                new Position(7, 7), new Position(7, 8),
                new Position(7, 9), new Position(8, 9),
                new Position(8, 10), new Position(9, 10),
                new Position(10, 10), new Position(10, 9),
                new Position(10, 8), new Position(9, 8),
                new Position(9, 7), new Position(8, 7)), sut.generate(board));
    }

    @Test
    public void shouldReturnLessThan16PositionsWhenTheTwoPositionsOnTheBoardTouchingEachotherWithARightDiagonal() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(8, 8)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(7, 9)))
                .thenReturn(PositionContent.WHITE);

        assertEquals(Set.of(
                new Position(6, 8), new Position(6, 9),
                new Position(6, 10), new Position(7, 10),
                new Position(8, 9), new Position(8, 10),
                new Position(9, 9), new Position(9, 8),
                new Position(9, 7), new Position(8, 7),
                new Position(7, 7), new Position(7, 8)), sut.generate(board));
    }

    @Test
    public void shouldReturnLessThan16PositionsWhenTheTwoPositionsOnTheBoardTouchingEachotherAndAgainstASideOfTheBoard() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(0, 5)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(1, 5)))
                .thenReturn(PositionContent.WHITE);

        assertEquals(Set.of(
                new Position(0, 4), new Position(0, 6),
                new Position(1, 4), new Position(1, 6),
                new Position(2, 4), new Position(2, 5),
                new Position(2, 6)), sut.generate(board));
    }

    @Test
    public void shouldCorrectlyReturnTheFreePositionsWhenThereAreManyOccupiedPositionsOnTheBoard() {
        Random random = mock(Random.class);

        PossibleMoveGeneratorAdjacent sut = new PossibleMoveGeneratorAdjacent(random);

        // Forms a triangle against the side of the board with one free position inside of it
        Gomoku board = mockEmptyBoard();
        when(board.getPositionContent(new Position(4, 18)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(5, 17)))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(new Position(6, 16)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(7, 15)))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(new Position(7, 16)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(7, 17)))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(new Position(7, 18)))
                .thenReturn(PositionContent.BLACK);
        when(board.getPositionContent(new Position(6, 18)))
                .thenReturn(PositionContent.WHITE);
        when(board.getPositionContent(new Position(5, 18)))
                .thenReturn(PositionContent.BLACK);

        assertEquals(Set.of(
                new Position(3, 18), new Position(3, 17),
                new Position(4, 17), new Position(4, 16),
                new Position(5, 16), new Position(5, 15),
                new Position(6, 15), new Position(6, 14),
                new Position(7, 14), new Position(8, 14),
                new Position(8, 15), new Position(8, 16),
                new Position(8, 17), new Position(8, 18),
                new Position(6, 17)), sut.generate(board));
    }

    private Gomoku mockEmptyBoard() {
        Gomoku board = mock(Gomoku.class);

        when(board.getPositionContent(any())).thenReturn(PositionContent.FREE);

        return board;
    }
    
    private Gomoku mockFullBoard() {
        Gomoku board = mock(Gomoku.class);

        when(board.getPositionContent(any())).thenReturn(PositionContent.BLACK);

        return board;
    }
}
