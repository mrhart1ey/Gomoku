package mrhart1ey.gomoku;

import java.util.concurrent.TimeUnit;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.Player;
import static org.awaitility.Awaitility.await;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveRetrieverTest {

    private Player me;
    private Player opponent;
    private PlayerName myName;
    private MoveRetriever sut;

    @Before
    public void before() {
        me = mock(Player.class);
        opponent = mock(Player.class);
        myName = PlayerName.BLACK;
        
        sut = new MoveRetriever(me, opponent, myName);
        sut.start();
    }
    
    @After
    public void after() {
        sut.stop();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfStartIsCalledTwice() {
        // Started before the method call bellow
        sut.start();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfStartIsCalledAfterStop() {
        MoveRetriever sut2 = new MoveRetriever(me, opponent, myName);
        
        sut2.start();
        sut2.stop();
        sut2.start();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfStopIsCalledBeforeStart() {
        MoveRetriever sut2 = new MoveRetriever(me, opponent, myName);
        
        sut2.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfExpectingAMoveIsCalledAgainBeforeRetrieveMove() {
        sut.expectingAMove(mock(Gomoku.class));

        sut.expectingAMove(mock(Gomoku.class));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnExceptionIfRetrieveMoveIsCalledBeforeExpectingAMoveIsCalled()
            throws InterruptedException {
        sut.retrieveMove();
    }

    @Test
    public void shouldNotHaveAMoveWaitingAfterStarting() {
        assertFalse(sut.isAMoveAvaliable());
    }

    @Test
    public void shouldSayAMoveIsWaitingAfterThePlayerWhoseTurnItIsSubmitsOne() {
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK);

        Position move = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move);
        
        sut.expectingAMove(board);
        
        await().atMost(1, TimeUnit.SECONDS).until(
                () -> sut.isAMoveAvaliable()
        );
    }

    @Test
    public void shouldRetrieveAMoveFromThePlayerWhooseTurnItIs() throws InterruptedException {
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK);

        Position move = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move);

        sut.expectingAMove(board);

        assertEquals(move, sut.retrieveMove());
    }
    
    @Test
    public void shouldSayAMoveIsNotWaitingAfterTheMoveHasBeenRetrieved() {
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK);

        Position move = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move);
        
        sut.expectingAMove(board);
        
        assertFalse(sut.isAMoveAvaliable());
    }

    @Test
    public void shouldRetrieveAMoveFromThePlayerWhooseTurnItIsEvenWhenTheOtherPlayerHasAMoveReady()
            throws InterruptedException {
        
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK);

        Position move1 = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move1);

        Position move2 = new Position(1, 1);
        when(opponent.nextMove(any())).thenReturn(move2);

        sut.expectingAMove(board);

        assertEquals(move1, sut.retrieveMove());
    }

    @Test
    public void shouldRetrieveMultipleMovesFromThePlayerWhoseTurnItIs()
            throws InterruptedException {
        
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK)
                .thenReturn(PlayerName.BLACK);

        Position move1 = new Position(5, 4);
        Position move2 = new Position(1, 1);
        when(me.nextMove(any())).thenReturn(move1).thenReturn(move2);

        sut.expectingAMove(board);

        assertEquals(move1, sut.retrieveMove());

        sut.expectingAMove(board);

        assertEquals(move2, sut.retrieveMove());
    }

    @Test
    public void shouldRetrieveMovesFromBothPlayers()
            throws InterruptedException {
        
        Gomoku board = mock(Gomoku.class);
        when(board.getCurrentTurn()).thenReturn(PlayerName.BLACK)
                .thenReturn(PlayerName.WHITE);

        Position move1 = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move1);

        Position move2 = new Position(1, 1);
        when(opponent.nextMove(any())).thenReturn(move2);

        sut.expectingAMove(board);

        assertEquals(move1, sut.retrieveMove());

        sut.expectingAMove(board);

        assertEquals(move2, sut.retrieveMove());
    }

    @Test
    public void shouldRetrieveMovesEvenIfTheBoardInstancesAreNotTheSame()
            throws InterruptedException {

        Gomoku board1 = mock(Gomoku.class);
        when(board1.getCurrentTurn()).thenReturn(PlayerName.BLACK)
                .thenReturn(PlayerName.WHITE);

        Position move1 = new Position(5, 4);
        when(me.nextMove(any())).thenReturn(move1);

        Position move2 = new Position(1, 1);
        when(opponent.nextMove(any())).thenReturn(move2);

        Gomoku board2 = mock(Gomoku.class);
        when(board2.getCurrentTurn()).thenReturn(PlayerName.BLACK);

        Position move3 = new Position(9, 9);
        when(me.nextMove(any())).thenReturn(move3);

        sut.expectingAMove(board1);

        sut.retrieveMove();

        sut.expectingAMove(board1);

        sut.retrieveMove();

        sut.expectingAMove(board2);

        assertEquals(move3, sut.retrieveMove());
    }
}
