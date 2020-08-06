package mrhart1ey.gomoku.player.gui;

import java.util.List;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.Player;
import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;

/**
 * A GameDisplay is used for drawing the game to the screen
 */
public interface GameDisplay {

    /**
     * @param board The game board to draw
     * @param widgets Additional items to draw
     */
    public void draw(DrawJob board, List<DrawJob> widgets);

    /**
     * @return The board position that the mouse is over, or null if the mouse
     * is not over a board position
     */
    public Position getBoardPositionMouseIsCurrentlyOver();
    
    public Player getMe();
    
    public void show();
    
    public boolean hasSpaceBarBeenPressed();
    
    public boolean hasEscapeKeyBeenPressed();
    
    public void clearKeyPresses();
}
