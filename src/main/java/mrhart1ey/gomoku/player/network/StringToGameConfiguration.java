package mrhart1ey.gomoku.player.network;

import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.GomokuImpl;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.timer.Clock;
import mrhart1ey.gomoku.timer.FixedTurnGameTimer;
import mrhart1ey.gomoku.timer.GameTimer;
import mrhart1ey.gomoku.timer.InfiniteGameTimer;
import mrhart1ey.gomoku.timer.StandardGameTimer;
import mrhart1ey.gomoku.timer.SystemClock;

public final class StringToGameConfiguration {

    public GameConfiguration convert(String gameConfigurationasString) {
        String[] configurationParts
                = gameConfigurationasString.split(
                        GameConfigurationToString.CONFIGURATION_SEPERATOR);

        PlayerName playerName = convertPlayerName(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_MY_NAME]);

        Gomoku board = convertGomoku(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_GOMOKU]);

        GameTimer gameTimer = convertGameTimer(configurationParts[GameConfigurationToString.CONFIGURATION_INDEX_GAME_TIMER]);

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

    private GameTimer convertGameTimer(String gameTimer) {
        if (gameTimer.startsWith("INFINITE")) {
            return new InfiniteGameTimer();
        } else if (gameTimer.startsWith("FIXED_TURN")) {
            long timePerTurn
                    = Long.parseLong(gameTimer.split(
                            GameConfigurationToString.PARAMETER_SEPERATOR)[1]);

            Clock defaultClock = new SystemClock();

            return new FixedTurnGameTimer(defaultClock, timePerTurn);
        }

        String[] parameters
                = gameTimer.split(GameConfigurationToString.PARAMETER_SEPERATOR);

        long reserveTime
                = Long.parseLong(parameters[1]);

        long timeAddedPerTurn
                = Long.parseLong(parameters[2]);

        Clock defaultClock = new SystemClock();

        return new StandardGameTimer(defaultClock, reserveTime, timeAddedPerTurn);
    }
}
