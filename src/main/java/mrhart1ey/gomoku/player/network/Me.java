package mrhart1ey.gomoku.player.network;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;

final class Me implements Player {

    private final Player input;

    private final SingleTimePasser<Position> movePasser;
    private final SingleTimePasser<Gomoku> boardPasser;
    private final SingleTimePasser<Boolean> gameInProgress;

    private final CyclicBarrier barrier;

    private final Lock moveRetrieverLock;

    public Me(Player input, SingleTimePasser<Position> movePasser,
            SingleTimePasser<Gomoku> boardPasser,
            SingleTimePasser<Boolean> gameInProgress,
            CyclicBarrier barrier,
            Lock moveRetrieverLock) {

        this.input = input;

        this.movePasser = movePasser;
        this.boardPasser = boardPasser;
        this.gameInProgress = gameInProgress;

        this.barrier = barrier;

        this.moveRetrieverLock = moveRetrieverLock;
    }

    @Override
    public Position nextMove(Gomoku board) {
        moveRetrieverLock.lock();

        Position move = input.nextMove(board);

        if (move != null) {
            movePasser.put(move);
            boardPasser.put(board);
            gameInProgress.put(true);
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.println(ex);
        } finally {
            moveRetrieverLock.unlock();
        }

        return move;
    }

}
