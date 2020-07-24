package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.timer.GameTimer;

public final class GameConfiguration {
    public final Gomoku board;
    public final GameTimer gameTimer;
    public final PlayerName myName;
    
    public GameConfiguration(Gomoku board, GameTimer gameTimer, PlayerName myName) {
        this.board = board;
        this.gameTimer = gameTimer;
        this.myName = myName;
    }
}
