package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.gui.BoardGraphicalState;

public class DrawJobVictoryLine implements DrawJob {

    private final Position start;
    private final Position end;

    public DrawJobVictoryLine(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(Graphics2D g2) {
        BoardGraphicalState state
                = new BoardGraphicalState(g2.getClipBounds().width,
                        g2.getClipBounds().height);

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(4));

        Point startOnScreenLocation
                = DrawUtil.findOnScreenPieceLocation(state, start);

        Point endOnScreenLocation
                = DrawUtil.findOnScreenPieceLocation(state, end);
        
        g2.drawLine(startOnScreenLocation.x, startOnScreenLocation.y, 
                endOnScreenLocation.x, endOnScreenLocation.y);
    }

}
