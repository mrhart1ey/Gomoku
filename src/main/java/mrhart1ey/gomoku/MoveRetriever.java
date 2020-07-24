package mrhart1ey.gomoku;

import java.util.concurrent.atomic.AtomicBoolean;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;

/**
 * The class allows checking of the two players to see if they have made a move
 * without the possible of blocking if they have not
 *
 * The methods should be used like the following
 *
 * start()
 *
 * while(gameRunning) if(isAMoveAvailable()) move = retrieveMove() if(move is
 * valid) ... moveWasAccepted(board after move) ... else ...
 * moveWasNotAccepted() ...
 */
public class MoveRetriever {

    private final SingleTimePasser<Gomoku> boardPasser;
    private final SingleTimePasser<Position> movePasser;
    private final SingleTimePasser<Boolean> moveAcceptedPasser;
    private final AtomicBoolean workerRunning;

    private final Thread worker;

    public MoveRetriever(Player me, Player opponent,
            Gomoku initialBoard, PlayerName myName) {
        boardPasser = new SingleTimePasser<>();
        boardPasser.put(initialBoard);

        movePasser = new SingleTimePasser<>();
        
        moveAcceptedPasser  = new SingleTimePasser<>();

        workerRunning = new AtomicBoolean(true);

        worker = new Thread(new Worker(me, opponent, myName, boardPasser,
                movePasser, moveAcceptedPasser, workerRunning));
    }

    /**
     * Call before the game starts
     */
    public void start() {
        worker.start();
    }

    /**
     * Call after the game has finished
     */
    public void stop() {
        workerRunning.set(false);
        worker.interrupt();
    }

    /**
     * @return True if a move is available, false if not
     */
    public boolean isAMoveAvailable() {
        return movePasser.isObjectWaiting();
    }

    /**
     * @return The move from the player whose turn it currently is
     */
    public Position retrieveMove() {
        Position move;

        try {
            move = movePasser.take();
        } catch (InterruptedException ex) {
            move = null;
        }

        return move;
    }

    public void moveWasAccepted(Gomoku board) {
        moveAcceptedPasser.put(true);
        boardPasser.put(board);
    }
    
    public void moveWasNotAccepted() {
        moveAcceptedPasser.put(false);
    }

    private static class Worker implements Runnable {

        private final Player me;
        private final Player opponent;

        private final PlayerName myName;

        private final SingleTimePasser<Gomoku> boardPasser;
        private final SingleTimePasser<Position> movePasser;
        private final SingleTimePasser<Boolean> moveAcceptedPasser;

        private final AtomicBoolean workerRunning;

        private Worker(Player me, Player opponent,
                PlayerName myName,
                SingleTimePasser<Gomoku> boardPasser,
                SingleTimePasser<Position> movePasser,
                SingleTimePasser<Boolean> moveAcceptedPasser,
                AtomicBoolean workerRunning) {
            this.me = me;
            this.opponent = opponent;

            this.myName = myName;

            this.boardPasser = boardPasser;
            this.movePasser = movePasser;
            this.moveAcceptedPasser = moveAcceptedPasser;
            
            this.workerRunning = workerRunning;
        }

        @Override
        public void run() {
            try {
                Gomoku board = boardPasser.take();
                
                while (workerRunning.get()) {
                    Position move;

                    if (board.getCurrentTurn() == myName) {
                        move = me.nextMove(board);
                    } else {
                        move = opponent.nextMove(board);
                    }

                    if (move == null) {
                        break;
                    }

                    movePasser.put(move);
                    
                    if(moveAcceptedPasser.take()) {
                        board = boardPasser.take();
                    }
                }
            } catch (InterruptedException ex) {

            }
        }
    }
}
