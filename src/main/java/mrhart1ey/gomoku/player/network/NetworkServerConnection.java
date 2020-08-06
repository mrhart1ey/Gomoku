package mrhart1ey.gomoku.player.network;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.PlayerHandler;
import mrhart1ey.gomoku.player.network.protocol.ClientNetworkCommunicator;
import mrhart1ey.gomoku.player.network.protocol.NetworkCommunicator;

/**
 * Connects the client to a server
 */
public final class NetworkServerConnection {

    private final SingleTimePasser<Position> movePasser;
    private final SingleTimePasser<Gomoku> boardPasser;
    private final SingleTimePasser<Boolean> gameInProgress;

    private final LinkedBlockingQueue<GameConfiguration> configurationPasser;

    private final PlayerHandler playerHandler;

    private final Thread worker;
    
    private final CyclicBarrier barrier;
    private static final int WORKER_AND_MOVE_RETRIEVER_THREAD = 2;
    
    private final Lock moveRetrieverLock;

    private NetworkServerConnection(NetworkCommunicator communicator) {

        movePasser = new SingleTimePasser<>();
        boardPasser = new SingleTimePasser<>();
        gameInProgress = new SingleTimePasser<>();

        configurationPasser = new LinkedBlockingQueue<>();

        SingleTimePasser<Boolean> doIWantANewGame = new SingleTimePasser<>();
        SingleTimePasser<Boolean> doesOpponentWantANewGame = new SingleTimePasser<>();

        barrier = new CyclicBarrier(WORKER_AND_MOVE_RETRIEVER_THREAD);
        
        moveRetrieverLock = new ReentrantLock();
        
        worker = new Thread(new Worker(communicator, configurationPasser,
                movePasser, boardPasser, doIWantANewGame,
                doesOpponentWantANewGame, gameInProgress, barrier));

        this.playerHandler = new NetworkPlayerHandler(communicator, worker,
                doIWantANewGame, doesOpponentWantANewGame, gameInProgress);
    }

    public static NetworkServerConnection waitForServer(String host)
            throws IOException {
        try {
            NetworkCommunicator communicator
                    = ClientNetworkCommunicator.connectToServer(host,
                            NetworkClientConnection.PORT);

            NetworkServerConnection connection
                    = new NetworkServerConnection(communicator);

            connection.start();

            return connection;
        } catch (InterruptedException | ExecutionException ex) {
            throw new IOException("Could not connect to the server", ex);
        }
    }

    /**
     * Call before the game starts
     */
    private void start() {
        worker.start();
    }

    public GameConfiguration getGameConfiguration() {
        while (configurationPasser.peek() == null);

        return configurationPasser.peek();
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    /**
     * @param input The player that actually decides what moves to make
     * @return A player that uses the passed in player for getting the moves and
     * then sends the moves over the network
     */
    public Player getMe(Player input) {
        return new Me(input, movePasser, boardPasser, 
                gameInProgress, barrier, moveRetrieverLock);
    }

    /**
     * @return The server player
     */
    public Player getOpponent() {
        return new Opponent(movePasser, boardPasser, 
                gameInProgress, barrier, moveRetrieverLock);
    }

    private static class Worker implements Runnable {

        private final NetworkCommunicator communicator;
        private final LinkedBlockingQueue<GameConfiguration> configurationPasser;

        private final SingleTimePasser<Position> movePasser;
        private final SingleTimePasser<Gomoku> boardPasser;
        private final SingleTimePasser<Boolean> doIWantANewGame;
        private final SingleTimePasser<Boolean> doesOpponentWantANewGame;
        private final SingleTimePasser<Boolean> gameInProgress;
        
        private final CyclicBarrier barrier;

        public Worker(NetworkCommunicator communicator,
                LinkedBlockingQueue<GameConfiguration> configurationPasser,
                SingleTimePasser<Position> movePasser,
                SingleTimePasser<Gomoku> boardPasser,
                SingleTimePasser<Boolean> doIWantANewGame,
                SingleTimePasser<Boolean> doesOpponentWantANewGame,
                SingleTimePasser<Boolean> gameInProgress,
                CyclicBarrier barrier) {

            this.communicator = communicator;
            this.configurationPasser = configurationPasser;

            this.movePasser = movePasser;
            this.boardPasser = boardPasser;
            this.doIWantANewGame = doIWantANewGame;
            this.doesOpponentWantANewGame = doesOpponentWantANewGame;
            this.gameInProgress = gameInProgress;
            
            this.barrier = barrier;
        }

        @Override
        public void run() {
            boolean anotherGame;

            try {
                GameConfiguration configuration
                        = communicator.readGameConfiguration();

                configurationPasser.put(configuration);

                PlayerName myName = configuration.myName;

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

                    boolean opponentsAnswer = communicator.doesOpponentWantANewGame();

                    doesOpponentWantANewGame.put(opponentsAnswer);

                    boolean myAnswer = doIWantANewGame.take();

                    communicator.indicateIfIWantANewGame(myAnswer);

                    anotherGame = myAnswer && opponentsAnswer;
                } while (anotherGame);
            } catch (InterruptedException | BrokenBarrierException ex) {
                // Will occur if the opponent leaves
            }
        }
    }
}
