package mrhart1ey.gomoku.player.ai;

import java.util.Set;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.Position;

/**
 * Suggests possible moves to an AI.
 */
public interface PossibleMoveGenerator {
    
    /**
     * @param board The board to find possible moves for
     * @return A set of moves that can be made on the passed in board
     */
    public Set<Position> generate(Gomoku board);
}
