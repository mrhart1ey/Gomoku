package mrhart1ey.gomoku.player.network.protocol;

import mrhart1ey.gomoku.GameConfiguration;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.timer.FixedTurnGameTimer;
import mrhart1ey.gomoku.timer.GameTimer;
import mrhart1ey.gomoku.timer.InfiniteGameTimer;
import mrhart1ey.gomoku.timer.StandardGameTimer;

public final class GameConfigurationToString {

    static final String CONFIGURATION_SEPERATOR = ":";
    static final String PARAMETER_SEPERATOR = ",";
    
    static final int CONFIGURATION_INDEX_MY_NAME = 0;
    static final int CONFIGURATION_INDEX_GOMOKU = 1;
    static final int CONFIGURATION_INDEX_GAME_TIMER = 2;

    public String convert(GameConfiguration gameConfiguration) {
        StringBuilder result = new StringBuilder();

        String myNameConfig = convertMyName(gameConfiguration.myName);
        result.append(myNameConfig);
        result.append(CONFIGURATION_SEPERATOR);

        String gomokuConfig = convertGomoku(gameConfiguration.board);
        result.append(gomokuConfig);
        result.append(CONFIGURATION_SEPERATOR);

        String gameTimerConfig = convertGameTimer(gameConfiguration.gameTimer);
        result.append(gameTimerConfig);

        return result.toString();
    }

    private String convertMyName(PlayerName myName) {
        if (myName.equals(PlayerName.BLACK)) {
            return "BLACK";
        }

        return "WHITE";
    }

    private String convertGomoku(Gomoku board) {
        return "STANDARD";
    }

    private String convertGameTimer(GameTimer gameTimer) {
        if (gameTimer instanceof InfiniteGameTimer) {
            return "INFINITE";
        } else if (gameTimer instanceof FixedTurnGameTimer) {
            FixedTurnGameTimer castedGameTimer = (FixedTurnGameTimer) gameTimer;

            return "FIXED_TURN" + PARAMETER_SEPERATOR + 
                    castedGameTimer.getTimeLeft(null).toMillis();
        }else if(gameTimer instanceof StandardGameTimer) {
            StandardGameTimer castedGameTimer = (StandardGameTimer) gameTimer;
            
            return "STANDARD" + PARAMETER_SEPERATOR 
                    + castedGameTimer.getTimeLeft(null).toMillis()
                    + PARAMETER_SEPERATOR 
                    + castedGameTimer.getTimeAddedPerTurn().toMillis();
        }
        
        throw new IllegalArgumentException("Unknown type of timer");
    }
}
