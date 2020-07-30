package mrhart1ey.gomoku.player.ai;

import java.util.Set;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;

/**
 * Quantifies how good a particular instance of a Gomoku board is.
 * 
 * All classes that implement this interface should be immutable and thread safe.
 */
public interface GomokuHeuristic {
    
    /**
     * @param board A board to score
     * @param difference The positions that have changed since the last board
     * @return An instance of GomokuHeuristic that has the score for the passed
     * in board, and allows another board to be evaluated
     */
    public GomokuHeuristic evaluate(Gomoku board, Set<Position> difference);
    
    /**
     * @return The score assigned to the board that it evaluated
     */
    public long getScore();
}
