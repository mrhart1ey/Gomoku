package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.timer.GameTimer;

public interface GameMonitor {
    public boolean shouldKeepPlaying(Gomoku board, 
            GameTimer myTimer, GameTimer opponentsTimer);
}
