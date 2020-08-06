package mrhart1ey.gomoku.player.gui.menu;

import java.io.IOException;
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
import mrhart1ey.gomoku.player.network.NetworkServerConnection;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;

public class JoinSessionConfigurationHandler implements Runnable {

    private final SingleTimePasser<SessionConfiguration> sessionConfigPasser;
    private final SingleTimePasser<String> hostPasser;
    private final GameDisplay display;

    public JoinSessionConfigurationHandler(GameDisplay display,
            SingleTimePasser<SessionConfiguration> sessionConfigPasser,
            SingleTimePasser<String> hostPasser) {
        this.sessionConfigPasser = sessionConfigPasser;
        this.hostPasser = hostPasser;
        this.display = display;
    }

    @Override
    public void run() {
        try {
            String hostAddress = hostPasser.take();

            NetworkServerConnection nsc = 
                    NetworkServerConnection.waitForServer(hostAddress);

            GameConfiguration gameConfiguration = nsc.getGameConfiguration();

            PlayerName myName = gameConfiguration.myName;

            Player me = nsc.getMe(display.getMe());

            Gomoku board = gameConfiguration.board;

            Player otherPlayer = nsc.getOpponent();

            DeactivatedGameTimer gameTimer = gameConfiguration.gameTimer;

            PlayerHandler playerHandler = nsc.getPlayerHandler();

            GameMonitor gameMonitor = (b, hasMyTimerRanOut, 
                        hasMyOpponentsTimerRanOut) -> {
                return b.getGameState() == GameState.ONGOING
                        && !hasMyTimerRanOut
                        && !hasMyOpponentsTimerRanOut
                        && playerHandler.isOpponentActive();
            };

            TurnIndicator turnIndicator = (a, b) -> {
                return a == b;
            };

            SessionConfiguration config = new SessionConfiguration(board,
                    me, otherPlayer, myName, gameTimer,
                    playerHandler, gameMonitor, turnIndicator);

            sessionConfigPasser.put(config);
        } catch (InterruptedException ex) {
            
        }catch(IOException ex) {
            System.out.println(ex);
        }
    }

}
