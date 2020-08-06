package mrhart1ey.gomoku.player.network.protocol;

import mrhart1ey.gomoku.player.network.protocol.GameConfigurationToString;
import java.time.Duration;
import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.GomokuImpl;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;
import mrhart1ey.gomoku.timer.FixedTurnGameTimer;
import mrhart1ey.gomoku.timer.InfiniteGameTimer;
import mrhart1ey.gomoku.timer.StandardGameTimer;

public final class StringToGameConfiguration {

    public GameConfiguration convert(String gameConfigurationasString) {
        String[] configurationParts
                = gameConfigurationasString.split(
                        GameConfigurationToString.CONFIGURATION_SEPERATOR);

        PlayerName playerName = convertPlayerName(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_MY_NAME]);

        Gomoku board = convertGomoku(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_GOMOKU]);

        DeactivatedGameTimer gameTimer = 
                convertGameTimer(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_GAME_TIMER]);

        return new GameConfiguration(board, gameTimer, playerName);
    }

    private PlayerName convertPlayerName(String playerName) {
        if (playerName.equals("BLACK")) {
            return PlayerName.BLACK;
        }

        return PlayerName.WHITE;
    }

    private Gomoku convertGomoku(String board) {
        return new GomokuImpl();
    }

    private DeactivatedGameTimer convertGameTimer(String gameTimer) {
        if (gameTimer.startsWith("INFINITE")) {
            return new InfiniteGameTimer();
        } else if (gameTimer.startsWith("FIXED_TURN")) {
            long timePerTurn
                    = Long.parseLong(gameTimer.split(
                            GameConfigurationToString.PARAMETER_SEPERATOR)[1]);

            return new FixedTurnGameTimer(Duration.ofMillis(timePerTurn));
        }

        String[] parameters
                = gameTimer.split(GameConfigurationToString.PARAMETER_SEPERATOR);

        long reserveTime
                = Long.parseLong(parameters[1]);

        long timeAddedPerTurn
                = Long.parseLong(parameters[2]);

        return new StandardGameTimer(Duration.ofMillis(reserveTime), 
                Duration.ofMillis(timeAddedPerTurn));
    }
}
