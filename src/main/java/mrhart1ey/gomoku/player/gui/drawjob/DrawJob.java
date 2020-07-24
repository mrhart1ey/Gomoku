package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Graphics2D;

/**
 * A draw job is used to signify something that should be drawn onto the screen.
 * Each time a new frame is rendered a new instance of the draw jobs to draw will 
 * be created.
 */
public interface DrawJob {
    
    /**
     * Called when the draw job is to be rendered to the screen
     * 
     * @param g2 THe object to use for drawing
     */
    public void draw(Graphics2D g2);
}
