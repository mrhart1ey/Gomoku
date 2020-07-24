package mrhart1ey.gomoku.player.gui;

import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class TicTacToeComponent extends JComponent {

    private DrawJob masterDrawer;

    public TicTacToeComponent() {
        masterDrawer = ((g) -> {
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        masterDrawer.draw(g2);
    }
    
    /**
     * Call this before paintComponent to draw the new jobs
     * 
     * @param board The board to draw
     * @param widgets The extras to draw
     */
    public void update(DrawJob board, List<DrawJob> widgets) {
        masterDrawer = new DrawingJobMaster(board, widgets);
    }

    private static class DrawingJobMaster implements DrawJob {

        private final List<DrawJob> drawingJobs;

        public DrawingJobMaster(DrawJob board, List<DrawJob> widgets) {
            drawingJobs = new ArrayList<>();

            drawingJobs.add(board);
            drawingJobs.addAll(widgets);
        }

        @Override
        public void draw(Graphics2D g2) {
            for (DrawJob drawingJob : drawingJobs) {
                Font oldFont = g2.getFont();
                Color oldColor = g2.getColor();
                Stroke oldStroke = g2.getStroke();

                RenderingHints rh = new RenderingHints(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setRenderingHints(rh);

                drawingJob.draw(g2);

                g2.setFont(oldFont);
                g2.setColor(oldColor);
                g2.setStroke(oldStroke);
            }
        }

    }
}
