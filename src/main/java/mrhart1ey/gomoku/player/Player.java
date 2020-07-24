package mrhart1ey.gomoku.player;

import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.Gomoku;


/**
 * The interface abstracts a Gomoku player. 
 * A player should be interruptible. 
 * An instance of a player should only be used for one instance of a game, and 
 * for one instance of a game no other player should make a move in-place of the
 * original player. 
 */
public interface Player {
    
    /**
     * @param board The board that the player should use the evaluate the next move
     * @return The position that the player wants to move to
     */
    Position nextMove(Gomoku board);
}
