package mrhart1ey.gomoku.player.gui.drawjob;

import java.awt.Graphics2D;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;
import mrhart1ey.gomoku.player.gui.BoardGraphicalState;

public class DrawJobMouseHover implements DrawJob {

    private final Gomoku board;
    private final Position position;

    public DrawJobMouseHover(Gomoku board, Position positionMouseIsHovering) {
        this.board = board;
        this.position = positionMouseIsHovering;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (position != null
                && board.getPositionContent(position) == PositionContent.FREE) {
            BoardGraphicalState state = new BoardGraphicalState(
                    (int) g2.getClipBounds().getWidth(),
                    (int) g2.getClipBounds().getHeight());

            int circleCenterX = state.boardHorizontalOffset
                    + (int) (state.verticalBarSeperation * position.column);
            int circleCenterY = state.boardVerticalOffset
                    + (int) (state.horizontalBarSeperation * position.row);

            int radius = (int) (Math.min(state.verticalBarSeperation,
                    state.horizontalBarSeperation)
                    * 0.6);

            if (board.getCurrentTurn()
                    == PlayerName.BLACK) {
                DrawUtil.drawBlackPiece(g2, circleCenterX, circleCenterY, radius);
            } else  {
                DrawUtil.drawWhitePiece(g2, circleCenterX, circleCenterY, radius);
            }
        }
    }

}
