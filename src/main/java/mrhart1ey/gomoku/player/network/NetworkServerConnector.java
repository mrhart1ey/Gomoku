package mrhart1ey.gomoku.player.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.PlayerHandler;

/**
 * Connects the client to a server
 */
final public class NetworkServerConnector {

    private final Thread moveRetriever;
    private final Thread newGameRequestor;
    private final Thread backgroundWorker;

    private final SingleTimePasser<Gomoku> boardPasser;
    private final BlockingQueue<Position> movePasserToMe;
    private final BlockingQueue<Position> movePasserToOtherPlayer;

    private final SingleTimePasser<Boolean> meAnotherGame;
    private final SingleTimePasser<Boolean> opponentAnotherGame;

    private final SingleTimePasser<GameConfiguration> gameConfigurationPasser;
    private final AtomicBoolean otherPlayerActive;

    public NetworkServerConnector(String host) {
        boardPasser = new SingleTimePasser<>();
        movePasserToMe = new LinkedBlockingQueue<>();
        movePasserToOtherPlayer = new LinkedBlockingQueue<>();

        meAnotherGame = new SingleTimePasser<>();
        opponentAnotherGame = new SingleTimePasser<>();

        gameConfigurationPasser = new SingleTimePasser<>();

        otherPlayerActive = new AtomicBoolean(false);

        moveRetriever = new Thread(new MoveRetriever(host, boardPasser,
                movePasserToMe, movePasserToOtherPlayer));

        newGameRequestor = new Thread(new NewGameRequestor(meAnotherGame,
                opponentAnotherGame));

        backgroundWorker = new Thread(new ActivityChecker(gameConfigurationPasser,
                otherPlayerActive));
    }

    /**
     * Call before the game starts
     */
    public GameConfiguration start() throws InterruptedException {
        backgroundWorker.start();

        newGameRequestor.start();
        
        moveRetriever.start();
        
        try {
            return gameConfigurationPasser.take();
        }catch(InterruptedException ex) {
            stop();
            throw new InterruptedException(ex.getMessage());
        }
    }

