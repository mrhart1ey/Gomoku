package mrhart1ey.gomoku.player.ai;

import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;

public interface GomokuHeuristic {
    
    /**
     * @param board The updates board
     * @return A score indicating how good the board looks for a player, after 
     * the passed in move has been made on the passed in board
     */
    public GomokuHeuristic evaluate(Gomoku board, PlayerName forPlayer);
    
    public long getScore();
}
