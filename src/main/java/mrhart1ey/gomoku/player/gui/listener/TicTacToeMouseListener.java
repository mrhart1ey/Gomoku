package mrhart1ey.gomoku.player.gui.listener;

import mrhart1ey.gomoku.MultiTimePasser;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import mrhart1ey.gomoku.SingleTimePasser;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.player.gui.BoardGraphicalState;

public class TicTacToeMouseListener extends MouseAdapter {

    private final MultiTimePasser<Dimension> gameComponentSizePasser;
    private final MultiTimePasser<Position> positionMouseIsOverPasser;
    private final SingleTimePasser<Position> movePasser;

    public TicTacToeMouseListener(SingleTimePasser<Position> movePasser,
            MultiTimePasser<Dimension> passer,
            MultiTimePasser<Position> positionMouseIsOverPasser) {
        this.gameComponentSizePasser = passer;
        this.movePasser = movePasser;
        this.positionMouseIsOverPasser = positionMouseIsOverPasser;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        Dimension componentSize = gameComponentSizePasser.get();
        
        int mouseX = event.getX();
        int mouseY = event.getY();
        
        if(isMouseOverABoardPosition(event, componentSize)) {
            positionMouseIsOverPasser.set(
                    boardPositionFromScreenCoordinates(mouseX, mouseY, componentSize));
            
        }else {
            positionMouseIsOverPasser.set(null);
        }
    }

    @Override
    public void mouseReleased(MouseEvent click) {
        Dimension componentSize = gameComponentSizePasser.get();

        Position move = boardPositionFromScreenCoordinates(click.getX(),
                click.getY(), componentSize);

        movePasser.put(move);
    }

    private boolean isMouseOverABoardPosition(MouseEvent mouseLocation,
            Dimension componentSize) {

        int mouseX = mouseLocation.getX();
        int mouseY = mouseLocation.getY();

        BoardGraphicalState state
                = new BoardGraphicalState(componentSize.width, componentSize.height);

        return mouseX > state.boardHorizontalOffset -
                state.verticalBarSeperation / 2
                && mouseX < state.boardWidth +  state.boardHorizontalOffset + 
                state.verticalBarSeperation / 2 
                && mouseY > state.boardVerticalOffset - state.horizontalBarSeperation / 2
                && mouseY < state.boardVerticalOffset + state.boardHeight + 
                state.horizontalBarSeperation / 2;
    }

    private Position boardPositionFromScreenCoordinates(int x, int y,
            Dimension componentSize) {

        BoardGraphicalState state
                = new BoardGraphicalState(componentSize.width, componentSize.height);

        double clickableRegionStartX = (state.boardHorizontalOffset - 
                state.verticalBarSeperation / 2);
        
        double clickableRegionStartY = (state.boardVerticalOffset - 
                state.horizontalBarSeperation / 2);
        
        int column = 
                (int)((x - clickableRegionStartX) / state.verticalBarSeperation);

        int row = 
                (int)((y - clickableRegionStartY) / state.horizontalBarSeperation);

        return new Position(row, column);
    }
}
