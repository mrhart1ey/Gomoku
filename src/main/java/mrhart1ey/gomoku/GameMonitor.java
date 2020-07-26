package mrhart1ey.gomoku;

import mrhart1ey.gomoku.game.Gomoku;

public interface GameMonitor {
    public boolean shouldKeepPlaying(Gomoku board, 
            boolean hasMyTimerRanOut, boolean hasOpponentsTimerRanOut);
}
