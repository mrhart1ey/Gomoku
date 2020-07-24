package mrhart1ey.gomoku.player.ai;

import java.util.Collection;
import java.util.HashSet;
import mrhart1ey.gomoku.game.GameState;
import mrhart1ey.gomoku.game.Gomoku;
import mrhart1ey.gomoku.game.PlayerName;
import mrhart1ey.gomoku.game.Position;
import mrhart1ey.gomoku.game.PositionContent;

public final class GomokuMoveTracker implements Gomoku {
    private final Gomoku board;
    
    private final GomokuMoveTracker parentBoard;
    private final Position differenceFromParent;
    
    public GomokuMoveTracker(Gomoku board) {
        this(board, null, null);
    }
    
    private GomokuMoveTracker(Gomoku board, 
            GomokuMoveTracker parentBoard, 
            Position differenceFromParent) {
        this.board = board;
        this.parentBoard = parentBoard;
        this.differenceFromParent = differenceFromParent;
    } 
    
    @Override
    public PositionContent getPositionContent(Position position) {
        return board.getPositionContent(position);
    }

    @Override
    public Gomoku move(Position pos) {
        Gomoku newBoard = board.move(pos);
        
        Gomoku newNode = 
                new GomokuMoveTracker(newBoard, this, pos);
        
        return newNode;
    }

    @Override
    public GameState getGameState() {
        return board.getGameState();
    }

    @Override
    public PlayerName getCurrentTurn() {
        return board.getCurrentTurn();
    }
    
    public Collection<Position> difference(Gomoku ancestorBoard) {
        Collection<Position> result = new HashSet<>();
        
        GomokuMoveTracker currentBoard = this;
        
        while(currentBoard != null &&
                currentBoard.differenceFromParent != null &&
                currentBoard != ancestorBoard) {
            result.add(currentBoard.differenceFromParent);
            
            currentBoard = currentBoard.parentBoard;
        }
        
        return result;
    }
    
}
