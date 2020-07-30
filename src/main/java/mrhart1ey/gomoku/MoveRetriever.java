package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.Player;

/**
 * The class allows checking of the two players to see if they have made a move
 * without the possible of blocking if they have not.
 *
 * An instance of MoveRetriever can be reused for another game after a game has finished
 * 
 * The methods should be used like the following...
 *
 * start() // Before any other method is called
 * ...
 * expectingAMove(initialBoard) // 
 * ...
 * while(gameRunning) 
 *      if(isAMoveAvaliable())
 *          move = retrieveMove() 
 *          if(move is valid)
 *              ...
 *              if(game is not over)
 *                  expectingAMove(boardAfterMove)
 *          else
 *              expectingAMove(originalBoard)
 * ...
 * stop() // When you are done with the move retriever
 */
public class MoveRetriever {

    private final Thread myWorker;
    private final Thread opponentsWorker;

    private final SingleTimePasser<Gomoku> boardPasserToMyWorker;
    private final SingleTimePasser<Gomoku> boardPasserToOpponentsWorker;

    private final SingleTimePasser<Position> movePasser;

    private final PlayerName myName;

    private boolean expectingAMove;

    /**
     * @param me A player to get moves from
     * @param opponent Another player to get moves from
     * @param myName The name of player "me"
     */
    public MoveRetriever(Player me, Player opponent, PlayerName myName) {
        boardPasserToMyWorker = new SingleTimePasser<>();
        boardPasserToOpponentsWorker = new SingleTimePasser<>();

        movePasser = new SingleTimePasser<>();

        myWorker = new Thread(new Worker(me, boardPasserToMyWorker, movePasser));

        opponentsWorker
                = new Thread(new Worker(opponent, boardPasserToOpponentsWorker, movePasser));

        this.myName = myName;

        expectingAMove = false;
    }

    /**
     * Call before calling any methods
     * 
     * @throws IllegalStateException If it is called after stop() has been called
     * @Throws IllegalStateException If called when already started
     */
    public void start() {
        if (myWorker.getState() == Thread.State.TERMINATED
                || opponentsWorker.getState() == Thread.State.TERMINATED) {
            throw new IllegalStateException("Can not restart the move retriever "
                    + "after it has stopped");
        }else if (myWorker.getState() != Thread.State.NEW
                || opponentsWorker.getState() != Thread.State.NEW) {
            throw new IllegalStateException("Move retriever has already started");
        }

        myWorker.start();
        opponentsWorker.start();
    }

    /**
     * Tells the move retriever to expect a move.
     * 
     * @param board The board to get whose turn it is, and to be passed to the player
     * whose turn it is so they can decide on their next move
     * @throws IllegalStateException If it is already expecting a move
     */
    public void expectingAMove(Gomoku board) {
        if (expectingAMove) {
            throw new IllegalStateException("Already expecting a move");
        }

        if (board.getCurrentTurn() == myName) {
            boardPasserToMyWorker.put(board);
        } else {
            boardPasserToOpponentsWorker.put(board);
        }

        expectingAMove = true;
    }

    /**
     * @return True if the player that the move retriever was expecting a move
     * from has made one, false otherwise. Not that after a player makes a move
     * this method may not immediately return true. 
     */
    public boolean isAMoveAvaliable() {
        return movePasser.isObjectWaiting();
    }

    /**
     * 
     * @return The move that the player that the move retriever was expecting a move
     * from has made.
     * 
     * This method will block until the player that the move retriever was expecting a move
     * from has made one.
     * 
     * @throws IllegalStateException If called when not expecting a move
     * @throws InterruptedException If the move retriever is interrupted.
     */
    public Position retrieveMove() throws InterruptedException {
        if(!expectingAMove) {
            throw new IllegalStateException("Not expecting a move");
        }
        
        expectingAMove = false;

        return movePasser.take();
    }

    /**
     * Call when no more moves are needed
     * 
     * @throws IllegalStateException If it is called after stop() has been called
     * @Throws IllegalStateException If called before start() has been called
     */
    public void stop() {
        if (myWorker.getState() == Thread.State.TERMINATED
                || opponentsWorker.getState() == Thread.State.TERMINATED) {
            throw new IllegalStateException("The move retriever has already "
                    + "been stopped");
        }else if (myWorker.getState() == Thread.State.NEW
                || opponentsWorker.getState() == Thread.State.NEW) {
            throw new IllegalStateException("Can not stop a move retriever that "
                    + " has not started yet");
        }

        myWorker.interrupt();
        opponentsWorker.interrupt();
    }

    private static class Worker implements Runnable {

        private final Player player;

        private final SingleTimePasser<Gomoku> boardPasser;
        private final SingleTimePasser<Position> movePasser;

        private Worker(Player player,
                SingleTimePasser<Gomoku> boardPasser,
                SingleTimePasser<Position> movePasser) {

            this.player = player;

            this.boardPasser = boardPasser;
            this.movePasser = movePasser;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Gomoku board = boardPasser.take();

                    Position move = player.nextMove(board);

                    // Might have been interrupted on the nextMove call
                    if(move != null) {
                        movePasser.put(move);
                    }
                }
            } catch (InterruptedException ex) {
                
            }
        }
    }
}
