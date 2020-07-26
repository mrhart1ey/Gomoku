package mrhart1ey.gomoku.player.gui.menu;

import mrhart1ey.gomoku.BlankPlayerHandler;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.GameMonitor;
import mrhart1ey.gomoku.PlayerHandler;
import mrhart1ey.gomoku.SessionConfiguration;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.TurnIndicator;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.player.gui.GameDisplay;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;
import mrhart1ey.gomoku.timer.GameTimer;

public class YourselfSessionConfigurationHandler implements Runnable {

    private final SingleTimePasser<SessionConfiguration> sessionConfigPasser;
    private final SingleTimePasser<GameConfiguration> gameConfigPasser;
    private final GameDisplay display;

    public YourselfSessionConfigurationHandler(GameDisplay display,
            SingleTimePasser<SessionConfiguration> sessionConfigFactoryPasser,
            SingleTimePasser<GameConfiguration> gameConfigFactoryPasser) {

        this.sessionConfigPasser = sessionConfigFactoryPasser;
        this.gameConfigPasser = gameConfigFactoryPasser;
        this.display = display;
    }

    @Override
    public void run() {
        try {
            GameConfiguration gameConfiguration
                    = gameConfigPasser.take();

            Player me = display.getMe();

            Player otherPlayer = display.getMe();

            PlayerName myName = gameConfiguration.myName;

            Gomoku board = gameConfiguration.board;

            DeactivatedGameTimer gameTimer = gameConfiguration.gameTimer;

            PlayerHandler playerHandler = new BlankPlayerHandler();

            GameMonitor gameMonitor = (b, hasMyTimerRanOut, 
                        hasMyOpponentsTimerRanOut) -> {
                return b.getGameState() == GameState.ONGOING
                        && !hasMyTimerRanOut
                        && !hasMyOpponentsTimerRanOut;

            };

            TurnIndicator turnIndicator = (a, b) -> {
                return true;
            };

            SessionConfiguration config = new SessionConfiguration(board,
                    me, otherPlayer, myName, gameTimer,
                    playerHandler, gameMonitor, turnIndicator);

            sessionConfigPasser.put(config);
        } catch (InterruptedException ex) {

        }
    }

}
