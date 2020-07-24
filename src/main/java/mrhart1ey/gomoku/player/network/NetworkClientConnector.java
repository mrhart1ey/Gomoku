package mrhart1ey.gomoku.player.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.PlayerHandler;

/**
 * Connects the server to a client
 */
final public class NetworkClientConnector {

    private final Thread moveRetriever;
    private final Thread newGameRequestor;
    private final Thread backgroundWorker;

    private final SingleTimePasser<Gomoku> boardPasser;
    private final BlockingQueue<Position> movePasserToMe;
    private final BlockingQueue<Position> movePasserToOtherPlayer;

    private final SingleTimePasser<Boolean> meAnotherGame;
    private final SingleTimePasser<Boolean> opponentAnotherGame;

    private final AtomicBoolean otherPlayerActive;
    private final SingleTimePasser<Boolean> connectionEstablishedIndicator;

    public NetworkClientConnector(GameConfiguration gameConfiguration) {
        boardPasser = new SingleTimePasser<>();
        movePasserToMe = new LinkedBlockingQueue<>();
        movePasserToOtherPlayer = new LinkedBlockingQueue<>();

        meAnotherGame = new SingleTimePasser<>();
        opponentAnotherGame = new SingleTimePasser<>();

        otherPlayerActive = new AtomicBoolean(false);

        connectionEstablishedIndicator = new SingleTimePasser<>();

        moveRetriever = new Thread(new MoveRetriever(gameConfiguration.myName, 
                boardPasser,
                movePasserToMe, movePasserToOtherPlayer,
                meAnotherGame, opponentAnotherGame));

        newGameRequestor = new Thread(new NewGameRequestor(meAnotherGame,
                opponentAnotherGame));

        backgroundWorker = new Thread(new BackgroundWorker(gameConfiguration,
                otherPlayerActive, connectionEstablishedIndicator));
    }

    /**
     * Call before the game starts
     */
    public void start() throws InterruptedException {
        backgroundWorker.start();

        newGameRequestor.start();
        
        moveRetriever.start();
        
        try {
            connectionEstablishedIndicator.take();
        }catch(InterruptedException ex) {
            stop();
            throw new InterruptedException(ex.getMessage());
        }
    }

    /**
     * @param input The player that actually decides what moves to make
     * @return A player that uses the passed in player for getting the moves and
     * then sends the moves over the network
     */
    public Player getMe(Player input) {
        return new Me(input, boardPasser, movePasserToOtherPlayer);
    }

    /**
     * @return The client player
     */
    public Player getOtherPlayer() {
        return new OtherPlayer(boardPasser, movePasserToMe);
    }

    public PlayerHandler getPlayerHandler() {
        return new PlayerHandlerImpl(this);
    }

    public void stop() {
        moveRetriever.interrupt();
        newGameRequestor.interrupt();
        backgroundWorker.interrupt();
    }

    public boolean doesOpponentWantAnotherGame() {
        try {
            return opponentAnotherGame.take();
        } catch (InterruptedException ex) {
            return false;
        }
    }

    public void tellTheOpponentIfAnotherGameIsWanted(boolean anotherGame) {
        meAnotherGame.put(anotherGame);
    }
    
    private boolean isOpponentActive() {
        return otherPlayerActive.get();
    }

    private static class MoveRetriever implements Runnable {

        private final PlayerName myName;
        private final SingleTimePasser<Gomoku> boardPasser;
        private final BlockingQueue<Position> movePasserToMe;
        private final BlockingQueue<Position> movePasserToOtherPlayer;

        public MoveRetriever(PlayerName myName,
                SingleTimePasser<Gomoku> boardPasser,
                BlockingQueue<Position> movePasserToMe,
                BlockingQueue<Position> movePasserToOtherPlayer,
                SingleTimePasser<Boolean> meAnotherGame,
                SingleTimePasser<Boolean> opponentAnotherGame) {
            this.myName = myName;
            this.boardPasser = boardPasser;
            this.movePasserToMe = movePasserToMe;
            this.movePasserToOtherPlayer = movePasserToOtherPlayer;
        }

        @Override
        public void run() {
            try ( AsynchronousServerSocketChannel server
                    = AsynchronousServerSocketChannel.open()) {

                server.bind(new InetSocketAddress("localhost", NetworkUtil.PORT));

                Future<AsynchronousSocketChannel> futureChannel = server.accept();

                AsynchronousSocketChannel channel = futureChannel.get();

                // Thread will be interrupted when the two players have stopped playing together
                while (true) {
                    Gomoku board = boardPasser.take();

                    while (board.getGameState() == GameState.ONGOING) {
                        if (myName == board.getCurrentTurn()) {
                            passMoveOnToOtherPlayer(channel);
                        } else {
                            retriveMoveFromOtherPlayer(channel);
                        }

                        board = boardPasser.take();
                    }
                }

            } catch (IOException | InterruptedException | ExecutionException ex) {

            }
        }

        private void passMoveOnToOtherPlayer(AsynchronousByteChannel channel)
                throws IOException, InterruptedException, ExecutionException {
            Position move = movePasserToOtherPlayer.take();

            NetworkUtil.write(channel, NetworkUtil.stringFromPosition(move));

            NetworkUtil.read(channel);
        }

