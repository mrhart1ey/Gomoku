package mrhart1ey.gomoku.player.gui;

import mrhart1ey.gomoku.game.Gomoku;

public class BoardGraphicalState {
    public static final double BOARD_WIDTH_RATIO = 0.80;
    public static final double BOARD_HEIGHT_RATIO = 0.80;

    public final int boardWidth;
    public final int boardHeight;
    
    public final int boardHorizontalOffset;
    public final int boardVerticalOffset;
    
    public final int barsToDraw;
    
    public final double horizontalBarSeperation;
    public final double verticalBarSeperation;
    
    public BoardGraphicalState(int componentWidth, int componentHeight) {
        this.boardWidth = (int) (componentWidth * BOARD_WIDTH_RATIO);
        this.boardHeight = (int) (componentHeight * BOARD_HEIGHT_RATIO);
        
        this.boardHorizontalOffset = (int) (componentWidth * 
                ((1 - BOARD_WIDTH_RATIO) / 2.0));
        this.boardVerticalOffset = (int) (componentHeight * 
                ((1 - BOARD_HEIGHT_RATIO) / 2.0));
        
        this.barsToDraw = Gomoku.SIZE - 1;
        
        this.horizontalBarSeperation = (double) boardHeight / barsToDraw;
        this.verticalBarSeperation = (double) boardWidth / barsToDraw;
    }
    
}
