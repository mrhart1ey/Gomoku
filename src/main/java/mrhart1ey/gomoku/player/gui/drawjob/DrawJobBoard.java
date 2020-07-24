package mrhart1ey.gomoku.player.gui.drawjob;

import mrhart1ey.gomoku.player.gui.drawjob.DrawJob;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.player.gui.BoardGraphicalState;

public class DrawJobBoard implements DrawJob {

    private static final Collection<String> columnLabels;
    private static final Collection<String> rowLabels;

    static {
        columnLabels = new ArrayList<>();
        char columnLabel = 'A';
        for (int column = 0; column < Gomoku.SIZE; column++, columnLabel++) {
            columnLabels.add(Character.toString(columnLabel));
        }

        rowLabels = new ArrayList<>();
        int rowLabel = 1;
        for (int row = 0; row < Gomoku.SIZE; row++, rowLabel++) {
            rowLabels.add(Integer.toString(rowLabel));
        }
    }

    private final Gomoku board;

    public DrawJobBoard(Gomoku board) {
        this.board = board;
    }

    @Override
    public void draw(Graphics2D g2) {
        int componentWidth = g2.getClipBounds().width;
        int componentHeight = g2.getClipBounds().height;

        g2.setColor(new Color(242, 255, 186));
        g2.fillRect(0, 0, componentWidth, componentHeight);
        g2.setColor(Color.BLACK);

        BoardGraphicalState state = new BoardGraphicalState(componentWidth,
                componentHeight);

        Point topLeft = new Point(state.boardHorizontalOffset,
                state.boardVerticalOffset);

        Point bottomLeft = new Point(state.boardHorizontalOffset,
                state.boardVerticalOffset + state.boardHeight);

        Point topRight = new Point(
                state.boardWidth + state.boardHorizontalOffset,
                state.boardVerticalOffset);

        Point bottomRight = new Point(state.boardWidth
                + state.boardHorizontalOffset,
                state.boardVerticalOffset + state.boardHeight);

        g2.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);

        g2.drawLine(topLeft.x, topLeft.y, topRight.x, topRight.y);

        g2.drawLine(bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y);

        g2.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y);

        for (int horizontalBar = 1; horizontalBar < state.barsToDraw;
                horizontalBar++) {
            g2.drawLine(state.boardHorizontalOffset,
                    state.boardVerticalOffset
                    + (int) (state.horizontalBarSeperation * horizontalBar),
                    state.boardHorizontalOffset + state.boardWidth,
                    state.boardVerticalOffset
                    + (int) (state.horizontalBarSeperation * horizontalBar));
        }

        for (int verticalBar = 1; verticalBar < state.barsToDraw;
                verticalBar++) {
            g2.drawLine(state.boardHorizontalOffset
                    + (int) (state.verticalBarSeperation * verticalBar),
                    state.boardVerticalOffset,
                    state.boardHorizontalOffset
                    + (int) (state.verticalBarSeperation * verticalBar),
                    state.boardVerticalOffset + state.boardHeight);
        }

        drawLabels(g2, state);

        drawContent(g2, board, state.boardHorizontalOffset,
                state.boardVerticalOffset, state.horizontalBarSeperation,
                state.verticalBarSeperation);
    }

    private void drawContent(Graphics2D g2, Gomoku board, int boardHorizontalOffset,
            int boardVerticalOffset, double horizontalBarSeperation,
            double verticalBarSeperation) {

        for (int row = 0; row < Gomoku.SIZE; row++) {
            for (int column = 0; column < Gomoku.SIZE; column++) {
                int circleCenterX = boardHorizontalOffset
                        + (int) (verticalBarSeperation * column);
                int circleCenterY = boardVerticalOffset
                        + (int) (horizontalBarSeperation * row);

                int radius = (int) (Math.min(verticalBarSeperation, horizontalBarSeperation)
                        * 0.6);

                if (board.getPositionContent(new Position(row, column))
                        == PositionContent.BLACK) {
                    DrawUtil.drawBlackPiece(g2, circleCenterX, circleCenterY, radius);
                } else if (board.getPositionContent(new Position(row, column))
                        == PositionContent.WHITE) {
                    DrawUtil.drawWhitePiece(g2, circleCenterX, circleCenterY, radius);
                }
            }
        }
    }

    private void drawLabels(Graphics2D g2, BoardGraphicalState state) {
        int rowLabelWidth = (int) (state.verticalBarSeperation * 0.5);
        int rowLabelHeight = (int) (state.horizontalBarSeperation);

        int columnLabelWidth = (int) (state.verticalBarSeperation);
        int columnLabelHeight = (int) (state.horizontalBarSeperation * 0.5);

        Font potentialFont1 = DrawUtil.largestStandardFontSoAllTextsFitInBound(g2,
                rowLabels, rowLabelWidth, rowLabelHeight);

        Font potentialFont2 = DrawUtil.largestStandardFontSoAllTextsFitInBound(g2,
                columnLabels, columnLabelWidth, columnLabelHeight);
        
        Font labelFont;
        
        if(potentialFont1.getSize() < potentialFont2.getSize()) {
            labelFont = potentialFont1;
        }else {
            labelFont = potentialFont2;
        }
        
        drawRowNumbers(g2, state, labelFont, rowLabelWidth, rowLabelHeight);
        drawColumnNumbers(g2, state, labelFont, columnLabelWidth, columnLabelHeight);
    }

    private void drawRowNumbers(Graphics2D g2, BoardGraphicalState state, 
            Font font, int labelWidth, int labelHeight) {
        
        int leftLabelX = state.boardHorizontalOffset
                - (int) (labelWidth * 2);

        int rightLabelX = (state.boardWidth + state.boardHorizontalOffset)
                + labelWidth;

        double topLeftCurrentY = state.boardVerticalOffset
                - state.horizontalBarSeperation / 2;

        g2.setFont(font);

        for (String rowLabel : rowLabels) {
            DrawUtil.drawText(g2, rowLabel, leftLabelX, (int) topLeftCurrentY,
                    labelWidth, labelHeight);
            
            DrawUtil.drawText(g2, rowLabel,
                    rightLabelX, (int) topLeftCurrentY, labelWidth, labelHeight);

            topLeftCurrentY += state.horizontalBarSeperation;
        }
    }

    private void drawColumnNumbers(Graphics2D g2, BoardGraphicalState state, 
            Font font, int labelWidth, int labelHeight) {

        int topLabelY = state.boardVerticalOffset
                - (int) state.horizontalBarSeperation;

        int bottomLabelY = (state.boardHeight + state.boardVerticalOffset)
                + labelHeight;

        double topLeftCurrentX = state.boardHorizontalOffset
                - state.verticalBarSeperation / 2;

        g2.setFont(font);

        for (String columnLabel : columnLabels) {
            DrawUtil.drawText(g2, columnLabel, (int) topLeftCurrentX, topLabelY,
                    labelWidth, labelHeight);

            DrawUtil.drawText(g2, columnLabel,
                    (int) topLeftCurrentX, bottomLabelY, labelWidth, labelHeight);

            topLeftCurrentX += state.verticalBarSeperation;
        }
    }

}
