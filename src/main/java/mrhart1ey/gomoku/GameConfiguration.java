package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.timer.DeactivatedGameTimer;

public final class GameConfiguration {
    public final Gomoku board;
    public final DeactivatedGameTimer gameTimer;
    public final PlayerName myName;
    
    public GameConfiguration(Gomoku board, DeactivatedGameTimer gameTimer, 
            PlayerName myName) {
        this.board = board;
        this.gameTimer = gameTimer;
        this.myName = myName;
    }
}
