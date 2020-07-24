package mrhart1ey.gomoku.player;

import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;

/**
 * The interface abstracts a Gomoku player.
 */
public interface Player {

    /**
     * A player should be interruptible, and after it has been interrupted it
     * should come to a stop as early as possible. After a player has been
     * interrupted it should not be asked for another move.
     * 
     * The move that the player returns does not necessary have to one that the 
     * gomoku board will accept.
     *
     * @param board The board that the player should use the evaluate the next
     * move
     * @return The position that the player wants to move to
     */
    Position nextMove(Gomoku board);
}