    private boolean isOpponentActive() {
        return otherPlayerActive.get();
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

    /**
     * @param input The player that actually decides what moves to make
     * @return A player that uses the passed in player for getting the moves and
     * then sends the moves over the network
     */
    public Player getMe(Player input) {
        return new Me(input, boardPasser, movePasserToOtherPlayer);
    }

    /**
     * @return The server player
     */
    public Player getOtherPlayer() {
        return new OtherPlayer(boardPasser, movePasserToMe);
    }

    private static class MoveRetriever implements Runnable {

        private final String host;
        private final SingleTimePasser<Gomoku> boardPasser;
        private final BlockingQueue<Position> movePasserToMe;
        private final BlockingQueue<Position> movePasserToOtherPlayer;

        public MoveRetriever(String host, SingleTimePasser<Gomoku> boardPasser,
                BlockingQueue<Position> movePasserToMe,
                BlockingQueue<Position> movePasserToOtherPlayer) {
            this.host = host;
            this.boardPasser = boardPasser;
            this.movePasserToMe = movePasserToMe;
            this.movePasserToOtherPlayer = movePasserToOtherPlayer;
        }

        @Override
        public void run() {
            boolean connected = false;

            while (!connected) {
                connected = tryToConnectToServer();
            }
        }

        private boolean tryToConnectToServer() {
            try ( AsynchronousSocketChannel channel
                    = AsynchronousSocketChannel.open()) {

                Future<Void> connectionWaiter
                        = channel.connect(new InetSocketAddress(host,
                                NetworkUtil.PORT));

                connectionWaiter.get();

                Gomoku board;

                String reply;

                while (true) {
                    do {
                        reply = NetworkUtil.read(channel);

                        if (!reply.isEmpty() && reply.equals(NetworkUtil.REQUEST_POSITION)) {
                            Position move = movePasserToOtherPlayer.take();

                            NetworkUtil.write(channel,
                                    NetworkUtil.stringFromPosition(move));
                        } else if (!reply.isEmpty()) {
                            Position move = NetworkUtil.positionFromString(reply);

                            movePasserToMe.put(move);

                            NetworkUtil.write(channel, NetworkUtil.ACK);
                        }

                        board = boardPasser.take();
                    } while (board.getGameState() == GameState.ONGOING);
                }

            } catch (ExecutionException ex) {
                return false;
            } catch (IOException | InterruptedException ex) {

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
            boolean answered = false;

            while (!answered) {
                try ( AsynchronousSocketChannel channel
                        = AsynchronousSocketChannel.open()) {

                    Future<Void> connectionWaiter
                            = channel.connect(new InetSocketAddress("localhost",
                                    NetworkUtil.NEW_GAME_PORT));

                    connectionWaiter.get();
                    
                    answered = true;
                    
                    while (true) {
                        boolean doesServerWantAnotherGame;

                        String serverMessage = NetworkUtil.read(channel);

                        if (serverMessage.equals(NetworkUtil.ANOTHER_GAME)) {
                            doesServerWantAnotherGame = true;
                        } else {
                            doesServerWantAnotherGame = false;
                        }

                        opponentAnotherGame.put(doesServerWantAnotherGame);

                        boolean doesClientWantAnotherGame = meAnotherGame.take();

                        if (doesClientWantAnotherGame) {
                            NetworkUtil.write(channel, NetworkUtil.ANOTHER_GAME);
                        } else {
                            NetworkUtil.write(channel, NetworkUtil.NOT_ANOTHER_GAME);
                        }
                    }

                } catch (ExecutionException | InterruptedException ex) {
                    answered = true;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static class ActivityChecker implements Runnable {

        private final SingleTimePasser<GameConfiguration> gameConfigurationPasser;
        private final AtomicBoolean otherPlayerActive;

        public ActivityChecker(
                SingleTimePasser<GameConfiguration> gameConfigurationFactoryPasser,
                AtomicBoolean otherPlayerActive) {
            this.gameConfigurationPasser = gameConfigurationFactoryPasser;
            this.otherPlayerActive = otherPlayerActive;
        }

        @Override
        public void run() {
            boolean answered = false;

            PlayerName result;

            while (!answered) {
                try ( AsynchronousSocketChannel channel
                        = AsynchronousSocketChannel.open()) {

                    Future<Void> connectionWaiter
                            = channel.connect(new InetSocketAddress("localhost",
                                    NetworkUtil.ACTIVITY_PORT));

                    connectionWaiter.get();

                    String gameConfigString = NetworkUtil.read(channel);

                    StringToGameConfiguration stringToGameConfiguration
                            = new StringToGameConfiguration();

                    gameConfigurationPasser.put(
                            stringToGameConfiguration.convert(gameConfigString));

                    answered = true;

                    otherPlayerActive.set(true);

                    NetworkUtil.read(channel);
                } catch (ExecutionException | InterruptedException ex) {
                    answered = true;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            otherPlayerActive.set(false);
        }
    }

    private static class PlayerHandlerImpl implements PlayerHandler {

        private final NetworkServerConnector nsc;

        public PlayerHandlerImpl(NetworkServerConnector nsc) {
            this.nsc = nsc;
        }

        @Override
        public void stop() {
            nsc.stop();
        }

        @Override
        public boolean doesOpponentWantAnotherGame() {
            return nsc.isOpponentActive()
                    && nsc.doesOpponentWantAnotherGame();
        }

        @Override
        public void tellTheOpponentIfAnotherGameIsWanted(boolean anotherGame) {
            nsc.tellTheOpponentIfAnotherGameIsWanted(anotherGame);
        }

        @Override
        public boolean isOpponentActive() {
            return nsc.isOpponentActive();
        }

    }
}