        private void retriveMoveFromOtherPlayer(AsynchronousByteChannel channel)
                throws IOException, InterruptedException, ExecutionException {

            NetworkUtil.write(channel, NetworkUtil.REQUEST_POSITION);

            String input = NetworkUtil.read(channel);

            if (isInputInTheCorrectFormat(input)) {
                Position move = NetworkUtil.positionFromString(input);

                movePasserToMe.put(move);
            }
        }

        private boolean isInputInTheCorrectFormat(String input) {
            if (!input.contains(NetworkUtil.COORDINATE_SEPERATOR)) {
                return false;
            }

            String[] components = input.split(NetworkUtil.COORDINATE_SEPERATOR);

            if (components.length != 2) {
                return false;
            }

            // Check to see if the components are numbers
            try {
                int rowComponent
                        = Integer.parseInt(components[NetworkUtil.COMPONENT_ROW_INDEX]);

                int columnComponent
                        = Integer.parseInt(components[NetworkUtil.COMPONENT_COLUMN_INDEX]);
            } catch (NumberFormatException ex) {
                return false;
            }

            return true;
        }

    }

    private static class NewGameRequestor implements Runnable {

        private final SingleTimePasser<Boolean> meAnotherGame;
        private final SingleTimePasser<Boolean> opponentAnotherGame;

        public NewGameRequestor(SingleTimePasser<Boolean> meAnotherGame,
                SingleTimePasser<Boolean> opponentAnotherGame) {
            this.meAnotherGame = meAnotherGame;
            this.opponentAnotherGame = opponentAnotherGame;
        }

        @Override
        public void run() {
            try ( AsynchronousServerSocketChannel server
                    = AsynchronousServerSocketChannel.open()) {

                server.bind(new InetSocketAddress("localhost", NetworkUtil.NEW_GAME_PORT));

                Future<AsynchronousSocketChannel> futureChannel = server.accept();

                AsynchronousSocketChannel channel = futureChannel.get();

                while (true) {
                    boolean doesServerWantAnotherGame = meAnotherGame.take();

                    if (doesServerWantAnotherGame) {
                        NetworkUtil.write(channel, NetworkUtil.ANOTHER_GAME);
                    } else {
                        NetworkUtil.write(channel, NetworkUtil.NOT_ANOTHER_GAME);
                    }

                    String clientReply = NetworkUtil.read(channel);

                    boolean doesClientWantAnotherGame;

                    if (clientReply.equals(NetworkUtil.ANOTHER_GAME)) {
                        doesClientWantAnotherGame = true;
                    } else {
                        doesClientWantAnotherGame = false;
                    }

                    opponentAnotherGame.put(doesClientWantAnotherGame);
                }

            } catch (ExecutionException | InterruptedException ex) {

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static class BackgroundWorker implements Runnable {

        private final GameConfiguration gameConfiguration;
        private final AtomicBoolean otherPlayerActive;
        private final SingleTimePasser<Boolean> connectionEstablishedIndicator;

        public BackgroundWorker(GameConfiguration gameConfiguration,
                AtomicBoolean otherPlayerActive,
                SingleTimePasser<Boolean> connectionEstablishedIndicator) {
            
            GameConfiguration clientsGameConfiguration = 
                    new GameConfiguration(gameConfiguration.board, 
                            gameConfiguration.gameTimer, 
                            gameConfiguration.myName.turnAfter());
            
            this.gameConfiguration = clientsGameConfiguration;
            this.otherPlayerActive = otherPlayerActive;
            this.connectionEstablishedIndicator = connectionEstablishedIndicator;
        }

        @Override
        public void run() {
            try ( AsynchronousServerSocketChannel server
                    = AsynchronousServerSocketChannel.open()) {

                server.bind(new InetSocketAddress("localhost", NetworkUtil.ACTIVITY_PORT));

                Future<AsynchronousSocketChannel> futureChannel = server.accept();

                AsynchronousSocketChannel channel = futureChannel.get();

                otherPlayerActive.set(true);
                
                GameConfigurationToString gameConfigurationToString = 
                        new GameConfigurationToString();
                
                String gameConfigMessage = 
                        gameConfigurationToString.convert(gameConfiguration);
                
                NetworkUtil.write(channel, gameConfigMessage);

                connectionEstablishedIndicator.put(true);

                NetworkUtil.read(channel);
            } catch (InterruptedException ex) {

            } catch (IOException | ExecutionException ex) {

            }

            otherPlayerActive.set(false);
        }

    }

    private static class PlayerHandlerImpl implements PlayerHandler {

        private final NetworkClientConnector ncc;

        public PlayerHandlerImpl(NetworkClientConnector ncc) {
            this.ncc = ncc;
        }

        @Override
        public void stop() {
            ncc.stop();
        }

        @Override
        public boolean doesOpponentWantAnotherGame() {
            return ncc.isOpponentActive()
                    && ncc.doesOpponentWantAnotherGame();
        }

        @Override
        public void tellTheOpponentIfAnotherGameIsWanted(boolean anotherGame) {
            ncc.tellTheOpponentIfAnotherGameIsWanted(anotherGame);
        }

        @Override
        public boolean isOpponentActive() {
            return ncc.isOpponentActive();
        }

    }
}
