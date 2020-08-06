package mrhart1ey.gomoku.player.network;

import mrhart1ey.gomoku.PlayerHandler;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.player.network.protocol.NetworkCommunicator;

final class NetworkPlayerHandler implements PlayerHandler {

    private final NetworkCommunicator communicator;
    private final Thread worker;
    private final SingleTimePasser<Boolean> doIWantANewGame;
    private final SingleTimePasser<Boolean> doesOpponentWantANewGame;
    private final SingleTimePasser<Boolean> gameInProgress;

    public NetworkPlayerHandler(NetworkCommunicator communicator,
            Thread worker, SingleTimePasser<Boolean> doIWantANewGame,
            SingleTimePasser<Boolean> doesOpponentWantANewGame,
            SingleTimePasser<Boolean> gameInProgress) {

        this.communicator = communicator;
        this.worker = worker;
        this.doIWantANewGame = doIWantANewGame;
        this.doesOpponentWantANewGame = doesOpponentWantANewGame;
        this.gameInProgress = gameInProgress;
    }

    @Override
    public void stop() {
        worker.interrupt();
        communicator.close();
    }

    @Override
    public boolean doesOpponentWantAnotherGame() {
        try {
            return doesOpponentWantANewGame.take();
        } catch (InterruptedException ex) {
            return false;
        }
    }

    @Override
    public void tellTheOpponentIfIWantAnotherGame(boolean anotherGame) {
        gameInProgress.put(false);

        doIWantANewGame.put(anotherGame);
    }

    @Override
    public boolean isOpponentActive() {
        return communicator.isOpponentActive();
    }

}
