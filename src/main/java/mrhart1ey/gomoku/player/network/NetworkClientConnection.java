package mrhart1ey.gomoku.player.network;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.PlayerHandler;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.network.protocol.NetworkCommunicator;
import mrhart1ey.gomoku.player.network.protocol.ServerNetworkCommunicator;

/**
 * Connects the server to a client
 */
public final class NetworkClientConnection {
    public static final int PORT = 22222;
    
    private final SingleTimePasser<Position> movePasser;
    private final SingleTimePasser<Gomoku> boardPasser;
    private final SingleTimePasser<Boolean> gameInProgress;

    private final PlayerHandler playerHandler;

    private final Thread worker;
    
    private final CyclicBarrier workerBarrier;
    private static final int WORKER_AND_MOVE_RETRIEVER_THREAD = 2;
    
    private final Lock moveRetrieverLock;

    private NetworkClientConnection(GameConfiguration configuration,
            NetworkCommunicator communicator) {

        movePasser = new SingleTimePasser<>();
        boardPasser = new SingleTimePasser<>();
        gameInProgress = new SingleTimePasser<>();

        SingleTimePasser<Boolean> doIWantANewGame = new SingleTimePasser<>();
        SingleTimePasser<Boolean> doesOpponentWantANewGame = new SingleTimePasser<>();
        
        workerBarrier = new CyclicBarrier(WORKER_AND_MOVE_RETRIEVER_THREAD);
        
        moveRetrieverLock = new ReentrantLock();
        
        worker = new Thread(new Worker(configuration,
                communicator, movePasser, boardPasser,
                doIWantANewGame, doesOpponentWantANewGame, gameInProgress, workerBarrier));

        this.playerHandler = new NetworkPlayerHandler(communicator, worker,
                doIWantANewGame, doesOpponentWantANewGame, gameInProgress);
    }

    public static NetworkClientConnection waitForClient(GameConfiguration gameConfiguration)
            throws IOException {
        try {
            NetworkCommunicator communicator
                    = ServerNetworkCommunicator.waitForClient(PORT);

            NetworkClientConnection connection
                    = new NetworkClientConnection(gameConfiguration,
                            communicator);

            connection.start();

            return connection;
        } catch (InterruptedException | ExecutionException ex) {
            throw new IOException("Could not connect to a client", ex);
        }
    }

    /**
     * Call before the game starts
     */
    private void start() throws InterruptedException {
        worker.start();
    }

    /**
     * @param input The player that actually decides what moves to make
     * @return A player that uses the passed in player for getting the moves and
     * then sends the moves over the network
     */
    public Player getMe(Player input) {
        return new Me(input, movePasser, boardPasser, 
                gameInProgress, workerBarrier, moveRetrieverLock);
    }

    /**
     * @return The client player
     */
    public Player getOpponent() {
        return new Opponent(movePasser, boardPasser, 
                gameInProgress, workerBarrier, moveRetrieverLock);
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    private static class Worker implements Runnable {

        private final GameConfiguration configuration;

        private final NetworkCommunicator communicator;

        private final SingleTimePasser<Position> movePasser;
        private final SingleTimePasser<Gomoku> boardPasser;
        private final SingleTimePasser<Boolean> doIWantANewGame;
        private final SingleTimePasser<Boolean> doesOpponentWantANewGame;
        private final SingleTimePasser<Boolean> gameInProgress;
        
        private final CyclicBarrier barrier;

        public Worker(GameConfiguration configuration,
                NetworkCommunicator communicator,
                SingleTimePasser<Position> movePasser,
                SingleTimePasser<Gomoku> boardPasser,
                SingleTimePasser<Boolean> doIWantANewGame,
                SingleTimePasser<Boolean> doesOpponentWantANewGame,
                SingleTimePasser<Boolean> gameInProgress,
                CyclicBarrier barrier) {

            this.configuration = configuration;

            this.communicator = communicator;

            this.movePasser = movePasser;
            this.boardPasser = boardPasser;
            this.doIWantANewGame = doIWantANewGame;
            this.doesOpponentWantANewGame = doesOpponentWantANewGame;
            this.gameInProgress = gameInProgress;
            
            this.barrier = barrier;
        }

        @Override
        public void run() {
            PlayerName myName = configuration.myName;

            GameConfiguration clientConfiguration
                    = new GameConfiguration(configuration.board,
                            configuration.gameTimer, myName.turnAfter());

            boolean anotherGame;

            try {
                communicator.sendGameConfiguration(clientConfiguration);

                do {

                    while (gameInProgress.take()) {
                        Gomoku board = boardPasser.take();
                        
                        PlayerName currentTurn = board.getCurrentTurn();

                        if (currentTurn == myName) {
                            Position move = movePasser.take();
                            
                            communicator.sendMove(move);
                        } else {
                            Position move = communicator.readMove();
                            
                            movePasser.put(move);
                        }
                        
                        barrier.await();
                    }

                    boolean myAnswer = doIWantANewGame.take();

                    communicator.indicateIfIWantANewGame(myAnswer);

                    boolean opponentsAnswer = communicator.doesOpponentWantANewGame();

                    doesOpponentWantANewGame.put(opponentsAnswer);

                    anotherGame = myAnswer && opponentsAnswer;
                } while (anotherGame);
            } catch (InterruptedException | BrokenBarrierException ex) {
                // Will occur if a player leaves
            }
        }
    }
}
